package com.hao.yu.lc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月02日 星期二 11:36
 */
public class T006_Z字型转换 {

    public static void main(String[] args) {
        String paypalishiring = convert("PAYPALISHIRING", 3);
        System.out.println(paypalishiring);
    }

    public static String convert(String s, int numRows) {

        if (numRows <= 1) {
            return s;
        }

        //123432 123432 123432
        char[] arr = s.toCharArray();

        int level = 1;
        List<ModelTest> list = new ArrayList<>();

        boolean needPlus = true;
        for (char c : arr) {

            ModelTest test = new ModelTest();
            test.setCh(c);
            test.setLevel(level);

            list.add(test);
            if (needPlus) {
                level++;
            } else {
                level--;
            }
            if (level == 1) {
                needPlus = true;
            }

            if (level == numRows) {
                needPlus = false;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int j = 1; j <= numRows; j++) {

            for (ModelTest test : list) {
                if (test.getLevel() == j) {
                    builder.append(test.getCh());
                }
            }

        }

        return builder.toString();
    }

    @Setter
    @Getter
    static class ModelTest {
        private Integer level;

        private char ch;

    }
}
