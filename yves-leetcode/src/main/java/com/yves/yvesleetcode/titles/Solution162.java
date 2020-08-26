package com.yves.yvesleetcode.titles;

/**
 * 寻找峰值
 * 峰值元素是指其值大于左右相邻值的元素。
 * <p>
 * 给定一个输入数组 nums，其中 nums[i] ≠ nums[i+1]，找到峰值元素并返回其索引。
 * <p>
 * 数组可能包含多个峰值，在这种情况下，返回任何一个峰值所在位置即可。
 * <p>
 * 你可以假设 nums[-1] = nums[n] = -∞。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-peak-element
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author yijinjin
 * @date 2020/8/21 -16:54
 */
public class Solution162 {

    public static void main(String[] args) {
        //int[] attr = new int[]{1, 2, 3, 1};
        //int[] attr = new int[]{1,2,1,3,5,6,4};
        int[] attr = new int[]{1};
        int result = findPeakElement(attr);
        System.err.println(result);
    }

    /**
     * 示例 1:
     * 输入: nums = [1,2,3,1]
     * 输出: 2
     * 解释: 3 是峰值元素，你的函数应该返回其索引 2。
     *
     * @param nums
     * @return
     */
    public static int findPeakElement(int[] nums) {
        if (nums.length <= 1) {
            return 0;
        }
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            if ((i == 0 && cur > nums[i + 1]) || (i == nums.length - 1 && cur > nums[i - 1])) {
                return i;
            } else if (i == 0 || i == nums.length - 1) {
                continue;
            }
            int pre = nums[i - 1];
            int next = nums[i + 1];


            if (cur > pre && cur > next) {
                return i;
            }
        }
        return -1;
    }

    //二分法查找
    public int findPeakElement_2(int[] nums) {
        return search(nums, 0, nums.length - 1);
    }

    public int search(int[] nums, int l, int r) {
        if (l == r)
            return l;
        int mid = (l + r) / 2;
        if (nums[mid] > nums[mid + 1])
            return search(nums, l, mid);
        return search(nums, mid + 1, r);
    }

}
