package com.hao.yu.lc;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月05日 星期五 10:16
 */
public class T016_最接近的三数之和 {

    public static void main(String[] args) {
        System.out.println(new T016_最接近的三数之和().threeSumClosest(new int[]{-1, 2, 1, 4}, 4));
    }

    public int threeSumClosest(int[] nums, int target) {

        int result = 0;

        Arrays.sort(nums);

        int positive = Integer.MAX_VALUE;

        System.out.println(JSON.toJSONString(nums));

        for (int i = 0; i < nums.length - 2; i++) {

            int begin = i + 1;
            int end = nums.length - 1;
            while (begin < end) {
                //计算
                int temp = nums[i] + nums[begin] + nums[end];

                //算差值
                int diff = temp - target;

                if (Math.abs(diff) < positive) {
                    result = temp;
                    positive = Math.abs(diff);
                }

                if (diff == 0) {
                    return target;
                } else if (diff < 0) {
                    begin++;
                } else {
                    end--;
                }
            }
        }

        return result;
    }
}
