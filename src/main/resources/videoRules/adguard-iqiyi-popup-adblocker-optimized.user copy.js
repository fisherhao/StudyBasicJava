// ==UserScript==
// @name         AdGuard - 余浩 仅爱奇艺弹窗/视频广告拦截优化版
// @namespace    adguard
// @version      3.0.0
// @description  仅爱奇艺：不引发跳转、多次重放控制、尽量限制片头广告可见时间<=2s
// @author       yuHao
// @license      LGPL-3.0
// @match        https://www.iqiyi.com/*
// @match        https://*.iqiyi.com/*
// @grant        GM_getValue
// @grant        GM_setValue
// @grant        GM_deleteValue
// @grant        GM_listValues
// @run-at       document-start
// ==/UserScript==

(function () {
    "use strict";

    // -------------------- 基础配置区 --------------------
    // 允许域：命中后不拦截（通过 localStorage/GM 存储维护）
    var STORAGE_KEY_ALLOWED = "allowed";
    var STORAGE_KEY_SILENCED = "silenced";
    var STORAGE_KEY_INSTANCE_ID = "instanceId";
    var STORAGE_PREFIX = "pb_opt_";
    // 用户手势窗口：在短时间内认为用户主动触发，减少误拦截
    var LAST_USER_GESTURE_MS = 1200;
    // 提示 Toast 显示时长（静默模式下不显示）
    var SILENCE_TOAST_MS = 2500;
    // 广告清理/识别扫描间隔：越小越激进，但会增加 CPU 占用
    var VIDEO_AD_SCAN_MS = 60;
    // 爱奇艺域名关键字
    var IQIYI_HOST_KEY = "iqiyi.com";
    // 选择“主播放器”视频：可见区域面积阈值（避免列表页/预览误伤）
    var MIN_MAIN_VIDEO_AREA = 120000;
    // 同一轮广告跳尾锁定时长：防止重复 seek 导致“广告重复播放”
    var AD_SKIP_LOCK_MS = 4200;
    // 广告态允许的“剩余可见尾段”最多为 2 秒
    var AD_MAX_VISIBLE_TAIL_SEC = 2.0;

    // -------------------- 运行时状态区 --------------------
    var lastUserGestureAt = 0;
    var blockedCount = 0;
    // 广告跳尾去重缓存：同一资源在一段时间内只执行一次关键 seek
    var adJumpCache = {};
    var adSkipLockUntil = 0;

    // -------------------- 存储能力检测区 --------------------
    // AdGuard 脚本环境可能提供或不提供 GM_*，这里做兼容兜底到 localStorage
    var HAS_GM = typeof GM_getValue === "function" &&
        typeof GM_setValue === "function" &&
        typeof GM_deleteValue === "function" &&
        typeof GM_listValues === "function";

    function now() {
        // 获取当前时间戳（用于手势窗口、锁定判断）
        return Date.now();
    }

    function getDomain(url) {
        // 从任意 url 中解析出域名；解析失败返回空串
        try {
            return new URL(url, window.location.href).hostname || "";
        } catch (e) {
            return "";
        }
    }

    function getCurrentDomain() {
        // 当前页面的 hostname
        return window.location.hostname || "";
    }

    function isIqiyiPage() {
        // 是否为爱奇艺相关域（包含子域名）
        return getCurrentDomain().indexOf(IQIYI_HOST_KEY) >= 0;
    }

    function isIqiyiDetailPage() {
        // 仅在详情页启用（避免列表页预览也被加速/清理）
        if (!isIqiyiPage()) {
            return false;
        }
        var path = window.location.pathname || "";
        return path.indexOf("/v_") === 0 ||
            path.indexOf("/w_") === 0 ||
            path.indexOf("/a_") === 0 ||
            path.indexOf("/play") === 0 ||
            path.indexOf("/vplay") === 0;
    }

    function splitList(value) {
        // 将逗号分隔字符串转为数组，过滤掉空项
        if (!value) {
            return [];
        }
        return String(value)
            .split(",")
            .map(function (item) {
                return item.trim();
            })
            .filter(function (item) {
                return item.length > 0;
            });
    }

    function joinList(list) {
        // 将数组转回逗号分隔字符串
        return list.join(",");
    }

    function getList(key) {
        // 从存储读取字符串列表并解析为数组
        return splitList(getValue(key, ""));
    }

    function setList(key, list) {
        // 将数组列表序列化存储
        setValue(key, joinList(list));
    }

    function addToList(key, value) {
        // 给 key 对应的列表添加元素（去重）
        var list = getList(key);
        if (list.indexOf(value) === -1) {
            list.push(value);
            setList(key, list);
        }
    }

    function isInList(key, value) {
        // 判断元素是否存在于列表中
        return getList(key).indexOf(value) >= 0;
    }

    function ensureInstanceId() {
        // 生成并缓存本脚本实例 ID（用于定位/避免重复状态）
        var instanceId = getValue(STORAGE_KEY_INSTANCE_ID, "");
        if (!instanceId) {
            instanceId = "pb_" + Math.random().toString(36).slice(2) + "_" + now();
            setValue(STORAGE_KEY_INSTANCE_ID, instanceId);
        }
        return instanceId;
    }

    function getStorageKey(key) {
        // 给脚本内部存储做统一前缀，避免与站点其它 localStorage 冲突
        return STORAGE_PREFIX + key;
    }

    function getValue(key, defaultValue) {
        // 优先使用 GM_* 存储；否则用 localStorage 做隔离兜底
        if (HAS_GM) {
            return GM_getValue(key, defaultValue);
        }
        try {
            var raw = window.localStorage.getItem(getStorageKey(key));
            return raw === null ? defaultValue : raw;
        } catch (e) {
            return defaultValue;
        }
    }

    function setValue(key, value) {
        // 设置存储值（兼容 GM_/localStorage）
        if (HAS_GM) {
            GM_setValue(key, value);
            return;
        }
        try {
            window.localStorage.setItem(getStorageKey(key), String(value));
        } catch (e) {
            // ignore
        }
    }

    function deleteValue(key) {
        // 删除存储值（兼容 GM_/localStorage）
        if (HAS_GM) {
            GM_deleteValue(key);
            return;
        }
        try {
            window.localStorage.removeItem(getStorageKey(key));
        } catch (e) {
            // ignore
        }
    }

    function listValues() {
        // 列出脚本自己的存储键（兼容 GM_/localStorage）
        if (HAS_GM) {
            return GM_listValues();
        }
        var keys = [];
        try {
            for (var i = 0; i < window.localStorage.length; i++) {
                var key = window.localStorage.key(i);
                if (key && key.indexOf(STORAGE_PREFIX) === 0) {
                    keys.push(key.substring(STORAGE_PREFIX.length));
                }
            }
        } catch (e) {
            // ignore
        }
        return keys;
    }

    function hasRecentUserGesture() {
        // 判断是否在最近一段时间内发生过用户手势
        return now() - lastUserGestureAt <= LAST_USER_GESTURE_MS;
    }

    function rememberGesture() {
        // 记录最近一次用户手势发生的时间戳
        lastUserGestureAt = now();
    }

    function isAllowedByDomain(targetUrl) {
        // 如果目标域/当前域在“允许列表”中，则不拦截
        var targetDomain = getDomain(targetUrl);
        if (!targetDomain) {
            return false;
        }
        return isInList(STORAGE_KEY_ALLOWED, targetDomain) || isInList(STORAGE_KEY_ALLOWED, getCurrentDomain());
    }

    function isSilenced() {
        // 静默通知：不弹出 Toast
        return isInList(STORAGE_KEY_SILENCED, getCurrentDomain());
    }

    function renderToast(text) {
        // 在页面底部渲染一个短时通知（用于调试/可见性）
        if (isSilenced()) {
            return;
        }
        var host = document.documentElement;
        if (!host) {
            return;
        }

        var wrapper = document.createElement("div");
        wrapper.setAttribute("style",
            "position:fixed;left:50%;bottom:24px;transform:translateX(-50%);z-index:2147483647;" +
            "background:rgba(0,0,0,.78);color:#fff;padding:10px 14px;border-radius:8px;" +
            "font-size:13px;line-height:1.4;font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Arial,sans-serif;" +
            "box-shadow:0 4px 16px rgba(0,0,0,.25);max-width:88vw;word-break:break-word;");
        wrapper.textContent = text;
        host.appendChild(wrapper);
        window.setTimeout(function () {
            if (wrapper && wrapper.parentNode) {
                wrapper.parentNode.removeChild(wrapper);
            }
        }, SILENCE_TOAST_MS);
    }

    function blockPopup(targetUrl, reason) {
        // 统一拦截入口：增加计数并展示原因
        blockedCount += 1;
        var message = "[Popup Blocker] 已拦截弹窗(" + blockedCount + "): " + targetUrl + "，原因: " + reason;
        renderToast(message);
    }

    function shouldBlock(targetUrl) {
        // 核心策略：
        // 1) 允许列表命中 -> 不拦截
        // 2) 非允许域 -> 且最近无用户手势 -> 拦截
        if (!targetUrl) {
            return false;
        }
        if (isAllowedByDomain(targetUrl)) {
            return false;
        }
        return !hasRecentUserGesture();
    }

    function showControlPanelOnce() {
        // 一次性控制面板：给你开关“允许当前域弹窗/静默通知”
        if (isSilenced()) {
            return;
        }
        if (window.top !== window.self) {
            return;
        }
        window.addEventListener("DOMContentLoaded", function () {
            var panel = document.createElement("div");
            panel.setAttribute("style",
                "position:fixed;right:12px;bottom:12px;z-index:2147483647;background:#fff;" +
                "border:1px solid #e8e8e8;border-radius:10px;padding:8px 10px;" +
                "box-shadow:0 6px 22px rgba(0,0,0,.15);font-size:12px;" +
                "font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Arial,sans-serif;color:#222;");

            var allowBtn = document.createElement("button");
            allowBtn.textContent = "允许当前域弹窗";
            allowBtn.setAttribute("style", "margin-right:8px;border:0;background:#67b279;color:#fff;padding:6px 8px;border-radius:6px;cursor:pointer;");
            allowBtn.addEventListener("click", function () {
                addToList(STORAGE_KEY_ALLOWED, getCurrentDomain());
                renderToast("已添加允许域: " + getCurrentDomain());
            });

            var silenceBtn = document.createElement("button");
            silenceBtn.textContent = "静默通知";
            silenceBtn.setAttribute("style", "border:0;background:#666;color:#fff;padding:6px 8px;border-radius:6px;cursor:pointer;");
            silenceBtn.addEventListener("click", function () {
                addToList(STORAGE_KEY_SILENCED, getCurrentDomain());
                renderToast("已静默当前域通知: " + getCurrentDomain());
            });

            panel.appendChild(allowBtn);
            panel.appendChild(silenceBtn);
            document.documentElement.appendChild(panel);
            window.setTimeout(function () {
                if (panel && panel.parentNode) {
                    panel.parentNode.removeChild(panel);
                }
            }, 10000);
        });
    }

    function wrapWindowOpen() {
        // 拦截 window.open：避免弹出广告页面/无限新标签页
        var originalOpen = window.open;
        if (typeof originalOpen !== "function") {
            return;
        }
        window.open = function (url, name, features) {
            var targetUrl = String(url || "");
            if (isIqiyiPage() && isLikelyVideoAdUrl(targetUrl)) {
                // iQiyi 相关：疑似视频广告请求/链接直接阻断
                blockPopup(targetUrl, "广告新标签拦截");
                return null;
            }
            if (shouldBlock(targetUrl)) {
                // 非手势触发弹窗：拦截
                blockPopup(targetUrl, "无用户手势");
                return null;
            }
            return originalOpen.call(window, url, name, features);
        };
    }

    function interceptAnchorClick() {
        // 拦截点击 <a href="...">：对于疑似广告/弹窗链接做预防性拦截
        document.addEventListener("click", function (event) {
            var node = event.target;
            while (node && node !== document.documentElement) {
                if (node.tagName === "A" && node.href) {
                    var href = node.href;
                    if (shouldBlock(href)) {
                        event.preventDefault();
                        event.stopPropagation();
                        blockPopup(href, "可疑链接点击");
                    }
                    return;
                }
                node = node.parentElement;
            }
        }, true);
    }

    function listenGestureEvents() {
        // 监听用户输入事件，用于降低误拦截风险
        document.addEventListener("mousedown", rememberGesture, true);
        document.addEventListener("touchstart", rememberGesture, true);
        document.addEventListener("keydown", rememberGesture, true);
    }

    function cleanupBrokenStorageKeys() {
        // 清理可能残留的临时存储键（用于避免旧状态干扰）
        var keys = listValues();
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            if (key && key.indexOf("popup-blocker-temp-") === 0) {
                deleteValue(key);
            }
        }
    }

    function isLikelyVideoAdUrl(url) {
        // 对疑似“暂停广告/广告代理/广告请求路径”做快速匹配
        if (!url) {
            return false;
        }
        var lower = String(url).toLowerCase();
        if (lower.indexOf("iqiyi.com") >= 0 || lower.indexOf("qiyi.com") >= 0) {
            return lower.indexOf("cupid") >= 0 ||
                lower.indexOf("cupidv3") >= 0 ||
                lower.indexOf("adproxy") >= 0 ||
                lower.indexOf("ad_proxy") >= 0 ||
                lower.indexOf("adsclient") >= 0 ||
                lower.indexOf("pausead") >= 0 ||
                lower.indexOf("pause_ad") >= 0 ||
                lower.indexOf("/ads/") >= 0 ||
                lower.indexOf("/ad/") >= 0 ||
                lower.indexOf("/v_ad") >= 0 ||
                lower.indexOf("adslots") >= 0 ||
                lower.indexOf("adid=") >= 0 ||
                lower.indexOf("ad_type=") >= 0;
        }
        return false;
    }

    function installFetchAndXhrAdBlocker() {
        // 通过覆写 fetch / XMLHttpRequest 直接拦截广告请求（尽量不让页面走到渲染逻辑）
        var originFetch = window.fetch;
        if (typeof originFetch === "function") {
            window.fetch = function (input, init) {
                var url = "";
                if (typeof input === "string") {
                    url = input;
                } else if (input && typeof input.url === "string") {
                    url = input.url;
                }
                if (isLikelyVideoAdUrl(url)) {
                    // 返回一个空 JSON 响应，避免页面报错并达到“广告请求不落地”
                    blockPopup(url, "视频广告请求拦截(fetch)");
                    return Promise.resolve(new Response("{}", {
                        status: 200,
                        headers: { "Content-Type": "application/json" }
                    }));
                }
                return originFetch.call(window, input, init);
            };
        }

        var originOpen = XMLHttpRequest.prototype.open;
        var originSend = XMLHttpRequest.prototype.send;
        XMLHttpRequest.prototype.open = function (method, url) {
            // 把 url 缓存在 XHR 实例上，便于 send 阶段判断
            this.__pbUrl = url;
            return originOpen.apply(this, arguments);
        };
        XMLHttpRequest.prototype.send = function () {
            if (isLikelyVideoAdUrl(this.__pbUrl)) {
                // 拦截并终止广告请求
                blockPopup(this.__pbUrl, "视频广告请求拦截(xhr)");
                try {
                    this.abort();
                } catch (e) {
                    // ignore
                }
                return;
            }
            return originSend.apply(this, arguments);
        };
    }

    function removeVideoAdNodesOnce() {
        // DOM 清理：隐藏/抑制常见“暂停广告/广告层/倒计时”等元素的可见性
        var selectors = [
            "[class*='cupid']",
            "[id*='cupid']",
            "[class*='ad-']",
            "[class*='ad_']",
            "[id^='ad_']",
            "[class*='pause-ad']",
            "[class*='pause_ad']",
            "[class*='pauseAd']",
            "[id*='pause_ad']",
            "[id*='pause-ad']",
            ".iqp-player-pause-ad",
            ".iqp-pause-ad",
            ".iqp-ad-layer",
            ".iqp-ad-wrap",
            ".iqp-ad-container",
            ".qy-player-pause-ad",
            "[class*='videoAd']",
            "[class*='player-ad']",
            "[class*='ad-countdown']"
        ];
        for (var i = 0; i < selectors.length; i++) {
            var list = document.querySelectorAll(selectors[i]);
            for (var j = 0; j < list.length; j++) {
                var el = list[j];
                if (el && el.style) {
                    // 只做可见性/交互层面的隐藏，避免 display:none 带来的布局抖动
                    el.style.setProperty("visibility", "hidden", "important");
                    el.style.setProperty("opacity", "0", "important");
                    el.style.setProperty("pointer-events", "none", "important");
                }
            }
        }
    }

    function shouldTreatAsIqiyiAdState() {
        // 通过页面文本特征判断当前是否处在“广告态”
        var text = "";
        if (document.body && document.body.innerText) {
            text = document.body.innerText;
        }
        return text.indexOf("精彩即将开始") >= 0 ||
            text.indexOf("跳过广告") >= 0 ||
            text.indexOf("关闭广告") >= 0 ||
            text.indexOf("VIP可关闭广告") >= 0;
    }

    function clickSkipButtonsIfExists() {
        // 扫描页面里“疑似跳过/关闭广告”的按钮/容器并尝试点击
        var primary = getPrimaryVideoElement();
        if (primary && primary.paused) {
            return;
        }
        var nodes = document.querySelectorAll("button,span,div");
        for (var i = 0; i < nodes.length; i++) {
            var node = nodes[i];
            var txt = "";
            if (node && typeof node.innerText === "string") {
                txt = node.innerText.trim();
            }
            if (!txt) {
                continue;
            }
            var className = (node.className || "").toString().toLowerCase();
            var idName = (node.id || "").toString().toLowerCase();
            var likelySkipNode = className.indexOf("skip") >= 0 ||
                className.indexOf("close") >= 0 ||
                className.indexOf("ad") >= 0 ||
                idName.indexOf("skip") >= 0 ||
                idName.indexOf("close") >= 0 ||
                idName.indexOf("ad") >= 0;
            if (!likelySkipNode) {
                continue;
            }
            if (txt === "跳过广告" || txt === "关闭广告" || txt.indexOf("关闭广告") >= 0) {
                try {
                    // 小心：这里只对按钮/容器点击，不做导航类的强制跳转
                    node.click();
                } catch (e) {
                    // ignore
                }
            }
        }
    }

    function trySkipPrerollByVideoState() {
        // 视频态跳尾：当检测到“广告态文本”或“父容器广告文案”时，静音+倍速并 seek 到广告尾段
        var video = getPrimaryVideoElement();
        if (!video) {
            return;
        }
        if (video.paused) {
            return;
        }
        var adState = shouldTreatAsIqiyiAdState();
        var parentText = "";
        if (video.parentElement && video.parentElement.innerText) {
            parentText = video.parentElement.innerText;
        }
        var maybeAd = adState ||
            parentText.indexOf("VIP可关闭广告") >= 0 ||
            parentText.indexOf("精彩即将开始") >= 0;
        if (maybeAd) {
            var nowTs = now();
            // 重复触发保护：同一个广告段在锁定期内不再重复执行 seek
            if (nowTs < adSkipLockUntil) {
                return;
            }
            try {
                video.muted = true;
                if (video.playbackRate < 16) {
                    video.playbackRate = 16;
                }
                if (!isNaN(video.duration) && video.duration > 1 && video.duration <= 180) {
                    // 使用 key 做去重：避免 DOM 更新导致的重复 seek
                    var adKey = buildAdKey(video);
                    if (!adJumpCache[adKey]) {
                        adJumpCache[adKey] = true;
                    }
                    // 把 currentTime 推到“只保留很短可见尾段”的位置附近
                    var tailTime = Math.max(0, video.duration - AD_MAX_VISIBLE_TAIL_SEC);
                    if (video.currentTime < tailTime) {
                        video.currentTime = tailTime;
                    }
                    // 写入锁定时间，避免接下来轮询再次触发
                    adSkipLockUntil = nowTs + AD_SKIP_LOCK_MS;
                }
            } catch (e) {
                // ignore
            }
        } else {
            try {
                if (video.playbackRate > 1.25) {
                    // 非广告态：尽快恢复到接近正常速度，降低“偶发正片被加速”的概率
                    video.playbackRate = 1;
                }
            } catch (e) {
                // ignore
            }
        }
    }

    function getPrimaryVideoElement() {
        // 选择“可见且面积最大的 video”：尽量避免列表/预览卡片误伤
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
            var visible = rect.width > 0 && rect.height > 0 && rect.bottom > 0 && rect.right > 0;
            if (!visible) {
                continue;
            }
            if (area > bestArea) {
                bestArea = area;
                best = v;
            }
        }
        if (bestArea < MIN_MAIN_VIDEO_AREA) {
            return null;
        }
        return best;
    }

    function buildAdKey(video) {
        // 广告去重 key：基于视频资源地址（去掉 query）+ duration 粗略值拼接
        var src = "";
        try {
            src = (video.currentSrc || video.src || "").split("?")[0];
        } catch (e) {
            src = "";
        }
        var d = 0;
        if (!isNaN(video.duration) && video.duration > 0) {
            d = Math.round(video.duration * 10) / 10;
        }
        return src + "::" + d;
    }

    function installVideoAdCleaner() {
        // 启用视频广告处理：轮询 + MutationObserver 联合，尽量覆盖动态渲染场景
        removeVideoAdNodesOnce();
        window.setInterval(function () {
            removeVideoAdNodesOnce();
            clickSkipButtonsIfExists();
            trySkipPrerollByVideoState();
        }, VIDEO_AD_SCAN_MS);

        var observer = new MutationObserver(function () {
            removeVideoAdNodesOnce();
            clickSkipButtonsIfExists();
        });
        if (document.documentElement) {
            observer.observe(document.documentElement, {
                childList: true,
                subtree: true
            });
        }
    }

    function init() {
        // 初始化顺序（重要）：
        // 1) 生成实例 ID/清理脏键（避免状态遗留）
        // 2) 监听手势并安装拦截器（避免误拦截）
        // 3) 仅在爱奇艺详情页启用 fetch/xhr 拦截与视频广告清理
        ensureInstanceId();
        cleanupBrokenStorageKeys();
        listenGestureEvents();
        wrapWindowOpen();
        interceptAnchorClick();
        if (isIqiyiDetailPage()) {
            installFetchAndXhrAdBlocker();
            installVideoAdCleaner();
        }
        showControlPanelOnce();
    }

    init();
})();

