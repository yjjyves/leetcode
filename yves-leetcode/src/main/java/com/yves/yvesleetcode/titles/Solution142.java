package com.yves.yvesleetcode.titles;

import com.yves.yvesleetcode.base.ListNode;
import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个链表，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
 * 说明：不允许修改给定的链表
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/linked-list-cycle-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author yijinjin
 * @date 2020/8/21 -10:34
 */
public class Solution142 {

    /**
     * 双指针法判断链表是否有环,时间复杂度 O(n) 空间复杂度 O(1)
     * 有则返回快慢指针第一次相遇的节点，无则返回null
     */

    public ListNode detectCycle(ListNode head) {
        ListNode cycleNode = hasCycle(head);
        if (cycleNode == null) {
            return null;
        }

        while (head != cycleNode) {
            head = head.next;
            cycleNode = cycleNode.next;
        }
        return head;

    }

    public ListNode detectCycle_1(ListNode head) {
        Set<ListNode> nodes = new HashSet<>();

        ListNode tempNode = head;
        while (tempNode != null) {
            if (nodes.contains(tempNode)) {
                return tempNode;
            }
            nodes.add(tempNode);

            tempNode = tempNode.next;
        }
        return null;
    }

    //找到双指针第一次相遇的节点
    public ListNode hasCycle(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            slow = head.next;//走一步
            fast = slow.next;//走两步
            if (slow == fast) {
                return slow;
            }
        }

        return null;
    }
}
