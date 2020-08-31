package com.yves.yvesleetcode.titles;

import java.util.HashMap;
import java.util.Map;

/**
 * 多数元素
 * 给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数大于 ⌊ n/2 ⌋ 的元素。
 * <p>
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/majority-element
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author yijinjin
 * @date 2020/8/31 -10:25
 */
public class Solution169 {
    public static void main(String[] args) {
        int nums[] = new int[]{3, 3, 4};
        System.err.println(majorityElement(nums));
    }

    public static int majorityElement(int[] nums) {
        Map<Integer, Integer> countMap = new HashMap<>();
        int maxNum = nums[0];
        for (int num : nums) {
            int count = countMap.getOrDefault(num, 0) + 1;
            if (count >= countMap.getOrDefault(maxNum, 0)) {
                maxNum = num;
            }
            countMap.put(num, count);
        }
        return maxNum;
    }

    //分治法,不用map
    //将数组分成左右两部分，分别求出左半部分的众数 a1 以及右半部分的众数 a2，随后在 a1 和 a2 中选出正确的众数。
    public int majorityElement_2(int[] nums) {
        return majorityElementRec(nums, 0, nums.length - 1);

    }

    private int majorityElementRec(int[] nums, int lo, int hi) {
        if (lo == hi) {
            return nums[lo];
        }

        int mid = (lo + hi) / 2 + lo;
        int left = majorityElementRec(nums, lo, mid);
        int right = majorityElementRec(nums, mid, hi);

        if (left == right) {
            return left;
        }
        int leftCount = countInRange(nums, left, lo, hi);
        int rightCount = countInRange(nums, right, lo, hi);

        return leftCount > rightCount ? left : right;
    }

    private int countInRange(int[] nums, int num, int lo, int hi) {
        int count = 0;
        for (int i = lo; i <= hi; i++) {
            if (nums[i] == num) {
                count++;
            }
        }
        return count;
    }


}
