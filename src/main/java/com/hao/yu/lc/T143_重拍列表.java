package com.hao.yu.lc;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月16日 星期三 13:29
 */
public class T143_重拍列表 {
    public static void main(String[] args) {

    }

    private static void test() {

    }

    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 断开链表，rightHead 是右半部分的头节点
        ListNode rightHead = slow.next;
        //左边部分是个全链表，现在将其断掉后序的舍弃掉
        slow.next = null;

        ListNode rDummny = new ListNode(-1);
        rDummny.next = rightHead;
        ListNode rCur = rightHead;
        while (rCur.next != null) {
            ListNode next = rCur.next;
            rCur.next = next.next;
            next.next = rDummny.next;
            rDummny.next = next;
        }

        ListNode l1 = head;
        ListNode l2 = rDummny.next;

    }

    private static ListNode initNode() {
        ListNode node = new ListNode(1);
        node.next = new ListNode(2);
        node.next.next = new ListNode(3);
        node.next.next.next = new ListNode(4);
        node.next.next.next.next = new ListNode(5);
        node.next.next.next.next.next = new ListNode(6);
        node.next.next.next.next.next.next = new ListNode(7);
        node.next.next.next.next.next.next.next = new ListNode(8);
        node.next.next.next.next.next.next.next.next = new ListNode(9);
        node.next.next.next.next.next.next.next.next.next = new ListNode(10);
        return node;
    }
}
