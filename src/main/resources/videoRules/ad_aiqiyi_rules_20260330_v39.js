// ==UserScript==
// @name         ad_aiqiyi_rules_20260330_v39
// @namespace    http://tampermonkey.net/
// @version      2.5.1
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
    var LOOP_IDLE_MS = 700;
    // 广告态轮询间隔（毫秒）：检测到广告线索后使用高频扫描。
    var LOOP_ACTIVE_MS = 120;
    // 后台/未聚焦标签页轮询间隔（毫秒）：页面不可见或失焦时大幅降频。
    var LOOP_BACKGROUND_MS = 12000;
    // 广告信号有效期（毫秒）：短期内用于判断是否仍可能处于广告相关阶段。
    var AD_SIGNAL_TTL_MS = 1600;
    // 主视频最小面积阈值（像素）：过滤小窗/装饰视频，避免误操作非主播放器。
    var MIN_MAIN_VIDEO_AREA = 60000;
    // 最近一次广告线索时间（毫秒时间戳）。
    var lastAdSignalAt = 0;
    // 最近一次用户主动交互时间（点击/按键/触摸等）。
    var lastUserActionAt = 0;
    // 近 5 秒 window.open 时间列表，用于识别异常连续弹窗。
    var recentOpenAtList = [];

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

    // 返回当前时间戳（毫秒）。
    function now() {
        return Date.now();
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

    // 当前是否“正在观看”的标签页（可见且聚焦）。
    function isActiveWatchingPage() {
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
            if (txt.indexOf("可跳过广告") >= 0 || txt.indexOf("后可跳过广告") >= 0 || /^[0-9]{1,3}$/.test(txt)) {
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
            if (hasPrerollHint() && t - lastUserActionAt > 1500) {
                markAdSignal();
                return null;
            }
            return rawOpen.apply(window, arguments);
        };
        window.open.__iqiyiAdWrapped = true;
    }

    // 主播放器识别：选择页面面积最大的可见 video
    function getMainVideo() {
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
            return null;
        }
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

    // 仅点击安全跳过节点，避免误点广告落地入口
    function tryClickSafeSkipNode() {
        var nodes = document.querySelectorAll(SAFE_SKIP_SELECTORS);
        var limit = Math.min(nodes.length, 12);
        for (var i = 0; i < limit; i++) {
            var node = nodes[i];
            if (!node || node.offsetParent === null) {
                continue;
            }
            var text = typeof node.textContent === "string" ? node.textContent.trim() : "";
            var cls = typeof node.className === "string" ? node.className.toLowerCase() : "";
            var hasLanding = text.indexOf("查看详情") >= 0 || text.indexOf("开通会员") >= 0 || text.indexOf("立即开通") >= 0;
            // 只点击纯 cd-time/public-time 跳过节点
            if (hasLanding && cls.indexOf("cd-time") < 0 && cls.indexOf("public-time") < 0) {
                continue;
            }
            var sec = parseCdSeconds(text);
            var shouldClick = text.indexOf("跳过广告") >= 0 || text.indexOf("可跳过广告") >= 0 || (sec >= 0 && sec <= 3);
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
        if (!isActiveWatchingPage()) {
            return false;
        }
        var v = getMainVideo();
        if (!v) {
            return false;
        }
        var adLike = hasPrerollHint();
        if (!adLike) {
            restoreNormalPlayback();
            return false;
        }
        boostAdVideoSafely(v);
        tryClickSafeSkipNode();
        return true;
    }

    // 注册轻量交互监听：
    // 用户点击/按键/触摸/窗口聚焦时，触发一次即时检测，提升体感响应速度。
    function installInteractionListeners() {
        var onAction = function () {
            if (!isActiveWatchingPage()) {
                return;
            }
            lastUserActionAt = now();
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
            if (!isActiveWatchingPage()) {
                window.setTimeout(loop, LOOP_BACKGROUND_MS);
                return;
            }
            var active = runOnce();
            window.setTimeout(loop, active ? LOOP_ACTIVE_MS : LOOP_IDLE_MS);
        }
        loop();
    }

    if (!isIqiyi()) {
        return;
    }
    if (!isDetailPage()) {
        return;
    }
    installRequestBlocker();
    installWindowOpenGuard();
    installInteractionListeners();
    if (document.documentElement) {
        startLoop();
    } else {
        document.addEventListener("DOMContentLoaded", startLoop, { once: true });
    }
})();
