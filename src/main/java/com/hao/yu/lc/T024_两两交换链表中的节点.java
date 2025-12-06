package com.hao.yu.lc;

import com.alibaba.fastjson.JSON;

import java.util.Objects;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月05日 星期五 23:58
 */

public class T024_两两交换链表中的节点 {

    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);
        listNode.next.next.next.next.next = new ListNode(6);
        listNode.next.next.next.next.next.next = new ListNode(7);
        listNode.next.next.next.next.next.next.next = new ListNode(8);
        listNode.next.next.next.next.next.next.next.next = new ListNode(9);
        listNode.next.next.next.next.next.next.next.next.next = new ListNode(10);
        System.out.println(JSON.toJSONString(swapPairs(listNode)));
        System.out.println(JSON.toJSONString(swapPairs(listNode)));

    }

    public static ListNode swapPairs(ListNode head) {

        ListNode newNode = new ListNode(0, head);

        //当前只针
        ListNode current = newNode;
        //尾针
        ListNode tail;
        while (Objects.nonNull(current.next) && Objects.nonNull(current.next.next)) {

            tail = current.next;

            current.next = tail.next;
            tail.next = tail.next.next;
            current.next.next = tail;

            current = tail;
        }

        return newNode.next;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
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

}
