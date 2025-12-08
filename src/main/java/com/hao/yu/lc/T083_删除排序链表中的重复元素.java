package com.hao.yu.lc;

import com.hao.yu.lc.listnode.ListNode;
import com.hao.yu.utils.JsonUtil;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月08日 星期一 23:37
 */
public class T083_删除排序链表中的重复元素 {

    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(1);
        listNode.next.next = new ListNode(2);
        listNode.next.next.next = new ListNode(3);
        listNode.next.next.next.next = new ListNode(
                3
        );
        System.out.println(JsonUtil.toJson(new T083_删除排序链表中的重复元素().deleteDuplicates(listNode)));

    }

    public ListNode deleteDuplicates(ListNode head) {

        ListNode newNode = new ListNode(0, head);
        ListNode pre = newNode;

        while (pre.next != null) {
            //当前节点和下一个节点相同
            if ((pre.next.next != null && pre.next.val == pre.next.next.val)) {
                pre.next = pre.next.next;
            } else {
                pre = pre.next;
            }
        }

        return newNode.next;
    }
}
