package com.hao.yu.lc;

import com.hao.yu.lc.listnode.ListNode;
import com.hao.yu.utils.JsonUtil;

import java.util.Objects;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2025年12月08日 星期一 14:36
 */
public class T082_删除排序链表中的重复元素II {

    public static void main(String[] args) {

        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(3);
        listNode.next.next.next.next = new ListNode(4);
        listNode.next.next.next.next.next = new ListNode(4);
        listNode.next.next.next.next.next.next = new ListNode(5);

        System.out.println(JsonUtil.toJson(new T082_删除排序链表中的重复元素II().deleteDuplicates(listNode)));
    }

    public ListNode deleteDuplicates(ListNode head) {

        ListNode newNode = new ListNode(0, head);
        ListNode pre = newNode;

        //记录一下已经重复了的节点，用于做排除运算
        ListNode temp = null;

        while (pre.next != null) {

            if ((pre.next.next != null && pre.next.val == pre.next.next.val) || (Objects.nonNull(
                    temp) && temp.val == pre.next.val)) {

                temp = pre.next;
                pre.next = pre.next.next;
            } else {
                pre = pre.next;
            }
        }

        return newNode.next;
    }

}
