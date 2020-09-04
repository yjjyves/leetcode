package com.yves.yvesleetcode.titles;

import com.yves.yvesleetcode.base.ListNode;

/**
 * 反转链表
 *
 * @author yijinjin
 * @date 2020/8/31 -19:18
 */
public class Solution206 {
    public ListNode reverseList(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode curr = head;
        ListNode pre = null;
        while (curr.next != null) {
            ListNode tempNext = curr.next;
            curr.next = pre;
            pre = curr;
            curr = tempNext;
        }
        return pre;
    }

    public ListNode reverseList_2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode p = reverseList_2(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }


}
