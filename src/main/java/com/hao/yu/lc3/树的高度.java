package com.hao.yu.lc3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月13日 星期日 18:35
 */
public class 树的高度 {

    public static void main(String[] args) {

        Node initNode = getInitNode();

        int height = getHeight(initNode);

        System.out.println(height);

        Node initNode2 = getInitNode();
        System.out.println(getiHeight2(initNode2));

    }

    public static int getiHeight2(Node node) {

        if (Objects.isNull(node)) {
            return 0;
        }

        int height = 0;
        if (Objects.nonNull(node.children)) {
            for (Node child : node.children) {
                height = Math.max(getHeight(child), height);
            }
        }

        return height + 1;
    }

    public static int getHeight(Node node) {

        if (Objects.isNull(node)) {
            return 0;
        }

        if (Objects.isNull(node.children) || node.children.isEmpty()) {
            return 1;
        }

        int high = 0;
        for (Node child : node.children) {
            high = Math.max(high, getHeight(child));
        }

        return high + 1;
    }

    public static Node getInitNode() {

        // 第 1 层
        Node root = new Node(1);

        // 第 2 层：10 ~ 16
        Node n10 = new Node(10);
        Node n11 = new Node(11);
        Node n12 = new Node(12);
        Node n13 = new Node(13);
        Node n14 = new Node(14);
        Node n15 = new Node(15);
        Node n16 = new Node(16);
        root.children = Arrays.asList(n10, n11, n12, n13, n14, n15, n16);

        // 第 3 层
        Node n101 = new Node(101);
        Node n102 = new Node(102);
        Node n103 = new Node(103);
        n10.children = Arrays.asList(n101, n102, n103);
        // n11 是叶子，children 保持 null

        Node n121 = new Node(121);
        Node n122 = new Node(122);
        Node n123 = new Node(123);
        n12.children = Arrays.asList(n121, n122, n123);

        // n13 是九叉
        Node n131 = new Node(131);
        Node n132 = new Node(132);
        Node n133 = new Node(133);
        Node n134 = new Node(134);
        Node n135 = new Node(135);
        Node n136 = new Node(136);
        Node n137 = new Node(137);
        Node n138 = new Node(138);
        Node n139 = new Node(139);
        n13.children = Arrays.asList(n131, n132, n133, n134, n135, n136, n137, n138,
            n139);

        // n14 是四叉
        Node n141 = new Node(141);
        Node n142 = new Node(142);
        Node n143 = new Node(143);
        Node n144 = new Node(144);
        n14.children = Arrays.asList(n141, n142, n143, n144);
        // n15 是叶子，children 保持 null
        // n16 是空孩子的特殊情况
        n16.children = new ArrayList<>();

        // 第 4 层：主干 101 是四叉
        Node n1011 = new Node(1011);
        Node n1012 = new Node(1012);
        Node n1013 = new Node(1013);
        Node n1014 = new Node(1014);
        n101.children = Arrays.asList(n1011, n1012, n1013, n1014);

        Node n1211 = new Node(1211);
        Node n1212 = new Node(1212);
        n121.children = Arrays.asList(n1211, n1212);

        // 第 5 层
        Node n10111 = new Node(10111);
        Node n10112 = new Node(10112);
        Node n10113 = new Node(10113);
        n1011.children = Arrays.asList(n10111, n10112, n10113);

        // 第 6 层
        Node n101111 = new Node(101111);
        Node n101112 = new Node(101112);
        n10111.children = Arrays.asList(n101111, n101112);

        // 第 7 层：五叉
        Node n1011111 = new Node(1011111);
        Node n1011112 = new Node(1011112);
        Node n1011113 = new Node(1011113);
        Node n1011114 = new Node(1011114);
        Node n1011115 = new Node(1011115);
        n101111.children = Arrays.asList(n1011111, n1011112, n1011113, n1011114,
            n1011115);

        // 第 8 层
        Node n10111111 = new Node(10111111);
        Node n10111112 = new Node(10111112);
        Node n10111113 = new Node(10111113);
        n1011111.children = Arrays.asList(n10111111, n10111112, n10111113);

        // 第 9 层：最底层，都是叶子
        Node n101111111 = new Node(101111111);
        Node n101111112 = new Node(101111112);
        Node n101111113 = new Node(101111113);
        n10111111.children = Arrays.asList(n101111111, n101111112, n101111113);

        return root;
    }
}

class Node {
    public int val;
    public List<Node> children;

    public Node() {
    }

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }
}

