package com.hao.yu.lc;

import com.hao.yu.lc.node.BNode;

import java.util.Objects;

/**
 * 说明：二叉树相关的操作嘛
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月14日 星期一 10:54
 */
public class BinaryTree {

    public static void main(String[] args) {
        二叉树前序遍历();
    }

    private static void 二叉树前序遍历() {

        BNode initBNode = getInitNode2();

        preOrder(initBNode);
    }

    private static void preOrder(BNode node) {

        if (Objects.isNull(node)) {
            return;
        }

        System.out.println(node.val);

        preOrder(node.left);
        preOrder(node.right);
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

