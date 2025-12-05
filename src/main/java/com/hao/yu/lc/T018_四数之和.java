package com.hao.yu.lc;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月05日 星期五 18:23
 */
public class T018_四数之和 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                1000000000, 1000000000, 1000000000, 1000000000
        };
        System.out.println(JSON.toJSONString(new T018_四数之和().fourSum(nums, -294967296)));
    }

    public List<List<Integer>> fourSum(int[] nums, int target) {

        Arrays.sort(nums);

        List<List<Integer>> res = new ArrayList<>();

        for (int i = 0; i < nums.length - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            for (int j = i + 1; j < nums.length - 2; j++) {
                if (j > i + 1 && nums[j - 1] == nums[j]) {
                    continue;
                }
                long temTarget = (long) target - (long) nums[i] - (long) nums[j];

                int begin = j + 1;
                int end = nums.length - 1;

                while (begin < end) {
                    long sum = (long) nums[begin] + (long) nums[end];
                    if (sum - temTarget == 0L) {
                        res.add(Arrays.asList(nums[i], nums[j], nums[begin], nums[end]));
                        while (begin < end && nums[begin] == nums[begin + 1]) {
                            begin++;
                        }
                        while (begin < end && nums[end] == nums[end - 1]) {
                            end--;
                        }
                        begin++;
                        end--;
                    } else if (sum - temTarget < 0) {
                        begin++;
                    } else {
                        end--;
                    }
                }
            }
        }

        return res;
    }
}
