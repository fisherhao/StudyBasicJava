package com.hao.yu.lc;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月12日 星期六 23:44
 */
public class T092_链表反转II {

    public static void main(String[] args) {

        ListNode initNodes = getInitNodes();

        ListNode listNode = reverseBetween(initNodes, 2, 4);

        printListNode(listNode);

    }

    private static void printListNode(ListNode listNode) {
        while (listNode.next != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }

    private static ListNode reverseBetween(ListNode head, int left, int right) {

        if (head == null || right <= left) {
            return head;
        }

        //自定义一个头结点
        ListNode dummy = new ListNode(-1);

        dummy.next = head;

        //进入换取之前的节点
        ListNode pre = dummy;

        //进入替换之前的一个节点
        for (int i = 0; i < left - 1; i++) {
            pre = pre.next;
        }

        //第一个节点正式替换的节点
        ListNode cur = pre.next;

        for (int i = left; i < right; i++) {

            ListNode next = cur.next;

            cur.next = next.next;
            next.next = pre.next;
            pre.next = next;

        }

        return dummy.next;
    }

    private static ListNode getInitNodes() {

        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);
        listNode.next.next.next.next.next = new ListNode(6);
        listNode.next.next.next.next.next.next = new ListNode(7);

        return listNode;
    }
}

class ListNode {
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
}

