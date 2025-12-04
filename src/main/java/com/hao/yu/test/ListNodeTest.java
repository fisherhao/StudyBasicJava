package com.hao.yu.test;

/**
 * 说明：
 *
 * @author： Witty·Kid Fisher
 * @date： 2022-03-29 22:49:18
 */
public class ListNodeTest {

    public static void main(String[] args) {

        ListNode<Integer> node = getNode();
        print(node);

        ListNode<Integer> integerListNode = reverseLists(node);
        print(integerListNode);

        //		ListNode listNode = reverseList(node);
        //		print(listNode);

    }

    public static ListNode<Integer> getNode() {

        ListNode<Integer> head = new ListNode<>(0);

        ListNode<Integer> node1 = new ListNode<>(1);

        ListNode<Integer> node2 = new ListNode<>(2);

        ListNode<Integer> node3 = new ListNode<>(3);
        ListNode<Integer> node4 = new ListNode<>(4);
        head.setNext(node1);
        node1.setNext(node2);
        node2.setNext(node3);
        node3.setNext(node4);

        return head;
    }

    public static <T> void print(ListNode<T> head) {

        if (head == null) {
            return;
        }

        ListNode p = head;
        while (p != null) {
            System.out.print(p.data + " ");
            p = p.next;
        }
        System.out.println();
    }

    public static <T> ListNode reverseList(ListNode<T> head) {
        //最小子问题，结束
        if (head == null || head.next == null)
            return head;
        //递的过程，一次次拆解问题
        ListNode newHead = reverseList(head.next);
        //归的过程，反转
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    public static <T> ListNode<T> reverseLists(ListNode<T> head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    private static class ListNode<T> {

        private ListNode<T> next;

        private T data;

        public ListNode(T data) {
            this.data = data;
        }

        public void setNext(ListNode<T> next) {
            this.next = next;
        }
    }
}
