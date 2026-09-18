package com.hao.yu.lc;

import com.hao.yu.lc.node.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 说明：二叉树相关的操作嘛
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月14日 星期一 10:54
 */
public class BinaryTree {

    public static void main(String[] args) {
        System.out.println("前序遍历");
        二叉树前序遍历();
        System.out.println();
        System.out.println("中序遍历");
        二叉树中序遍历();
        System.out.println();
        System.out.println("后序遍历");
        二叉树后序遍历();
        System.out.println();
        System.out.println("层序遍历");
        二叉树层序遍历();
        System.out.println();
        System.out.println("===============================");
        System.out.println("前序遍历2");
        二叉树前序遍历2();
        System.out.println();
        System.out.println("中序遍历2");
        二叉树钟中遍历2();
        System.out.println();
        System.out.println("后序遍历2");
        二叉树后序遍历2();
    }

    /**
     * 二叉树前序遍历
     * 1 2 5 9 6 10 11 3 7 8 12
     */
    private static void 二叉树前序遍历() {

        TreeNode initTreeNode = getInitNode2();

        preOrder(initTreeNode);
    }

    /**
     * 二叉树前序遍历:使用非递归的形式出现
     * 1 2 5 9 6 10 11 3 7 8 12
     */
    private static void 二叉树前序遍历2() {

        TreeNode node = getInitNode2();

        preOrderLevel(node);

    }

    private static void 二叉树中序遍历2() {

        TreeNode node = getInitNode2();

        preOrderLevel(node);

    }

    /**
     * 二叉树后序遍历
     * 9 10 5 11 6 2 12 8 7 3 1
     */
    private static void 二叉树后序遍历() {

        TreeNode initTreeNode = getInitNode2();

        afterOrder(initTreeNode);
    }

    /**
     * 二叉树的后序遍历2
     */
    private static void 二叉树后序遍历2() {
        TreeNode node = getInitNode2();
        afterOrderLevel(node);
    }

    /**
     * 二叉树中序遍历
     * 9 5 10 2 11 6 1 7 3 8 12
     */
    private static void 二叉树中序遍历() {

        TreeNode initTreeNode = getInitNode2();

        mediumOrder(initTreeNode);
    }

    /**
     * 钟旭遍历2
     */
    private static void 二叉树钟中遍历2() {
        TreeNode node = getInitNode2();
        mediumOrderLevel(node);
    }

    /**
     * 二叉树层序遍历
     * 1 2 3 4 5 6 7 8 9 10 11 12
     */
    private static void 二叉树层序遍历() {

        TreeNode initTreeNode = getInitNode2();

        levelOrder(initTreeNode);
    }

    private static void mediumOrder(TreeNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        mediumOrder(node.left);
        System.out.print(node.val + "->");
        mediumOrder(node.right);
    }

    private static void afterOrder(TreeNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        afterOrder(node.left);
        afterOrder(node.right);
        System.out.print(node.val + "->");
    }

    private static void levelOrder(TreeNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(node);

        while (!queue.isEmpty()) {
            TreeNode root = queue.poll();
            System.out.print(root.val + "->");
            if (Objects.nonNull(root.left)) {
                queue.offer(root.left);
            }
            if (Objects.nonNull(root.right)) {
                queue.offer(root.right);
            }
        }

    }

    private static void preOrder(TreeNode node) {

        if (Objects.isNull(node)) {
            return;
        }

        System.out.print(node.val + "->");

        preOrder(node.left);
        preOrder(node.right);
    }

    /**
     * 二叉树前序遍历2:使用非递归的形式出现
     *
     * @param node
     */
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
    private static void preOrderLevel(TreeNode node) {

        if (Objects.isNull(node)) {
            return;
        }

        Deque<TreeNode> queue = new ArrayDeque<>();
        //根-左-右
        queue.push(node);
        while (!queue.isEmpty()) {
            TreeNode pop = queue.pop();
            int val = pop.val;
            System.out.print(val + "->");
            TreeNode right = pop.right;
            if (Objects.nonNull(right)) {
                queue.push(right);
            }
            TreeNode left = pop.left;
            if (Objects.nonNull(left)) {
                queue.push(left);
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
    private static void mediumOrderLevel(TreeNode node) {

        if (Objects.isNull(node)) {
            return;
        }

        Deque<TreeNode> stack = new ArrayDeque<>();
        //左 ->跟->右
        TreeNode cur = node;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            TreeNode pop = stack.pop();
            int val = pop.val;
            System.out.print(val + "->");
            cur = pop.right;
        }

    }

    private static void afterOrderLevel(TreeNode node) {

        if (Objects.isNull(node)) {
            return;
        }

        Deque<TreeNode> stack1 = new ArrayDeque<>();
        Deque<TreeNode> stack2 = new ArrayDeque<>();
        stack1.push(node);

        //左 ->右->跟
        while (!stack1.isEmpty()) {
            TreeNode pop = stack1.pop();
            stack2.push(pop);

            if (Objects.nonNull(pop.left)) {
                stack1.push(pop.left);
            }
            if (Objects.nonNull(pop.right)) {
                stack1.push(pop.right);
            }
        }
        while (!stack2.isEmpty()) {
            TreeNode pop = stack2.pop();
            System.out.print(pop.val + "->");
        }

    }

    /**
     * 二叉树的高度
     */
    private static void 二叉树的高度() {
        TreeNode initTreeNode = getInitNode();

        int height = getHeight(initTreeNode);

        System.out.println("树的高度为1：");
        System.out.println(height);

        TreeNode initNode3 = getInitNode();

        int height3 = getHeight2(initNode3);

        System.out.println("树的高度为2：");
        System.out.println(height3);
    }

    private static int getHeight2(TreeNode TreeNode) {
        if (Objects.isNull(TreeNode)) {
            return 0;
        }
        int height = 0;
        height = Math.max(height, getHeight(TreeNode.right));
        height = Math.max(height, getHeight(TreeNode.left));

        return height + 1;
    }

    private static int getHeight(TreeNode TreeNode) {

        if (Objects.isNull(TreeNode)) {
            return 0;
        }
        if (Objects.isNull(TreeNode.left) && Objects.isNull(TreeNode.right)) {
            return 1;
        }
        int length = 0;

        if (Objects.nonNull(TreeNode.right)) {
            length = Math.max(length, getHeight(TreeNode.right));
        }
        if (Objects.nonNull(TreeNode.left)) {
            length = Math.max(length, getHeight(TreeNode.left));
        }

        return length + 1;
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

    private static TreeNode getInitNode() {

        // 主干左链 1~15，保证树高 15
        TreeNode n1 = new TreeNode(1);
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
        TreeNode n13 = new TreeNode(13);
        TreeNode n14 = new TreeNode(14);
        TreeNode n15 = new TreeNode(15);

        // 左链连接
        n1.left = n2;
        n2.left = n3;
        n3.left = n4;
        n4.left = n5;

        // 右子树分支，增加复杂度
        n1.right = new TreeNode(100);

        TreeNode n300 = new TreeNode(300);
        TreeNode n3001 = new TreeNode(3001);
        TreeNode n30011 = new TreeNode(30011);
        n3.right = n300;
        n300.left = n3001;
        n3001.left = n30011;

        TreeNode n500 = new TreeNode(500);
        TreeNode n5001 = new TreeNode(5001);
        n5.right = n500;
        n500.left = n5001;

        n7.right = new TreeNode(700);

        TreeNode n1000 = new TreeNode(1000);
        TreeNode n10001 = new TreeNode(10001);
        TreeNode n100011 = new TreeNode(100011);
        TreeNode n1000111 = new TreeNode(1000111);
        n10.right = n1000;
        n1000.left = n10001;
        n10001.left = n100011;
        n100011.left = n1000111;

        n13.right = new TreeNode(1300);

        return n1;
    }
}

