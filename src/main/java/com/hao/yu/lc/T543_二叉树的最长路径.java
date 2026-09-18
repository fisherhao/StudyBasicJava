package com.hao.yu.lc;

import com.hao.yu.lc.node.TreeNode;

import java.util.Objects;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月16日 星期三 12:37
 */
public class T543_二叉树的最长路径 {

    private static int maxDiameter = 0;

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        TreeNode initTreeNode = getInitTreeNode();
        int depth = depth(initTreeNode);
        System.out.println(maxDiameter);
        System.out.println(depth);
    }

    private static TreeNode getInitTreeNode() {
        return new TreeNode(1, new TreeNode(4, new TreeNode(4), new TreeNode(4)),
            new TreeNode(5, new TreeNode(4), new TreeNode(4)));
    }

    public static int depth(TreeNode root) {

        if (Objects.isNull(root)) {
            return 0;
        }

        int left = depth(root.left);
        int right = depth(root.right);

        //直径
        maxDiameter = Math.max(maxDiameter, left + right);

        //单边节点数
        return Math.max(left, right) + 1;
    }

}
