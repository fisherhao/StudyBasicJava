package com.hao.yu.lc.node;

import java.io.Serializable;
import java.util.List;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月15日 星期二 00:07
 */
public class NXNode implements Serializable {

    private static final long serialVersionUID = 4975221389109774010L;
    public int val;
    public List<NXNode> children;

    public NXNode() {
    }

    public NXNode(int _val) {
        val = _val;
    }

    public NXNode(int _val, List<NXNode> _children) {
        val = _val;
        children = _children;
    }
}
