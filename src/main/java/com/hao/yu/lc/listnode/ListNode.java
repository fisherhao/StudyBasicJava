package com.hao.yu.lc.listnode;

import java.io.Serializable;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月08日 星期一 14:37
 */
public class ListNode implements Serializable {

    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    /**
     * Getter method for property <tt>val</tt>.
     *
     * @return property value of val
     */
    public int getVal() {
        return val;
    }

    /**
     * Setter method for property <tt>val</tt>.
     *
     * @param val
     *         value to be assigned to property
     */
    public void setVal(int val) {
        this.val = val;
    }

    /**
     * Getter method for property <tt>next</tt>.
     *
     * @return property value of next
     */
    public ListNode getNext() {
        return next;
    }

    /**
     * Setter method for property <tt>next</tt>.
     *
     * @param next
     *         value to be assigned to property
     */
    public void setNext(ListNode next) {
        this.next = next;
    }
}