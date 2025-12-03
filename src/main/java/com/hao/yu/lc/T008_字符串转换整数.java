package com.hao.yu.lc;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月02日 星期二 20:52
 */
public class T00_字符串转换整数 {
    public static void main(String[] args) {

//        System.out.println(myAtoi("42"));
//        System.out.println(myAtoi("-042"));
        System.out.println(myAtoi("2147483646"));
    }

    /**
     * [−2 31,  2 31 − 1]
     *
     * @param s
     *
     * @return
     */
    public static int myAtoi(String s) {

        s = s.trim();

        char[] charArray = s.toCharArray();

        boolean isFushu = s.startsWith("-");

        boolean sign = false;

        boolean first = true;

        Long sum = 0L;

        for (char ch : charArray) {

            if (first) {
                first = false;
                if (ch == '+') {
                    continue;
                }
                if (isFushu) {
                    continue;
                }
            }

            if (ch - '0' > 9 || ch - '0' < 0) {
                break;
            }
            sum = sum * 10 + (ch - '0');
            //负数
            if (isFushu) {
                if (-sum <= Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
                //正数
            } else {
                if (sum >= Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }
            }

        }
        if (isFushu) {
            return -sum.intValue();
        }

        return sum.intValue();
    }
}
