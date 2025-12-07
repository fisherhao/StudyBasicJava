package com.hao.yu.utils;

import com.alibaba.fastjson.JSON;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月07日 星期日 23:05
 */
public final class JosnUtil {

    private JosnUtil() {
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }
 
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSON.toJSONString(obj);
    }
}
