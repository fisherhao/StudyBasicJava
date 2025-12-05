package com.hao.yu.lc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月04日 星期四 17:05
 */
public class T015三数之和 {
    public static List<List<Integer>> threeSum2(int[] nums) {

        Arrays.sort(nums);
//        System.out.println(JSON.toJSONString(nums));

        List<List<Integer>> result = new ArrayList<>();

        //取出中间值
        for (int j = 1; j < nums.length - 1; j++) {
            //使用左右值
            int begin = 0;
            int end = nums.length - 1;
            while (begin < j && j < end) {
                while (begin + 1 < j && nums[begin + 1] == nums[begin]) {
                    begin++;
                }

                while (end - 1 > j && nums[end] == nums[end - 1]) {
                    end--;
                }
                if (nums[begin] + nums[j] + nums[end] == 0) {
                    result.add(Arrays.asList(nums[begin], nums[j], nums[end]));
                    begin++;
                    end--;
                } else if (nums[begin] + nums[j] + nums[end] < 0) {
                    begin++;
                } else {
                    end--;
                }
            }

        }

        return result;
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }

        // 排序
        Arrays.sort(nums);

        // 外层循环：固定第一个数（i），范围是0到nums.length-3（保证后面有两个数）
        for (int i = 0; i < nums.length - 2; i++) {
            // 去重1：第一个数重复时，直接跳过（避免生成重复三元组）
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            // 双指针：left = i+1（第二个数），right = 末尾（第三个数）
            int begin = i + 1;
            int end = nums.length - 1;

            while (begin < end) {
                int sum = nums[i] + nums[begin] + nums[end];

                if (sum == 0) {
                    // 找到符合条件的三元组，加入结果
                    result.add(Arrays.asList(nums[i], nums[begin], nums[end]));

                    // 去重2：跳过begin的重复元素（避免重复的第二个数）
                    while (begin < end && nums[begin] == nums[begin + 1]) {
                        begin++;
                    }
                    // 去重3：跳过end的重复元素（避免重复的第三个数）
                    while (begin < end && nums[end] == nums[end - 1]) {
                        end--;
                    }

                    // 双指针同时移动（找下一组可能的数）
                    begin++;
                    end--;
                } else if (sum < 0) {
                    //左指针右移（增大数值）
                    begin++;
                } else {
                    // 右指针左移（减小数值）
                    end--;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {

        int[] nums = new int[]{
                0, 0, 0, -1, 0, 1, 2, -1, -4, 9, 9, 9, 9, 9
        };

        List<List<Integer>> threeSum = threeSum(nums);
        System.out.println(threeSum);

    }
}
