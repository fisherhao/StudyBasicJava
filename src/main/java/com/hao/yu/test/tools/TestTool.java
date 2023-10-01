package com.hao.yu.test.tools;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.Hutool;

/**
 * 说明：
 *
 * @author： Vicolen.Hao
 * @date： 2023-10-01 22:19:36
 */
public class TestTool {

    public static void main(String[] args) {

        System.out.println("查看全部的工具");
        Set<Class<?>> allUtils = Hutool.getAllUtils();

        List<String> collect = allUtils.stream().map(t -> t.getSimpleName()).sorted().collect(Collectors.toList());

        System.out.println(collect);
        System.out.println();
        collect.stream().forEach(System.out::println);
    }
}
