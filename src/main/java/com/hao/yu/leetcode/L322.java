package com.hao.yu.leetcode;

/**
 * 说明：
 *
 * @author： Vicolen.Hao
 * @date： 2024-03-24 17:56:32
 */
public class L322 {
    public static void main(String[] args) {
        int i = new L322().coinChange(new int[] {1, 2, 5}, 11);
        System.out.println(i);
    }

    public int coinChange(int[] coins, int amount) {
        return dp(coins, amount);
    }

    private int dp(int[] coins, int amount) {

        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }
        int result = Integer.MAX_VALUE;
        for (int coin : coins) {
            int sub = dp(coins, amount - coin);

            if (sub == -1) {
                continue;
            }
            result = Math.min(result, sub + 1);
        }
        return result == Integer.MAX_VALUE ? -1 : result;
    }
}
