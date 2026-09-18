package com.hao.yu.lc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月18日 星期五 07:47
 */
public class T003_最长不重复子串 {

    public static void main(String[] args) {

    }

    public static int lengthOfLongestSubstring(String s) {

        if (s == null) {
            return 0;
        }
        char[] charArray = s.toCharArray();
        Queue<Character> queue = new LinkedList<>();
        int max = 0;
        for (char c : charArray) {
            while (queue.contains(c)) {
                queue.poll();
            }
            queue.offer(c);
            max = Math.max(max, queue.size());
        }

        return max;
    }

    public static int lengthOfLongestSubstring2(String s) {

        if (s == null || s.isEmpty()) {
            return 0;
        }
        //ABCDAADJE
        //012345678
        char[] charArray = s.toCharArray();
        Map<Character, Integer> map = new HashMap<>();

        int left = 0;
        int maxl = 0;
        for (int i = 0; i < charArray.length; i++) {
            if (map.containsKey(charArray[i])) {
                left = Math.max(left, map.get(charArray[i]) + 1);
            }
            map.put(charArray[i], i);

            maxl = Math.max(maxl, i - left + 1);
        }

        return left;
    }
}
