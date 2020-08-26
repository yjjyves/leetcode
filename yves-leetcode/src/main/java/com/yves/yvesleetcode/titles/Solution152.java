package com.yves.yvesleetcode.titles;

/**
 * 146
 * 给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积
 * <p>
 * 示例 1:
 * 输入: [2,3,-2,4]
 * 输出: 6
 * 解释: 子数组 [2,3] 有最大乘积 6。
 * <p>
 * 示例 2:
 * 输入: [-2,0,-1]
 * 输出: 0
 * 解释: 结果不能为 2, 因为 [-2,-1] 不是子数组。
 *
 * @author yijinjin
 * @date 2020/8/21 -16:54
 */
public class Solution152 {


    public static void main(String[] args) {
        int[] attr = new int[]{2, 3, -2, 4};
        int result = maxProduct(attr);
        System.err.println(result);

        System.err.println("---------------------");

        int[] attr2 = new int[]{-2, 0, -1};
        int result2 = maxProduct(attr2);

        System.err.println(result2);
    }

    public static int maxProduct_1(int[] nums) {
        int max = Integer.MIN_VALUE, imax = 1, imin = 1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < 0) {
                int tmp = imax;
                imax = imin;
                imin = tmp;
            }
            imax = Math.max(imax * nums[i], nums[i]);
            imin = Math.min(imin * nums[i], nums[i]);

            max = Math.max(max, imax);
        }
        System.err.println("[" + imin + " , " + imax + "]");
        return max;
    }


    //最小值可能变成最大值 需要记录最小值 + 最大值
    public static int maxProduct(int[] nums) {

        //初始化最大值
        int max = Integer.MIN_VALUE;
        //乘积的最大最小值
        int iMin = 0;
        int iMax = 0;

        //滑动窗口
        int minStart = 0, minEnd = 0;
        int maxStart = 0, maxEnd = 0;

        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];

            //负数  最大值变最小值
            if (num < 0) {
                int temp = iMax;
                iMax = iMin;
                iMin = temp;

                minEnd = i;
                maxStart = i;
                //如果是负数 下标得互换
            } else {
                maxEnd = i;
                minStart = i;
            }

            //因为是整数 乘积会越来越大 0除外
            //存在0的情况 所以滑动窗口需要重新取值
            iMax = Math.max(iMax * num, num);
            iMin = Math.min(iMin * num, iMin);

            max = Math.max(max, iMax);
        }

        System.err.println("[" + minStart + " , " + minEnd + "]");
        return max;
    }


}
