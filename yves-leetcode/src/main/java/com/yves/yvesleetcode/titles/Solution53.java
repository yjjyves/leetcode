package com.yves.yvesleetcode.titles;

/**
 * 最大子序和
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和
 * 输入: [-2,1,-3,4,-1,2,1,-5,4]
 * 输出: 6
 * 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
 *
 * @author yijinjin
 * @date 2020/8/21 -16:54
 */
public class Solution53 {

    public static void main(String[] args) {
        int[] attr = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int result = maxSubArray(attr);
        System.err.println(result);
    }

    /**
     * 这道题用动态规划的思路并不难解决，比较难的是后文提出的用分治法求解，但由于其不是最优解法，所以先不列出来
     * 动态规划的是首先对数组进行遍历，当前最大连续子序列和为 sum，结果为 ans
     * 如果 sum > 0，则说明 sum 对结果有增益效果，则 sum 保留并加上当前遍历数字
     * 如果 sum <= 0，则说明 sum 对结果无增益效果，需要舍弃，则 sum 直接更新为当前遍历数字
     * 每次比较 sum 和 ans的大小，将最大值置为ans，遍历结束返回结果
     * 时间复杂度：O(n)O(n)
     *
     * @param nums
     * @return
     */
    public static int maxSubArray(int[] nums) {
        //初始化最大值
        int ans = nums[0];
        //滑动窗口累加值
        int sum = 0;
        //滑动窗口
        int start = 0, end = 0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (sum > 0) {
                //数组中的元素累加
                sum += num;
            } else {
                //1 第一次窗口为第一个元素
                //2 如果前一个窗口的和为负数则不进行累加
                sum = num;
                start = i;
            }
            //滑动窗口的和与当前最大值比较
            if (sum > ans) {
                end = i;
            }
            ans = Math.max(ans, sum);

        }
        System.err.println("[" + start + " , " + end + "]");
        return ans;
    }

}
