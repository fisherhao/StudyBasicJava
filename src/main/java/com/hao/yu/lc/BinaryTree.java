package com.hao.yu.lc;

import com.hao.yu.lc.node.BNode;

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

        BNode initBNode = getInitNode2();

        preOrder(initBNode);
    }

    /**
     * 二叉树前序遍历:使用非递归的形式出现
     * 1 2 5 9 6 10 11 3 7 8 12
     */
    private static void 二叉树前序遍历2() {

        BNode node = getInitNode2();

        preOrderLevel(node);

    }

    private static void 二叉树中序遍历2() {

        BNode node = getInitNode2();

        preOrderLevel(node);

    }

    /**
     * 二叉树后序遍历
     * 9 10 5 11 6 2 12 8 7 3 1
     */
    private static void 二叉树后序遍历() {

        BNode initBNode = getInitNode2();

        afterOrder(initBNode);
    }

    /**
     * 二叉树的后序遍历2
     */
    private static void 二叉树后序遍历2() {
        BNode node = getInitNode2();
        afterOrderLevel(node);
    }

    /**
     * 二叉树中序遍历
     * 9 5 10 2 11 6 1 7 3 8 12
     */
    private static void 二叉树中序遍历() {

        BNode initBNode = getInitNode2();

        mediumOrder(initBNode);
    }

    /**
     * 钟旭遍历2
     */
    private static void 二叉树钟中遍历2() {
        BNode node = getInitNode2();
        mediumOrderLevel(node);
    }

    /**
     * 二叉树层序遍历
     * 1 2 3 4 5 6 7 8 9 10 11 12
     */
    private static void 二叉树层序遍历() {

        BNode initBNode = getInitNode2();

        levelOrder(initBNode);
    }

    private static void mediumOrder(BNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        mediumOrder(node.left);
        System.out.print(node.val + "->");
        mediumOrder(node.right);
    }

    private static void afterOrder(BNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        afterOrder(node.left);
        afterOrder(node.right);
        System.out.print(node.val + "->");
    }

    private static void levelOrder(BNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        Queue<BNode> queue = new LinkedList<>();
        queue.offer(node);

        while (!queue.isEmpty()) {
            BNode root = queue.poll();
            System.out.print(root.val + "->");
            if (Objects.nonNull(root.left)) {
                queue.offer(root.left);
            }
            if (Objects.nonNull(root.right)) {
                queue.offer(root.right);
            }
        }

    }

    private static void preOrder(BNode node) {

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
    private static void preOrderLevel(BNode node) {

        if (Objects.isNull(node)) {
            return;
        }

        Deque<BNode> queue = new ArrayDeque<>();
        //根-左-右
        queue.push(node);
        while (!queue.isEmpty()) {
            BNode poll = queue.pop();
            int val = poll.val;
            System.out.print(val + "->");
            BNode right = poll.right;
            if (Objects.nonNull(right)) {
                queue.push(right);
            }
            BNode left = poll.left;
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
    private static void mediumOrderLevel(BNode node) {

        if (Objects.isNull(node)) {
            return;
        }

        Deque<BNode> stack = new ArrayDeque<>();
        //左 ->跟->右
        BNode cur = node;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            BNode poll = stack.pop();
            int val = poll.val;
            System.out.print(val + "->");
            cur = poll.right;
        }

    }

    private static void afterOrderLevel(BNode node) {

        if (Objects.isNull(node)) {
            return;
        }

        Deque<BNode> stack1 = new ArrayDeque<>();
        Deque<BNode> stack2 = new ArrayDeque<>();
        stack1.push(node);

        //左 ->右->跟
        while (!stack1.isEmpty()) {
            BNode pop = stack1.pop();
            stack2.push(pop);

            if (Objects.nonNull(pop.left)) {
                stack1.push(pop.left);
            }
            if (Objects.nonNull(pop.right)) {
                stack1.push(pop.right);
            }
        }
        while (!stack2.isEmpty()) {
            BNode pop = stack2.pop();
            System.out.print(pop.val + "->");
        }

    }

    /**
     * 二叉树的高度
     */
    private static void 二叉树的高度() {
        BNode initBNode = getInitNode();

        int height = getHeight(initBNode);

        System.out.println("树的高度为1：");
        System.out.println(height);

        BNode initNode3 = getInitNode();

        int height3 = getHeight2(initNode3);

        System.out.println("树的高度为2：");
        System.out.println(height3);
    }

    private static int getHeight2(BNode BNode) {
        if (Objects.isNull(BNode)) {
            return 0;
        }
        int height = 0;
        height = Math.max(height, getHeight(BNode.right));
        height = Math.max(height, getHeight(BNode.left));

        return height + 1;
    }

    private static int getHeight(BNode BNode) {

        if (Objects.isNull(BNode)) {
            return 0;
        }
        if (Objects.isNull(BNode.left) && Objects.isNull(BNode.right)) {
            return 1;
        }
        int length = 0;

        if (Objects.nonNull(BNode.right)) {
            length = Math.max(length, getHeight(BNode.right));
        }
        if (Objects.nonNull(BNode.left)) {
            length = Math.max(length, getHeight(BNode.left));
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
    private static BNode getInitNode2() {
        BNode root = new BNode(1);
        BNode n2 = new BNode(2);
        BNode n3 = new BNode(3);
        BNode n4 = new BNode(4);
        BNode n5 = new BNode(5);
        BNode n6 = new BNode(6);
        BNode n7 = new BNode(7);
        BNode n8 = new BNode(8);
        BNode n9 = new BNode(9);
        BNode n10 = new BNode(10);
        BNode n11 = new BNode(11);
        BNode n12 = new BNode(12);

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

    private static BNode getInitNode() {

        // 主干左链 1~15，保证树高 15
        BNode n1 = new BNode(1);
        BNode n2 = new BNode(2);
        BNode n3 = new BNode(3);
        BNode n4 = new BNode(4);
        BNode n5 = new BNode(5);
        BNode n6 = new BNode(6);
        BNode n7 = new BNode(7);
        BNode n8 = new BNode(8);
        BNode n9 = new BNode(9);
        BNode n10 = new BNode(10);
        BNode n11 = new BNode(11);
        BNode n12 = new BNode(12);
        BNode n13 = new BNode(13);
        BNode n14 = new BNode(14);
        BNode n15 = new BNode(15);

        // 左链连接
        n1.left = n2;
        n2.left = n3;
        n3.left = n4;
        n4.left = n5;

        // 右子树分支，增加复杂度
        n1.right = new BNode(100);

        BNode n300 = new BNode(300);
        BNode n3001 = new BNode(3001);
        BNode n30011 = new BNode(30011);
        n3.right = n300;
        n300.left = n3001;
        n3001.left = n30011;

        BNode n500 = new BNode(500);
        BNode n5001 = new BNode(5001);
        n5.right = n500;
        n500.left = n5001;

        n7.right = new BNode(700);

        BNode n1000 = new BNode(1000);
        BNode n10001 = new BNode(10001);
        BNode n100011 = new BNode(100011);
        BNode n1000111 = new BNode(1000111);
        n10.right = n1000;
        n1000.left = n10001;
        n10001.left = n100011;
        n100011.left = n1000111;

        n13.right = new BNode(1300);

        return n1;
    }
}

