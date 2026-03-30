// ==UserScript==
// @name         ad_aiqiyi_rules_20260330_v50定稿
// @namespace    http://tampermonkey.net/
// @version      2.7.1
// @description  爱奇艺网页端：保守稳定版去广告（防弹窗、短广告跳过、低风险）
// @author       yuHao
// @match        https://www.iqiyi.com/*
// @match        https://*.iqiyi.com/*
// @run-at       document-start
// @grant        none
// @license      MIT
// @noframes
// ==/UserScript==

(function () {
    "use strict";

    // =========================
    // 运行参数（性能与行为）
    // =========================
    // 正常轮询间隔（毫秒）：非广告态时的默认扫描频率。
    // 值越小越灵敏，但 CPU 占用越高；700ms 是在稳定性与性能间的折中。
    var LOOP_IDLE_MS = 700;
    // 正片稳定后轮询间隔（毫秒）：持续稳定播放后降频到此值，显著降低资源占用。
    var LOOP_IDLE_STABLE_MS = 11000;
    // 广告态轮询间隔（毫秒）：检测到广告线索后使用高频扫描，保证尽快跳过。
    var LOOP_ACTIVE_MS = 120;
    // 后台/未聚焦标签页轮询间隔（毫秒）：页面不可见或失焦时大幅降频。
    var LOOP_BACKGROUND_MS = 20000;
    // 广告信号有效期（毫秒）：在该窗口内认为“最近发生过广告相关行为”。
    var AD_SIGNAL_TTL_MS = 1600;
    // 主视频最小面积阈值（像素）：过滤小窗/装饰视频，避免误操作非主播放器。
    var MIN_MAIN_VIDEO_AREA = 60000;
    // 最近一次广告线索时间（毫秒时间戳）。
    var lastAdSignalAt = 0;
    // 最近一次用户主动交互时间（点击/按键/触摸等）。
    var lastUserActionAt = 0;
    // 正片稳定播放起始时间，用于触发“分阶段降频”。
    var stablePlaybackSince = 0;
    // 近 5 秒 window.open 时间列表，用于识别异常连续弹窗。
    var recentOpenAtList = [];
    // 主视频缓存，减少每轮 querySelectorAll("video") 的频次。
    var cachedMainVideo = null;
    var cachedMainVideoAt = 0;
    var MAIN_VIDEO_CACHE_TTL_MS = 4000;
    // 交互触发节流，避免连续点击/按键造成高频 runOnce。
    var ACTION_RUN_MIN_INTERVAL_MS = 350;
    var lastActionRunAt = 0;
    // 插件开关：关闭后仅保留开关按钮，不执行去广告逻辑（默认开启）。
    var PLUGIN_ENABLE_KEY = "__iqiyi_adguard_enabled_v1";
    var TOGGLE_BTN_ID = "__iqiyi_adguard_toggle_btn";
    var LOOP_DISABLED_MS = 60000;
    var TOGGLE_BTN_POS_KEY = "__iqiyi_adguard_toggle_pos_v1";
    var TOGGLE_BTN_DRAG_MIN_PX = 6;
    var TOGGLE_ENSURE_INTERVAL_MS = 5000;
    var lastToggleEnsureAt = 0;

    // 广告请求关键字（仅保留保守命中，避免误伤主流）
    var AD_URL_PATTERNS = [
        "cupid.iqiyi.com",
        "api.cupid.iqiyi.com",
        "t7z.cupid.iqiyi.com",
        "adx.iqiyi.com",
        "adproxy",
        "adid=",
        "cupidv3",
        "/pausead/",
        "/pause_ad/"
    ];

    // 安全跳过节点选择器：仅选已验证的倒计时/跳过区域，避免误点业务按钮。
    var SAFE_SKIP_SELECTORS = ".cd-time,.public-time,.countdown-topright";
    // 可维护文案列表：广告跳过线索关键词（用于识别广告态和可点击目标）。
    var SKIP_HINT_TEXT_LIST = ["可跳过广告", "后可跳过广告", "跳过广告"];
    // 可维护文案列表：广告落地/拉起页关键词（命中则禁止点击）。
    var LANDING_BLOCK_TEXT_LIST = ["查看详情", "开通会员", "立即开通", "去下载", "立即下载", "了解更多"];
    // 可维护 class 列表：允许自动点击的“安全跳过节点”类名。
    var SAFE_SKIP_CLASS_LIST = ["cd-time", "public-time", "countdown-topright"];

    // 返回当前时间戳（毫秒）。
    function now() {
        return Date.now();
    }

    // 读取开关状态：默认 true（开启）。
    function isPluginEnabled() {
        try {
            var raw = window.localStorage.getItem(PLUGIN_ENABLE_KEY);
            if (raw === null || raw === "") {
                return true;
            }
            return raw === "1";
        } catch (e) {
            return true;
        }
    }

    // 写入开关状态并刷新按钮文案。
    function setPluginEnabled(enabled) {
        try {
            window.localStorage.setItem(PLUGIN_ENABLE_KEY, enabled ? "1" : "0");
        } catch (e) {
            // ignore
        }
        updateToggleButtonText();
    }

    // 刷新开关按钮文案与颜色。
    function updateToggleButtonText() {
        var btn = document.getElementById(TOGGLE_BTN_ID);
        if (!btn) {
            return;
        }
        var enabled = isPluginEnabled();
        btn.textContent = enabled ? "去广告: 开" : "去广告: 关";
        btn.style.setProperty("background", enabled ? "rgba(22,163,74,0.92)" : "rgba(107,114,128,0.92)", "important");
    }

    function getSavedButtonPos() {
        try {
            var raw = window.localStorage.getItem(TOGGLE_BTN_POS_KEY);
            if (!raw) {
                return null;
            }
            var pos = JSON.parse(raw);
            if (typeof pos.x !== "number" || typeof pos.y !== "number") {
                return null;
            }
            return pos;
        } catch (e) {
            return null;
        }
    }

    function saveButtonPos(x, y) {
        try {
            window.localStorage.setItem(TOGGLE_BTN_POS_KEY, JSON.stringify({ x: x, y: y }));
        } catch (e) {
            // ignore
        }
    }

    function applyButtonPos(btn, x, y) {
        var maxX = Math.max(0, window.innerWidth - btn.offsetWidth);
        var maxY = Math.max(0, window.innerHeight - btn.offsetHeight);
        var nextX = Math.min(Math.max(0, x), maxX);
        var nextY = Math.min(Math.max(0, y), maxY);
        btn.style.setProperty("left", nextX + "px", "important");
        btn.style.setProperty("top", nextY + "px", "important");
        btn.style.removeProperty("right");
        btn.style.removeProperty("bottom");
    }

    function ensureToggleButton() {
        var t = now();
        if (t - lastToggleEnsureAt < TOGGLE_ENSURE_INTERVAL_MS) {
            return;
        }
        lastToggleEnsureAt = t;
        installToggleButton();
    }

    // 安装页面开关按钮（默认开启，可手动关闭/重开）。
    function installToggleButton() {
        if (!document || !document.body) {
            return;
        }
        if (document.getElementById(TOGGLE_BTN_ID)) {
            updateToggleButtonText();
            return;
        }
        var btn = document.createElement("button");
        btn.id = TOGGLE_BTN_ID;
        btn.type = "button";
        btn.style.setProperty("position", "fixed", "important");
        btn.style.setProperty("top", "16px", "important");
        btn.style.setProperty("left", Math.max(16, window.innerWidth - 120) + "px", "important");
        btn.style.setProperty("z-index", "2147483647", "important");
        btn.style.setProperty("padding", "6px 10px", "important");
        btn.style.setProperty("border", "0", "important");
        btn.style.setProperty("border-radius", "6px", "important");
        btn.style.setProperty("color", "#fff", "important");
        btn.style.setProperty("font-size", "12px", "important");
        btn.style.setProperty("line-height", "1.2", "important");
        btn.style.setProperty("cursor", "pointer", "important");
        btn.style.setProperty("user-select", "none", "important");
        btn.style.setProperty("touch-action", "none", "important");
        btn.style.setProperty("box-shadow", "0 2px 8px rgba(0,0,0,0.25)", "important");
        btn.style.setProperty("opacity", "0.85", "important");
        var savedPos = getSavedButtonPos();
        if (savedPos) {
            // 若用户拖动过，优先还原到用户上次保存的位置。
            applyButtonPos(btn, savedPos.x, savedPos.y);
        }

        var dragging = false;
        var moved = false;
        var dragStartX = 0;
        var dragStartY = 0;
        var btnStartLeft = 0;
        var btnStartTop = 0;
        var onPointerMove = function (e) {
            if (!dragging) {
                return;
            }
            var dx = e.clientX - dragStartX;
            var dy = e.clientY - dragStartY;
            if (!moved && (Math.abs(dx) >= TOGGLE_BTN_DRAG_MIN_PX || Math.abs(dy) >= TOGGLE_BTN_DRAG_MIN_PX)) {
                moved = true;
            }
            if (!moved) {
                return;
            }
            applyButtonPos(btn, btnStartLeft + dx, btnStartTop + dy);
        };
        var stopDrag = function () {
            if (!dragging) {
                return;
            }
            dragging = false;
            if (moved) {
                saveButtonPos(btn.offsetLeft, btn.offsetTop);
            }
            window.removeEventListener("pointermove", onPointerMove, true);
            window.removeEventListener("pointerup", stopDrag, true);
            window.removeEventListener("pointercancel", stopDrag, true);
        };
        btn.addEventListener("pointerdown", function (e) {
            if (e.button !== 0) {
                return;
            }
            dragging = true;
            moved = false;
            dragStartX = e.clientX;
            dragStartY = e.clientY;
            btnStartLeft = btn.offsetLeft;
            btnStartTop = btn.offsetTop;
            window.addEventListener("pointermove", onPointerMove, true);
            window.addEventListener("pointerup", stopDrag, true);
            window.addEventListener("pointercancel", stopDrag, true);
        }, true);
        btn.addEventListener("mouseenter", function () {
            btn.style.setProperty("opacity", "1", "important");
        });
        btn.addEventListener("mouseleave", function () {
            btn.style.setProperty("opacity", "0.85", "important");
        });
        btn.addEventListener("click", function (e) {
            if (moved) {
                moved = false;
                return;
            }
            e.preventDefault();
            e.stopPropagation();
            var next = !isPluginEnabled();
            setPluginEnabled(next);
            if (!next) {
                restoreNormalPlayback();
            } else {
                runOnce();
            }
        }, true);
        window.addEventListener("resize", function () {
            applyButtonPos(btn, btn.offsetLeft, btn.offsetTop);
        });
        document.body.appendChild(btn);
        updateToggleButtonText();
    }

    // 记录一次“广告线索命中”时间，用于短期状态判断。
    function markAdSignal() {
        lastAdSignalAt = now();
    }

    // 判断最近是否存在广告线索（网络命中/跳过按钮点击/弹窗拦截等）。
    function hasRecentAdSignal() {
        return now() - lastAdSignalAt <= AD_SIGNAL_TTL_MS;
    }

    // 当前页面是否属于爱奇艺域名。
    function isIqiyi() {
        return (window.location.hostname || "").indexOf("iqiyi.com") >= 0;
    }

    // 当前是否视频详情/播放页。只在该类页面启动脚本，其他页面完全休眠。
    function isDetailPage() {
        var path = window.location.pathname || "";
        var href = window.location.href || "";
        return path.indexOf("/v_") === 0 || path.indexOf("/w_") === 0 || path.indexOf("/a_") === 0 || path.indexOf("/play") === 0 || href.indexOf("/play/") >= 0;
    }

    function isActiveWatchingPage() {
        // 仅当前可见且聚焦的标签页运行插件逻辑
        return document.visibilityState === "visible" && document.hasFocus();
    }

    // 根据 URL 判断是否广告请求。
    // 策略：先限定 iqiyi/qiyi 域，再匹配保守关键词，减少误伤主业务请求。
    function isAdUrl(url) {
        if (!url) {
            return false;
        }
        var lower = String(url).toLowerCase();
        if (lower.indexOf("iqiyi.com") < 0 && lower.indexOf("qiyi.com") < 0) {
            return false;
        }
        for (var i = 0; i < AD_URL_PATTERNS.length; i++) {
            if (lower.indexOf(AD_URL_PATTERNS[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    // 网络层拦截：仅拦截明确广告请求
    function installRequestBlocker() {
        var rawFetch = window.fetch;
        if (typeof rawFetch === "function" && !rawFetch.__iqiyiAdWrapped) {
            window.fetch = function (input, init) {
                if (!isPluginEnabled()) {
                    return rawFetch.call(window, input, init);
                }
                var url = "";
                if (typeof input === "string") {
                    url = input;
                } else if (input && typeof input.url === "string") {
                    url = input.url;
                }
                if (isAdUrl(url)) {
                    markAdSignal();
                    return Promise.resolve(new Response("{}", { status: 200, headers: { "Content-Type": "application/json" } }));
                }
                return rawFetch.call(window, input, init);
            };
            window.fetch.__iqiyiAdWrapped = true;
        }

        var rawOpen = XMLHttpRequest.prototype.open;
        var rawSend = XMLHttpRequest.prototype.send;
        if (!rawOpen.__iqiyiAdWrapped) {
            XMLHttpRequest.prototype.open = function (method, url) {
                this.__reqUrl = typeof url === "string" ? url : "";
                return rawOpen.apply(this, arguments);
            };
            XMLHttpRequest.prototype.open.__iqiyiAdWrapped = true;
        }
        if (!rawSend.__iqiyiAdWrapped) {
            XMLHttpRequest.prototype.send = function () {
                if (!isPluginEnabled()) {
                    return rawSend.apply(this, arguments);
                }
                if (isAdUrl(this.__reqUrl)) {
                    markAdSignal();
                    try {
                        this.abort();
                    } catch (e) {
                        // ignore
                    }
                    return;
                }
                return rawSend.apply(this, arguments);
            };
            XMLHttpRequest.prototype.send.__iqiyiAdWrapped = true;
        }
    }

    // 贴片广告线索探测：
    // 1) 从安全倒计时节点识别“可跳过广告/纯数字倒计时”
    // 2) 无明显节点时回退到“近期广告信号”
    function hasPrerollHint() {
        var nodes = document.querySelectorAll(SAFE_SKIP_SELECTORS);
        var limit = Math.min(nodes.length, 8);
        for (var i = 0; i < limit; i++) {
            var node = nodes[i];
            if (!node || node.offsetParent === null) {
                continue;
            }
            var txt = typeof node.textContent === "string" ? node.textContent.trim() : "";
            if (!txt) {
                continue;
            }
            if (containsAnyText(txt, SKIP_HINT_TEXT_LIST) || /^[0-9]{1,3}$/.test(txt)) {
                return true;
            }
        }
        return hasRecentAdSignal();
    }

    // 新开页防护：限制广告链路弹窗与异常拉页
    function installWindowOpenGuard() {
        var rawOpen = window.open;
        if (typeof rawOpen !== "function" || rawOpen.__iqiyiAdWrapped) {
            return;
        }
        window.open = function (url) {
            if (!isPluginEnabled()) {
                return rawOpen.apply(window, arguments);
            }
            var t = now();
            recentOpenAtList = recentOpenAtList.filter(function (x) {
                return t - x <= 5000;
            });
            recentOpenAtList.push(t);
            if (recentOpenAtList.length > 2) {
                markAdSignal();
                return null;
            }
            if (isAdUrl(url)) {
                markAdSignal();
                return null;
            }
            if (isActiveWatchingPage() && hasPrerollHint() && t - lastUserActionAt > 1500) {
                markAdSignal();
                return null;
            }
            return rawOpen.apply(window, arguments);
        };
        window.open.__iqiyiAdWrapped = true;
    }

    // 主播放器识别：选择页面面积最大的可见 video
    function getMainVideo() {
        if (cachedMainVideo && now() - cachedMainVideoAt < MAIN_VIDEO_CACHE_TTL_MS) {
            try {
                if (!cachedMainVideo.isConnected || cachedMainVideo.readyState < 0) {
                    cachedMainVideo = null;
                } else {
                    var cachedRect = cachedMainVideo.getBoundingClientRect();
                    var cachedArea = Math.max(0, cachedRect.width) * Math.max(0, cachedRect.height);
                    if (cachedRect.width > 0 && cachedRect.height > 0 && cachedArea >= MIN_MAIN_VIDEO_AREA) {
                        return cachedMainVideo;
                    }
                    cachedMainVideo = null;
                }
            } catch (e) {
                cachedMainVideo = null;
            }
        }
        var videos = document.querySelectorAll("video");
        var best = null;
        var bestArea = 0;
        for (var i = 0; i < videos.length; i++) {
            var v = videos[i];
            if (!v || !v.getBoundingClientRect) {
                continue;
            }
            var rect = v.getBoundingClientRect();
            var area = Math.max(0, rect.width) * Math.max(0, rect.height);
            if (rect.width > 0 && rect.height > 0 && area > bestArea) {
                best = v;
                bestArea = area;
            }
        }
        if (bestArea < MIN_MAIN_VIDEO_AREA) {
            cachedMainVideo = null;
            return null;
        }
        cachedMainVideo = best;
        cachedMainVideoAt = now();
        return best;
    }

    // 从倒计时文本中抽取秒数（例如 "28s后可跳过广告" -> 28，"28" -> 28）。
    // 无法解析返回 -1。
    function parseCdSeconds(text) {
        if (!text) {
            return -1;
        }
        var m = String(text).match(/[0-9]{1,3}/);
        if (!m) {
            return -1;
        }
        return parseInt(m[0], 10);
    }

    // 判断文本是否包含任意关键词（基于列表配置，便于后续维护）。
    function containsAnyText(text, keywords) {
        if (!text || !keywords || !keywords.length) {
            return false;
        }
        for (var i = 0; i < keywords.length; i++) {
            if (String(text).indexOf(keywords[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    // 判断 className 是否命中任意安全类名（基于列表配置，便于后续维护）。
    function containsAnyClassName(className, classList) {
        if (!className || !classList || !classList.length) {
            return false;
        }
        var lower = String(className).toLowerCase();
        for (var i = 0; i < classList.length; i++) {
            if (lower.indexOf(classList[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    // 多开页防护（点击前校验）：
    // 若节点在可导航链接中，或自身/祖先存在明显跳转属性，则拒绝自动点击。
    function isNavigationRiskNode(node) {
        if (!node || !node.closest) {
            return false;
        }
        var link = node.closest("a[href], [data-href], [data-url]");
        if (link) {
            var href = (link.getAttribute("href") || link.getAttribute("data-href") || link.getAttribute("data-url") || "").trim();
            var hrefLower = href.toLowerCase();
            if (href && hrefLower !== "#" && hrefLower.indexOf("javascript:void") !== 0) {
                return true;
            }
        }
        var cur = node;
        var depth = 0;
        while (cur && depth < 5) {
            var onclickText = "";
            try {
                onclickText = cur.getAttribute ? (cur.getAttribute("onclick") || "") : "";
            } catch (e) {
                onclickText = "";
            }
            var onclickLower = String(onclickText).toLowerCase();
            if (onclickLower.indexOf("window.open") >= 0 || onclickLower.indexOf("location.href") >= 0 || onclickLower.indexOf("location.assign") >= 0) {
                return true;
            }
            cur = cur.parentElement;
            depth++;
        }
        return false;
    }

    // 读取安全跳过节点（统一入口），避免同一轮内重复 DOM 查询。
    function getSafeSkipNodes(limit) {
        var nodes = document.querySelectorAll(SAFE_SKIP_SELECTORS);
        if (!limit || nodes.length <= limit) {
            return nodes;
        }
        var arr = [];
        for (var i = 0; i < limit; i++) {
            arr.push(nodes[i]);
        }
        return arr;
    }

    // 仅点击安全跳过节点，避免误点广告落地入口
    function tryClickSafeSkipNode(nodes) {
        var list = nodes || getSafeSkipNodes(12);
        var limit = Math.min(list.length, 12);
        for (var i = 0; i < limit; i++) {
            var node = list[i];
            if (!node || node.offsetParent === null) {
                continue;
            }
            var text = typeof node.textContent === "string" ? node.textContent.trim() : "";
            var cls = typeof node.className === "string" ? node.className : "";
            var isSafeClassNode = containsAnyClassName(cls, SAFE_SKIP_CLASS_LIST);
            var hasLanding = containsAnyText(text, LANDING_BLOCK_TEXT_LIST);
            // 严格限制：只允许点击安全类名节点，并且遇到落地文案一律不点。
            if (!isSafeClassNode || hasLanding) {
                continue;
            }
            // 严格限制：疑似导航节点（可能触发多开页）一律不点。
            if (isNavigationRiskNode(node)) {
                continue;
            }
            var sec = parseCdSeconds(text);
            var shouldClick = containsAnyText(text, SKIP_HINT_TEXT_LIST) || (sec >= 0 && sec <= 3);
            if (!shouldClick) {
                continue;
            }
            try {
                node.removeAttribute("disabled");
                node.setAttribute("aria-disabled", "false");
                node.style.setProperty("pointer-events", "auto", "important");
                node.style.setProperty("z-index", "2147483647", "important");
                node.dispatchEvent(new MouseEvent("mousedown", { bubbles: true, cancelable: true }));
                node.dispatchEvent(new MouseEvent("mouseup", { bubbles: true, cancelable: true }));
                node.dispatchEvent(new MouseEvent("click", { bubbles: true, cancelable: true }));
                if (typeof node.click === "function") {
                    node.click();
                }
                markAdSignal();
                return true;
            } catch (e) {
                // ignore
            }
        }
        return false;
    }

    // 广告态温和提速：优先保证稳定性
    function boostAdVideoSafely(video) {
        if (!video) {
            return;
        }
        try {
            video.muted = true;
            if (video.paused && !video.ended) {
                var p = video.play();
                if (p && typeof p.catch === "function") {
                    p.catch(function () {
                        // ignore
                    });
                }
            }
            if (video.playbackRate < 8) {
                video.playbackRate = 8;
            }
            if (video.defaultPlaybackRate < 8) {
                video.defaultPlaybackRate = 8;
            }
        } catch (e) {
            // ignore
        }
    }

    // 从广告态恢复到正片态：把播放速率恢复为 1x。
    function restoreNormalPlayback() {
        var v = getMainVideo();
        if (!v) {
            return;
        }
        try {
            if (v.playbackRate !== 1) {
                v.playbackRate = 1;
            }
        } catch (e) {
            // ignore
        }
    }

    // 单轮执行：广告态处理 / 正片恢复
    function runOnce() {
        if (!isPluginEnabled()) {
            restoreNormalPlayback();
            return { active: false, video: null };
        }
        if (!isActiveWatchingPage()) {
            return { active: false, video: null };
        }
        var v = getMainVideo();
        if (!v) {
            return { active: false, video: null };
        }
        var skipNodes = getSafeSkipNodes(12);
        var adLike = false;
        var hintLimit = Math.min(skipNodes.length, 8);
        for (var i = 0; i < hintLimit; i++) {
            var n = skipNodes[i];
            if (!n || n.offsetParent === null) {
                continue;
            }
            var txt = typeof n.textContent === "string" ? n.textContent.trim() : "";
            if (!txt) {
                continue;
            }
            if (containsAnyText(txt, SKIP_HINT_TEXT_LIST) || /^[0-9]{1,3}$/.test(txt)) {
                adLike = true;
                break;
            }
        }
        if (!adLike) {
            adLike = hasRecentAdSignal();
        }
        if (!adLike) {
            restoreNormalPlayback();
            return { active: false, video: v };
        }
        boostAdVideoSafely(v);
        tryClickSafeSkipNode(skipNodes);
        return { active: true, video: v };
    }

    // 计算下一轮调度间隔（核心节流策略）：
    // - 插件关闭：60s（仅保留最低心跳）
    // - 后台标签页：18s
    // - 广告态：120ms
    // - 非广告态前期/可疑状态：700ms
    // - 正片稳定后：3s -> 10s
    function getNextDelay(result) {
        if (!isPluginEnabled()) {
            stablePlaybackSince = 0;
            return LOOP_DISABLED_MS;
        }
        if (!isActiveWatchingPage()) {
            stablePlaybackSince = 0;
            return LOOP_BACKGROUND_MS;
        }
        if (!result || result.active) {
            stablePlaybackSince = 0;
            return LOOP_ACTIVE_MS;
        }
        var v = result.video;
        if (!v) {
            stablePlaybackSince = 0;
            return LOOP_IDLE_MS;
        }
        // 兼顾不退化：前 2 分钟按 v37 频次跑，确保贴片场景不漏
        if (!isNaN(v.currentTime) && v.currentTime < 120) {
            stablePlaybackSince = 0;
            return LOOP_IDLE_MS;
        }
        if (v.paused || v.ended || hasRecentAdSignal()) {
            stablePlaybackSince = 0;
            return LOOP_IDLE_MS;
        }
        if (!stablePlaybackSince) {
            stablePlaybackSince = now();
            return 3300;
        }
        if (now() - stablePlaybackSince >= 20000) {
            return LOOP_IDLE_STABLE_MS;
        }
        return 3300;
    }

    // 注册轻量交互监听：
    // 用户点击/按键/触摸/窗口聚焦时，触发一次即时检测，提升体感响应速度。
    function installInteractionListeners() {
        var onAction = function () {
            ensureToggleButton();
            if (!isPluginEnabled()) {
                return;
            }
            if (!isActiveWatchingPage()) {
                return;
            }
            lastUserActionAt = now();
            if (lastUserActionAt - lastActionRunAt < ACTION_RUN_MIN_INTERVAL_MS) {
                return;
            }
            lastActionRunAt = lastUserActionAt;
            runOnce();
        };
        document.addEventListener("click", onAction, true);
        document.addEventListener("keydown", onAction, true);
        document.addEventListener("touchstart", onAction, { passive: true });
        window.addEventListener("focus", onAction, true);
    }

    // 主循环：根据状态动态调整扫描频率
    function startLoop() {
        function loop() {
            ensureToggleButton();
            var result = runOnce();
            window.setTimeout(loop, getNextDelay(result));
        }
        loop();
    }

    if (!isIqiyi()) {
        return;
    }
    // 仅视频详情页启用，其他页面完全不处理
    if (!isDetailPage()) {
        return;
    }
    installRequestBlocker();
    installWindowOpenGuard();
    installInteractionListeners();
    if (document.documentElement) {
        if (document.body) {
            installToggleButton();
        } else {
            document.addEventListener("DOMContentLoaded", installToggleButton, { once: true });
        }
        startLoop();
    } else {
        document.addEventListener("DOMContentLoaded", function () {
            installToggleButton();
            startLoop();
        }, { once: true });
    }
})();
