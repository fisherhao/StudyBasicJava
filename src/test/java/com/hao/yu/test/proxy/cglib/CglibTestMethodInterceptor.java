package com.hao.yu.test.proxy.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年07月06日 星期一 21:03
 */
public class CglibTestMethodInterceptor implements MethodInterceptor {
    
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("before method " + method.getName());

        Object object = methodProxy.invokeSuper(o, objects);

        System.out.println("after method " + method.getName());
        return object;
    }
}
