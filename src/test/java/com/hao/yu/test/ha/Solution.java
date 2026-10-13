package com.hao.yu.test.ha;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 计数器接口
 */
interface Calculate {
    /**
     * 通过一定算法，进行计数，如：a + b, a * b等
     *
     * @param a
     *     参数 1
     * @param b
     *     参数 2
     *
     * @return 计算后的结果
     */
    int compute(int a, int b);
}

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月30日 星期三 22:59
 */
public class Solution implements InvocationHandler {

    private Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        for (Object arg : args) {

            if (arg instanceof Integer) {
                if ((Integer) arg > 100) {
                    arg = 100;
                }
                if ((Integer) arg < 1) {
                    arg = 1;
                }
            }

        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Integer) {
                if ((Integer) args[i] > 100) {
                    args[i] = 100;
                }
                if ((Integer) args[i] < 1) {
                    args[i] = 1;
                }
            }
        }

        System.out.println("before method " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("after method " + method.getName());
        return method.invoke(target, args);
    }

    public Object createProxy(Object target) {
        // 请在这里实现方法

        return Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            this
        );
    }
}

/**
 * 加法器
 */
class AddCalculate implements Calculate {
    @Override
    public int compute(int a, int b) {
        return a + b;
    }
}

/**
 * 减法器
 */
class SubCalculate implements Calculate {
    @Override
    public int compute(int a, int b) {
        return a - b;
    }
}

/**
 * 乘法器
 */
class MulCalculate implements Calculate {
    @Override
    public int compute(int a, int b) {
        return a * b;
    }
}

/**
 * 除法器
 */
class DivCalculate implements Calculate {
    @Override
    public int compute(int a, int b) {
        return a / b;
    }
}