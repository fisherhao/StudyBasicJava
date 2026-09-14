package com.hao.yu.lc3;

import java.util.Objects;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月14日 星期一 10:54
 */
public class 二叉树的高度 {

    public static void main(String[] args) {

        Node2 initNode2 = getInitNode();

        int height = getHeight(initNode2);

        System.out.println("树的高度为1：");
        System.out.println(height);

        Node2 initNode3 = getInitNode();

        int height3 = getHeight(initNode3);

        System.out.println("树的高度为2：");
        System.out.println(height3);

    }

    private static int getHeight2(Node2 node2) {
        if (Objects.isNull(node2)) {
            return 0;
        }
        int height = 0;
        height = Math.max(height, getHeight(node2.right));
        height = Math.max(height, getHeight(node2.left));

        return height + 1;
    }

    private static int getHeight(Node2 node2) {

        if (Objects.isNull(node2)) {
            return 0;
        }
        if (Objects.isNull(node2.left) && Objects.isNull(node2.right)) {
            return 1;
        }
        int length = 0;

        if (Objects.nonNull(node2.right)) {
            length = Math.max(length, getHeight(node2.right));
        }
        if (Objects.nonNull(node2.left)) {
            length = Math.max(length, getHeight(node2.left));
        }

        return length + 1;
    }

    private static Node2 getInitNode() {

        // 主干左链 1~15，保证树高 15
        Node2 n1 = new Node2(1);
        Node2 n2 = new Node2(2);
        Node2 n3 = new Node2(3);
        Node2 n4 = new Node2(4);
        Node2 n5 = new Node2(5);
        Node2 n6 = new Node2(6);
        Node2 n7 = new Node2(7);
        Node2 n8 = new Node2(8);
        Node2 n9 = new Node2(9);
        Node2 n10 = new Node2(10);
        Node2 n11 = new Node2(11);
        Node2 n12 = new Node2(12);
        Node2 n13 = new Node2(13);
        Node2 n14 = new Node2(14);
        Node2 n15 = new Node2(15);

        // 左链连接
        n1.left = n2;
        n2.left = n3;
        n3.left = n4;
        n4.left = n5;

        // 右子树分支，增加复杂度
        n1.right = new Node2(100);

        Node2 n300 = new Node2(300);
        Node2 n3001 = new Node2(3001);
        Node2 n30011 = new Node2(30011);
        n3.right = n300;
        n300.left = n3001;
        n3001.left = n30011;

        Node2 n500 = new Node2(500);
        Node2 n5001 = new Node2(5001);
        n5.right = n500;
        n500.left = n5001;

        n7.right = new Node2(700);

        Node2 n1000 = new Node2(1000);
        Node2 n10001 = new Node2(10001);
        Node2 n100011 = new Node2(100011);
        Node2 n1000111 = new Node2(1000111);
        n10.right = n1000;
        n1000.left = n10001;
        n10001.left = n100011;
        n100011.left = n1000111;

        n13.right = new Node2(1300);

        return n1;
    }
}

class Node2 {
    public int val;
    public Node2 left;

    public Node2 right;

    public Node2() {
    }

    public Node2(int _val) {
        val = _val;
    }

    public Node2(int _val, Node2 _left, Node2 _right) {
        val = _val;
        left = _left;
        right = _right;
    }

}
