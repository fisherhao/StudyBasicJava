package com.hao.yu.lc;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月05日 星期五 16:41
 */
public class T017_电话号码的字母组合 {
    public static void main(String[] args) {
        List<String> strings = letterCombinations("239");
        System.out.println(JSON.toJSONString(strings));
    }

    public static List<String> letterCombinations(String digits) {

        Map<Integer, String[]> map = new HashMap<>();
        map.put(2, new String[]{"a", "b", "c"});
        map.put(3, new String[]{"d", "e", "f"});
        map.put(4, new String[]{"g", "h", "i"});
        map.put(5, new String[]{"j", "k", "l"});
        map.put(6, new String[]{"m", "n", "o"});
        map.put(7, new String[]{"p", "q", "r", "s"});
        map.put(8, new String[]{"t", "u", "v"});
        map.put(9, new String[]{"w", "x", "y", "z"});

        char[] charArray = digits.toCharArray();

        List<String> result = new ArrayList<>();

        boolean notFirst = true;
        for (char c : charArray) {

            Integer num = c - '0';
            String[] str = map.get(num);

            if (result.isEmpty()) {
                Collections.addAll(result, str);
            }

            List<String> temp = new ArrayList<>();
            if (!notFirst) {
                for (String s1 : str) {
                    for (String s : result) {
                        temp.add(s + s1);
                    }
                }
            }
            if (!temp.isEmpty()) {
                result = temp;
            }
            notFirst = false;
        }
        return result;
    }
}
