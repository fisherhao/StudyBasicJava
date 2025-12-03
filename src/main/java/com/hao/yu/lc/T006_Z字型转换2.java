package com.hao.yu.lc;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月02日 星期二 11:36
 */
public class T006_Z字型转换2 {

    public static void main(String[] args) {
        String paypalishiring = convert("PAYPALISHIRING", 3);
        System.out.println(paypalishiring);
    }

    public static String convert(String s, int numRows) {

        if (numRows <= 1) {
            return s;
        }

        //行号：123432 123432 123432
        char[] arr = s.toCharArray();

        int level = 1;

        StringBuilder builder = new StringBuilder();

        //是否需要倒转
        boolean needPlus = true;

        for (int i = 1; i <= numRows; i++) {
            for (char c : arr) {

                if (i == level) {
                    builder.append(c);
                }

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
        }

        return builder.toString();
    }

}
