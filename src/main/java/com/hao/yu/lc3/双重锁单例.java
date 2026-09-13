package com.hao.yu.lc3;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月13日 星期日 01:23
 */
public class 双重锁单例 {

    private static volatile 双重锁单例 instance;

    public static void main(String[] args) {

    }

    public static 双重锁单例 getInstance() {

        if (instance == null) {
            synchronized (双重锁单例.class) {
                if (instance == null) {
                    instance = new 双重锁单例();
                }
            }

        }
        return instance;
    }
}
