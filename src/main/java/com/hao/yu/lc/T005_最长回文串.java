package com.hao.yu.lc;

import java.util.Objects;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月18日 星期五 00:05
 */
public class T005_最长回文串 {

    public static void main(String[] args) {

    }

    public static String longestPalindrome(String s) {

        if (Objects.isNull(s)) {
            return null;
        }

        char[] chars = s.toCharArray();

        if (chars.length <= 1) {
            return s;
        }

        int center = 0;

        int maxLength = 0;

        for (int i = 0; i < chars.length; i++) {

            // 奇数长度回文
            int len1 = length(chars, i, i);
            // 偶数长度回文
            int len2 = length(chars, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > maxLength) {
                maxLength = len;
                center = i;
            }
        }
        int startIndex = center - (maxLength - 1) / 2;
        int endIndex = startIndex + maxLength;
        
        return s.substring(startIndex, endIndex);
    }

    private static int length(char[] chars, int left, int right) {

        while (left >= 0 && right < chars.length && chars[left] == chars[right]) {
            left--;
            right++;
        }
        //经过while后，left和right已经超出了回文串的范围，所以需要减去2，因为是坐标所以直接减一
        return right - left - 1;
    }
}
