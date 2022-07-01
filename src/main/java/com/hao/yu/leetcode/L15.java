package com.hao.yu.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 说明：
 * <p>
 * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/3sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author： Fisher.Hao
 * @date： 2022-06-26 17:59:29
 */
public class L15 {

	public static void main(String[] args) {

	}

	public List<List<Integer>> threeSum(int[] nums) {

		List<List<Integer>> result = new ArrayList<>();

		//排序
		Arrays.sort(nums);

		for (int i = 0; i < nums.length; i++) {

			//排序后，大于0的话，直接返回
			if (nums[i] > 0) {
				return result;
			}

			//相同的直接返回
			if (i > 0 && nums[i] == nums[i - 1]) {
				continue;
			}
		}

		return null;
	}

}
