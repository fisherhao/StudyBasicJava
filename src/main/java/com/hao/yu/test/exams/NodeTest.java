package com.hao.yu.test.exams;

import com.hao.yu.utils.JsonUtil;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月12日 星期六 00:03
 */
public class NodeTest {

    public static void main(String[] args) {
        Node head = getInitNode();

        System.out.println(JsonUtil.toJson(head));

    }

    private static Node getInitNode() {

        Node head = new Node();
        head.setKey(null);
        head.setVal(null);

        Node tail = new Node();
        tail.setKey(null);
        tail.setVal(null);

        head.setNext(tail);
        tail.setPre(head);

        Node current = head;

        for (int i = 1; i <= 10; i++) {

            Node node = new Node();

            node.setKey(i);
            node.setVal(2 * i);

            node.setPre(current);
            current.setNext(node);

            current = node;
        }
        
        // ★ 关键：把最后一个节点和哨兵尾连起来
        current.setNext(tail);
        tail.setPre(current);

        return head;
    }
}

class Node {

    private Integer key;

    private Integer val;

    private Node pre;

    private Node next;

    /**
     * Getter method for property <tt>key</tt>.
     *
     * @return property value of key
     */
    public Integer getKey() {
        return key;
    }

    /**
     * Setter method for property <tt>key</tt>.
     *
     * @param key
     *     value to be assigned to property
     */
    public void setKey(Integer key) {
        this.key = key;
    }

    /**
     * Getter method for property <tt>val</tt>.
     *
     * @return property value of val
     */
    public Integer getVal() {
        return val;
    }

    /**
     * Setter method for property <tt>val</tt>.
     *
     * @param val
     *     value to be assigned to property
     */
    public void setVal(Integer val) {
        this.val = val;
    }

    /**
     * Getter method for property <tt>pre</tt>.
     *
     * @return property value of pre
     */
    public Node getPre() {
        return pre;
    }

    /**
     * Setter method for property <tt>pre</tt>.
     *
     * @param pre
     *     value to be assigned to property
     */
    public void setPre(Node pre) {
        this.pre = pre;
    }

    /**
     * Getter method for property <tt>next</tt>.
     *
     * @return property value of next
     */
    public Node getNext() {
        return next;
    }

    /**
     * Setter method for property <tt>next</tt>.
     *
     * @param next
     *     value to be assigned to property
     */
    public void setNext(Node next) {
        this.next = next;
    }
}
