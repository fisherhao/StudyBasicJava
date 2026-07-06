package com.hao.yu.test.proxy.cglib.services;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年07月06日 星期一 21:02
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param message
     *
     * @return
     */
    String send(String message);
}
