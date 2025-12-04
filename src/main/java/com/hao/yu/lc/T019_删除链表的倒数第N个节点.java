package com.hao.yu.lc;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月03日 星期三 20:19
 */
public class T019_删除链表的倒数第N个节点 {

    public static void main(String[] args) {
        ListNode head = new ListNode(10);
        head.next = new ListNode(20);
        head.next.next = new ListNode(30);
        head.next.next.next = new ListNode(40);
        head.next.next.next.next = new ListNode(50);

        ListNode listNode = removeNthFromEnd(head, 1);

        System.out.println(JSON.toJSONString(listNode));

    }

    public static ListNode removeNthFromEnd(ListNode head, int n) {

        Map<Integer, ListNode> map = new HashMap<>();

        ListNode temp = head;
        Integer level = 0;
        map.put(level, head);
        while (Objects.nonNull(temp.next)) {
            level++;
            map.put(level, temp.next);
            temp = temp.next;
        }

        if (n == level + 1) {
            return head.next;
        }
        ListNode node = map.get(level - n);
        node.next = node.next.next;

        return head;
    }

    public static ListNode removeNthFromEnd1(ListNode head, int n) {

        return move(head, n) == n ? head.next : head;
    }

    private static int move(ListNode head, int n) {

        if (head.next == null) {
            return 1;
        }
        int level = move(head.next, n);

        if (level == n) {

            if (level == 1) {
                head.next = null;
            } else {
                head.next = head.next.next;
            }
        }

        return level + 1;
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


