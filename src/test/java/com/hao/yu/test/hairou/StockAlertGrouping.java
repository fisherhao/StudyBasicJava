package com.hao.yu.test.hairou;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年10月13日 星期二 15:09
 */

/**
 * 场景描述
 * 多个商品库存低于阈值，需通知补货。系统希望将地理位置相近的商品组合通知，减少通知次数。给定商品列表和距离阈值，求所有可能的组合方式。
 * <p>
 * 题目
 * 给定商品坐标列表，若两商品距离 ≤ t 可合并通知，求所有合法的分组方案。
 */
public class StockAlertGrouping {

    public List<List<Set<Integer>>> group(double[][] pos, double t) {

        // 完成此方法
        List<List<Set<Integer>>> res = new ArrayList<>();

        // do something

        return res;
    }

    // 工具函数
    private boolean canAdd(Set<Integer> g, double[][] p, int i, double t) {
        for (int j : g) {
            double d = Math.sqrt(
                (p[i][0] - p[j][0]) * (p[i][0] - p[j][0]) + (p[i][1] - p[j][1]) * (p[i][1] - p[j][1]));
            if (d > t) return false;
        }
        return true;
    }

    public static void main(String[] args) {

        StockAlertGrouping s = new StockAlertGrouping();

        // TC1: 2 points, dist=1.5, t=1 → cannot group
        double[][] p1 = {{0, 0}, {1, 1}};
        System.out.println("TC1 : " + s.group(p1, 1.0)); // [[[0], [1]]]

        // TC2: t=2.0 → can group
        System.out.println("TC2 : " + s.group(p1, 2.0)); // [[[0, 1]], [[0], [1]]]
    }
}
