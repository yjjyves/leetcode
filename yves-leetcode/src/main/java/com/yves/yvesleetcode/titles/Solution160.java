package com.yves.yvesleetcode.titles;

import com.alibaba.fastjson.JSONObject;
import com.yves.yvesleetcode.base.ListNode;

/**
 * 编写一个程序，找到两个单链表相交的起始节点。
 *
 * @author yijinjin
 * @date 2020/8/24 -15:01
 */
public class Solution160 {
    public static void main(String[] args) {
        ListNode a = new ListNode(4);
        ListNode a_2 = new ListNode(1);

        ListNode a_3 = new ListNode(8);
        ListNode a_4 = new ListNode(4);
        ListNode a_5 = new ListNode(5);
        a.next = a_2;
        a_2.next = a_3;
        a_3.next = a_4;
        a_4.next = a_5;

        ListNode b = new ListNode(5);
        ListNode b_2 = new ListNode(0);
        ListNode b_3 = new ListNode(1);
        b.next = b_2;
        b_2.next = b_3;
        b_3.next = a_3;
        a_3.next = a_4;
        a_4.next = a_5;

        ListNode result = getIntersectionNode(a, b);

        System.err.println(JSONObject.toJSONString(result));
    }

    /**
     * 双指针
     *
     * @return
     * @author yijinjin
     * @date 2020/8/24 -15:11
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode headATemp = headA, headBTemp = headB;
        while (headATemp != headBTemp) {
            headATemp = (headATemp == null) ? headB : headATemp.next;

            headBTemp = (headBTemp == null) ? headA : headBTemp.next;
        }
        return headATemp;
    }
}
