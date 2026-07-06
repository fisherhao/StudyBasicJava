package com.hao.yu.test.proxy.cglib.services.impl;

import com.hao.yu.test.proxy.cglib.services.SmsService;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年07月06日 星期一 21:05
 */
public class SmsServiceImpl implements SmsService {
    @Override
    public String send(String message) {
        String mes = message + "说几句给我个";

        System.out.println(mes);

        return message;
    }
}
