package com.hao.yu.lc3;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月14日 星期一 19:23
 */
public class 交替线程打印123 {

    private final Object lock = new Object();

    private int num = 0;

    public static void main(String[] args) {
        new 交替线程打印123().print();
    }

    public void print() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {

                synchronized (lock) {

                    while (num % 3 != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    num++;
                    System.out.println(Thread.currentThread().getName() + "打印A");
                    lock.notifyAll();
                }

            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {

                synchronized (lock) {

                    while (num % 3 != 1) {
                        try {

                            lock.wait();

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    num++;
                    System.out.println(Thread.currentThread().getName() + "打印B");
                    lock.notifyAll();

                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (lock) {
                    while (num % 3 != 2) {
                        try {

                            lock.wait();

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    num++;
                    System.out.println(Thread.currentThread().getName() + "打印C");
                    lock.notifyAll();

                }
            }
        }).start();

    }
}
