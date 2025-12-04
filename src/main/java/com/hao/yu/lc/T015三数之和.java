package com.hao.yu.lc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月04日 星期四 17:05
 */
public class T015三数之和 {
    public static List<List<Integer>> threeSum(int[] nums) {

        Arrays.sort(nums);

        List<List<Integer>> result = new ArrayList<>();

        int i = 0;

        int k = nums.length - 1;
        while (i < k) {

        }

        return result;
    }

    public static List<List<Integer>> threeSum2(int[] nums) {
        Integer[] numsTem = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            numsTem[i] = nums[i];
        }
        List<Integer> list = Arrays.asList(numsTem);
        list.sort(Comparator.comparingInt(Integer::intValue).reversed());

        int length = nums.length;

        List<List<Integer>> result = new ArrayList<>();
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < j; k++) {
                    if (list.get(i) + list.get(j) + list.get(k) == 0) {
                        String key = list.get(i) + "-" + list.get(j) + "-" + list.get(k);
                        if (strList.contains(key)) {
                            continue;
                        }
                        result.add(Arrays.asList(list.get(i), list.get(j), list.get(k)));
                        strList.add(key);
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {

        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> lists = threeSum2(nums);
        System.out.println(lists);

        System.out.printf("11{0}", threeSum(nums));
    }
}
