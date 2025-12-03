package com.hao.yu.lc;

/**
 * 说明：
 * <p>
 * 1、 本题使用两个for循环来暴力破解，时间复杂度O(n^2)
 * <p>
 * 2、本题使用双指针法，时间复杂度O(n)
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月03日 星期三 16:08
 */
public class T011_盛最多水的容器 {
    public static void main(String[] args) {

        int[] height = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};

        int i = maxArea2(height);
        System.out.println(i);
        System.out.println(maxArea(height));
    }

    public static int maxArea(int[] height) {

        //面积
        int sum = 0;

        int start = 0;
        int end = height.length - 1;

        while (start < end) {
            int area = Math.min(height[start], height[end]) * (end - start);
            sum = Math.max(sum, area);

            if (height[start] < height[end]) {
                start++;
            } else {
                end--;
            }
        }

        return sum;
    }

    public static int maxArea2(int[] height) {

        //面积
        int sum = 0;

        for (int i = 0; i < height.length; i++) {
            for (int j = 0; j < height.length; j++) {
                if (i < j) {
                    int area = Math.min(height[i], height[j]) * (j - i);
                    sum = Math.max(sum, area);
                }
            }
        }

        return sum;
    }
}
