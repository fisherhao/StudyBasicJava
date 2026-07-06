package com.hao.yu.test.proxy.cglib;

import com.hao.yu.test.proxy.cglib.services.impl.SmsServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年07月06日 星期一 20:59
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CglibTest {
    @org.junit.Test
    public void test() {
        System.out.println("***********************************");

        // 创建动态代理增强类
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(SmsServiceImpl.class.getClassLoader());
        // 设置被代理类
        enhancer.setSuperclass(SmsServiceImpl.class);
        // 设置方法拦截器
        enhancer.setCallback(new CglibTestMethodInterceptor());
        // 创建代理类
        SmsServiceImpl o = (SmsServiceImpl) enhancer.create();

        System.out.println("开始");
        
        System.out.println(o.send("hello"));
        System.out.println("结束");

        System.out.println(o.getClass());
        System.out.println(o.getClass().getSimpleName());
    }
}
