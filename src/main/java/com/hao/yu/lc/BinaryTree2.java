package com.hao.yu.lc;

import com.hao.yu.lc.node.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年10月13日 星期二 23:51
 */
public class BinaryTree2 {

    public static void main(String[] args) {

        preOrder2(getInitNode2());
        levelOrder(getInitNode2());

    }

    private static void preOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.val + "->");
        preOrder(node.left);
        preOrder(node.right);
    }

    /**
     * 非递归的方式实现前序遍历
     *
     * @param node
     */
    private static void preOrder2(TreeNode node) {
        if (node == null) {
            return;
        }

    }

    /**
     * 非递归的方式实现层序遍历
     *
     * @param node
     */
    private static void levelOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        Queue<TreeNode> stack = new LinkedList<>();
        stack.offer(node);

        while (!stack.isEmpty()) {
            TreeNode poll = stack.poll();
            System.out.print(poll.val + "->");

            if (poll.left != null) {
                stack.offer(poll.left);
            }
            if (poll.right != null) {
                stack.offer(poll.right);
            }

        }
    }

    /**
     * 初始化二叉树
     * <p>
     * 1
     * / \
     * 2   3
     * / \ / \
     * 5  6 7  8
     * \ / \   /
     * 9 10 11  12
     *
     * @return
     */
    private static TreeNode getInitNode2() {
        TreeNode root = new TreeNode(1);
        TreeNode n2 = new TreeNode(2);
        TreeNode n3 = new TreeNode(3);
        TreeNode n4 = new TreeNode(4);
        TreeNode n5 = new TreeNode(5);
        TreeNode n6 = new TreeNode(6);
        TreeNode n7 = new TreeNode(7);
        TreeNode n8 = new TreeNode(8);
        TreeNode n9 = new TreeNode(9);
        TreeNode n10 = new TreeNode(10);
        TreeNode n11 = new TreeNode(11);
        TreeNode n12 = new TreeNode(12);

        root.left = n2;
        root.right = n3;
        n2.left = n5;
        n2.right = n6;
        n5.right = n9;
        n6.left = n10;
        n6.right = n11;

        n3.left = n7;
        n3.right = n8;
        n8.left = n12;

        return root;
    }
}
