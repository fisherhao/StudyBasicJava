package com.hao.yu.lc;

import com.hao.yu.lc.listnode.ListNode;
import com.hao.yu.utils.JsonUtil;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月09日 星期二 17:54
 */
public class T086_分隔链表 {
    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(4);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(2);
        listNode.next.next.next.next = new ListNode(5);
        listNode.next.next.next.next.next = new ListNode(2);
        System.out.println(JsonUtil.toJson(listNode));
        System.out.println(JsonUtil.toJson(new T086_分隔链表().partition(listNode, 3)));
    }

    public ListNode partition(ListNode head, int x) {
    
        ListNode newNode = new ListNode(0);

        ListNode preTarget = newNode;
        ListNode preCurrent = newNode;

        ListNode current = newNode.next;

        return newNode.next;
    }
}
