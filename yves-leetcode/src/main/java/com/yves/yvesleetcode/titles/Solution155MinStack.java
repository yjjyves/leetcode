package com.yves.yvesleetcode.titles;

import java.util.Stack;

/**
 * 155
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 * <p>
 * push(x) —— 将元素 x 推入栈中。
 * pop() —— 删除栈顶的元素。
 * top() —— 获取栈顶元素。
 * getMin() —— 检索栈中的最小元素
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/min-stack
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author yijinjin
 * @date 2020/8/24 -14:31
 */
public class Solution155MinStack {
    // 数据栈
    private Stack<Integer> data;

    // 辅助栈
    private Stack<Integer> helper;

    /**
     * initialize your data structure here.
     */
    public Solution155MinStack() {
        data = new Stack<Integer>();
        helper = new Stack<Integer>();
    }

    public void push(int x) {
        data.push(x);
        if (helper.isEmpty() || x <= helper.peek()) {
            helper.push(x);
        } else {
            helper.push(helper.peek());
        }
    }

    public void pop() {
        if (!data.isEmpty()) {
            data.pop();
            helper.pop();
        }
    }

    public int top() {
        if (!data.isEmpty()) {
            return data.peek();
        }
        throw new RuntimeException("栈中元素为空，此操作非法");
    }

    public int getMin() {
        if (!data.isEmpty()) {
            return helper.peek();
        }
        throw new RuntimeException("栈中元素为空，此操作非法");
    }
}
