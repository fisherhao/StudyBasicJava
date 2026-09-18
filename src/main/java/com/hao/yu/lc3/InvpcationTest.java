package com.hao.yu.lc3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface A {
    String getPeopleName();
}

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月18日 星期五 12:28
 */
public class InvpcationTest implements InvocationHandler {
    private Object target;

    public InvpcationTest(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("进入切点");
        Object invoke = method.invoke(target, args);
        System.out.println("结束切面");
        return invoke;
    }
}

class Test {
    public static void main(String[] args) {

        A target = new B();
        InvocationHandler handler = new InvpcationTest(target);

        A a = (A) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(), handler);

        System.out.println(target);
        System.out.println(a.getPeopleName());
    }
}

class B implements A {
    @Override
    public String getPeopleName() {
        return "张三";
    }
}