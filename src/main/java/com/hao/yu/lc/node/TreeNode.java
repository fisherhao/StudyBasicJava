package com.hao.yu.lc.node;

import java.io.Serializable;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月15日 星期二 00:08
 */
public class BNode implements Serializable {

    private static final long serialVersionUID = 3663789122656356551L;
    public int val;
    public BNode left;

    public BNode right;

    public BNode() {
    }

    public BNode(int _val) {
        val = _val;
    }

    public BNode(int _val, BNode _left, BNode _right) {
        val = _val;
        left = _left;
        right = _right;
    }
}
