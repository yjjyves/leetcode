package com.yves.yvesleetcode.titles;

import com.alibaba.fastjson.JSONObject;
import com.yves.yvesleetcode.base.ListNode;

/**
 * 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
 * <p>
 * 输入: 4->2->1->3
 * 输出: 1->2->3->4
 *
 * @author yijinjin
 * @date 2020/8/21 -15:31
 */
public class Solution148 {

    public static void main(String[] args) {
        ListNode head = new ListNode(4);
        ListNode node2 = new ListNode(2);
        ListNode node1 = new ListNode(1);
        ListNode node3 = new ListNode(3);
        head.next = node2;
        node2.next = node1;
        node1.next = node3;

        System.err.println(JSONObject.toJSONString(head));
        ListNode sortNode = sortList(head);
        System.err.println(JSONObject.toJSONString(sortNode));
    }

    //归并排序（递归法）
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null)
            return head;
        //快慢双指针 快指针是满指针的两倍 当fast/fast.next为空时slow指针为中位节点/中位左边的节点
        ListNode fast = head.next, slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        //分割节点为两部分
        ListNode tmp = slow.next;
        slow.next = null;
        ListNode left = sortList(head);
        ListNode right = sortList(tmp);
        //辅助节点,相当于头节点
        ListNode h = new ListNode(0);
        ListNode res = h;
        //归并两个子节点并排序
        while (left != null && right != null) {
            if (left.val < right.val) {
                h.next = left;
                left = left.next;
            } else {
                h.next = right;
                right = right.next;
            }
            h = h.next;
        }
        //剩下的两个子链表补齐
        h.next = left != null ? left : right;
        return res.next;
    }
}
