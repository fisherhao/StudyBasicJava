package com.hao.yu;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication(scanBasePackages = { "com.hao.yu" })
public class StudyJavaApplication {

    public static void main(String[] args) {
        System.out.println("开始启动可好");
        SpringApplication.run(StudyJavaApplication.class, args);
        System.out.println("结束启动哈哈");
        // getAllReqestMapping(run);
    }

    public static void getAllReqestMapping(ConfigurableApplicationContext run) {
        RequestMappingHandlerMapping bean = run.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            Set<RequestMethod> methods = k.getMethodsCondition().getMethods();
            if (CollectionUtils.isEmpty(methods)) {
                methods = new HashSet<>();
                methods.add(RequestMethod.GET);
                methods.add(RequestMethod.POST);
            }
            final Set<String> patterns = k.getPatternsCondition().getPatterns();
            System.out.println("开始输出********************************************");
            for (RequestMethod requestMethod : methods) {
                for (String pattern : patterns) {
                    System.out.println("method：" + requestMethod + ",pattern:" + pattern);
                }
            }
            System.out.println("结束输出********************************************");
        });
    }

}
