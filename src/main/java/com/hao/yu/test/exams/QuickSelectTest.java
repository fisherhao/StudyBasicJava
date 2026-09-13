package com.hao.yu.test.exams;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 说明：找出第K个数字中第N大的数字，数字中可能会有重复
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月03日 星期四 15:44
 */
public class QuickSelectTest {
    public static void main(String[] args) {

    }

    /**
     * 找出第K个最大的值，需要做一下去重哈
     *
     * @param nums
     * @param k
     *
     * @return
     */
    public static int findKthLargest(int[] nums, int k) {

        if (nums == null || nums.length == 0 || k <= 0) {
            throw new IllegalArgumentException("Invalid input");
        }

        int[] distinct = Arrays.stream(nums).distinct().toArray();

        if (distinct.length < k) {
            throw new IllegalArgumentException("超出了数字范围");
        }

        return 0;
    }

    public static int quickSelect(int[] nums, int left, int right, int kIndex) {

        if (left == right) {
            return nums[left];
        }

        int pivot = ThreadLocalRandom.current().nextInt(right - left + 1) + left;

        return 0;
    }

}
