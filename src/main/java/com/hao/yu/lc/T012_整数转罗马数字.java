package com.hao.yu.lc;

import com.alibaba.fastjson.JSON;

import java.lang.management.BufferPoolMXBean;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月03日 星期三 17:06
 */
public class T012_整数转罗马数字 {

    public static void main(String[] args) {
        System.out.println(intToRoman(3749));
        System.out.println(intToRoman(1994));
        System.out.println(intToRoman(58));
    }

    public static String intToRoman(int num) {

        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "I");
        map.put(4, "IV");
        map.put(5, "V");
        map.put(9, "IX");
        map.put(10, "X");
        map.put(40, "XL");
        map.put(50, "L");
        map.put(90, "XC");
        map.put(100, "C");
        map.put(400, "CD");
        map.put(500, "D");
        map.put(900, "CM");
        map.put(1000, "M");

        List<String> list = Arrays.asList(map.values().toArray(new String[0]));
        Collections.reverse(list);
        List<Integer> list1 = Arrays.asList(map.keySet().toArray(new Integer[0]));
        Collections.reverse(list1);

        String[] keys = list.toArray(new String[0]);
        Integer[] values = list1.toArray(new Integer[0]);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            while (num >= values[i]) {
                builder.append(keys[i]);
                num = num - values[i];
            }
        }

        return builder.toString();
    }
}
