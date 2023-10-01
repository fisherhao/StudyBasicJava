package com.hao.yu.test.tools;

import java.util.Set;

import cn.hutool.Hutool;

/**
 * 说明：
 *
 * @author： Vicolen.Hao
 * @date： 2023-10-01 22:19:36
 */
public class TestTool {

    public static void main(String[] args) {

        System.out.println("的额外功");
        System.out.println("点击工具给哦额外");
        Set<Class<?>> allUtils = Hutool.getAllUtils();
        for (Class<?> allUtil : allUtils) {
             System.out.println(allUtil.getSimpleName());
        }
    }
}
