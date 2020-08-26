package com.yves.yvesleetcode.titles;

import com.yves.yvesleetcode.base.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个链表，判断链表中是否有环。
 * <p>
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/linked-list-cycle
 *
 * @author yijinjin
 * @date 2020/8/21 -9:59
 */
public class Solution141 {
    public static void main(String[] args) {


    }

    /**
     * 遍历链表,判断每个节点是否在之前有出现过 出现过则代表有环
     *
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        Set<ListNode> nodes = new HashSet<>();
        ListNode tempNode = head;
        while (tempNode != null) {
            if (nodes.contains(tempNode)) {
                return true;
            }
            nodes.add(tempNode);

            tempNode = tempNode.next;
        }
        return false;
    }


}
