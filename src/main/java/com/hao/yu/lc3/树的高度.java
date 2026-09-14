package com.hao.yu.lc3;

import com.hao.yu.lc.node.NXNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月13日 星期日 18:35
 */
public class 树的高度 {

    public static void main(String[] args) {

        NXNode initNXNode = getInitNode();

        int height = getHeight(initNXNode);

        System.out.println(height);

        NXNode initNXNode2 = getInitNode();
        System.out.println(getiHeight2(initNXNode2));

    }

    public static int getiHeight2(NXNode NXNode) {

        if (Objects.isNull(NXNode)) {
            return 0;
        }

        int height = 0;
        if (Objects.nonNull(NXNode.children)) {
            for (NXNode child : NXNode.children) {
                height = Math.max(getHeight(child), height);
            }
        }

        return height + 1;
    }

    public static int getHeight(NXNode NXNode) {

        if (Objects.isNull(NXNode)) {
            return 0;
        }

        if (Objects.isNull(NXNode.children) || NXNode.children.isEmpty()) {
            return 1;
        }

        int high = 0;
        for (NXNode child : NXNode.children) {
            high = Math.max(high, getHeight(child));
        }

        return high + 1;
    }

    public static NXNode getInitNode() {

        // 第 1 层
        NXNode root = new NXNode(1);

        // 第 2 层：10 ~ 16
        NXNode n10 = new NXNode(10);
        NXNode n11 = new NXNode(11);
        NXNode n12 = new NXNode(12);
        NXNode n13 = new NXNode(13);
        NXNode n14 = new NXNode(14);
        NXNode n15 = new NXNode(15);
        NXNode n16 = new NXNode(16);
        root.children = Arrays.asList(n10, n11, n12, n13, n14, n15, n16);

        // 第 3 层
        NXNode n101 = new NXNode(101);
        NXNode n102 = new NXNode(102);
        NXNode n103 = new NXNode(103);
        n10.children = Arrays.asList(n101, n102, n103);
        // n11 是叶子，children 保持 null

        NXNode n121 = new NXNode(121);
        NXNode n122 = new NXNode(122);
        NXNode n123 = new NXNode(123);
        n12.children = Arrays.asList(n121, n122, n123);

        // n13 是九叉
        NXNode n131 = new NXNode(131);
        NXNode n132 = new NXNode(132);
        NXNode n133 = new NXNode(133);
        NXNode n134 = new NXNode(134);
        NXNode n135 = new NXNode(135);
        NXNode n136 = new NXNode(136);
        NXNode n137 = new NXNode(137);
        NXNode n138 = new NXNode(138);
        NXNode n139 = new NXNode(139);
        n13.children = Arrays.asList(n131, n132, n133, n134, n135, n136, n137, n138,
            n139);

        // n14 是四叉
        NXNode n141 = new NXNode(141);
        NXNode n142 = new NXNode(142);
        NXNode n143 = new NXNode(143);
        NXNode n144 = new NXNode(144);
        n14.children = Arrays.asList(n141, n142, n143, n144);
        // n15 是叶子，children 保持 null
        // n16 是空孩子的特殊情况
        n16.children = new ArrayList<>();

        // 第 4 层：主干 101 是四叉
        NXNode n1011 = new NXNode(1011);
        NXNode n1012 = new NXNode(1012);
        NXNode n1013 = new NXNode(1013);
        NXNode n1014 = new NXNode(1014);
        n101.children = Arrays.asList(n1011, n1012, n1013, n1014);

        NXNode n1211 = new NXNode(1211);
        NXNode n1212 = new NXNode(1212);
        n121.children = Arrays.asList(n1211, n1212);

        // 第 5 层
        NXNode n10111 = new NXNode(10111);
        NXNode n10112 = new NXNode(10112);
        NXNode n10113 = new NXNode(10113);
        n1011.children = Arrays.asList(n10111, n10112, n10113);

        // 第 6 层
        NXNode n101111 = new NXNode(101111);
        NXNode n101112 = new NXNode(101112);
        n10111.children = Arrays.asList(n101111, n101112);

        // 第 7 层：五叉
        NXNode n1011111 = new NXNode(1011111);
        NXNode n1011112 = new NXNode(1011112);
        NXNode n1011113 = new NXNode(1011113);
        NXNode n1011114 = new NXNode(1011114);
        NXNode n1011115 = new NXNode(1011115);
        n101111.children = Arrays.asList(n1011111, n1011112, n1011113, n1011114,
            n1011115);

        // 第 8 层
        NXNode n10111111 = new NXNode(10111111);
        NXNode n10111112 = new NXNode(10111112);
        NXNode n10111113 = new NXNode(10111113);
        n1011111.children = Arrays.asList(n10111111, n10111112, n10111113);

        // 第 9 层：最底层，都是叶子
        NXNode n101111111 = new NXNode(101111111);
        NXNode n101111112 = new NXNode(101111112);
        NXNode n101111113 = new NXNode(101111113);
        n10111111.children = Arrays.asList(n101111111, n101111112, n101111113);

        return root;
    }
}


