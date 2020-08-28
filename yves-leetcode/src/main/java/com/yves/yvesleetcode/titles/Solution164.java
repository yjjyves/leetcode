package com.yves.yvesleetcode.titles;

/**
 * 最大间距
 * 给定一个无序的数组，找出数组在排序之后，相邻元素之间最大的差值。
 * 如果数组元素个数小于 2，则返回 0。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [3,6,9,1]
 * 输出: 3
 * 解释: 排序后的数组是 [1,3,6,9], 其中相邻元素 (3,6) 和 (6,9) 之间都存在最大差值 3
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-gap
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author yijinjin
 * @date 2020/8/26 -8:44
 */
public class Solution164 {

    public static void main(String[] args) {
        //int nums[] = new int[]{3, 6, 9, 1};
        //int nums[] = new int[]{3, 3, 3, 3};
        int nums[] = new int[]{1, 1, 1, 1, 1, 5, 5, 5, 5, 5};
        int max = maximumGap(nums);
        System.err.println(max);
    }

    /**
     * 桶和鸽笼原理
     *
     * @return
     * @author yijinjin
     * @date 2020/8/28 -11:15
     */
    public static int maximumGap(int[] nums) {
        if (nums.length < 2) {
            return 0;
        }
        //求最大值,最小值
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        for (int num : nums) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }

        //每个桶的容量
        int gap = Math.max(1, (max - min) / (nums.length - 1));
        int bucketSize = (max - min) / gap + 1;
        Bucket[] buckets = new Bucket[bucketSize];

        //将数据放入桶内
        for (int num : nums) {
            int bucketIndex = (num - min) / gap;
            if (buckets[bucketIndex] == null) {
                buckets[bucketIndex] = new Bucket();
            }
            buckets[bucketIndex].max = Math.max(num, buckets[bucketIndex].max);
            buckets[bucketIndex].min = Math.min(num, buckets[bucketIndex].min);
        }

        //差值大于一个桶的都分在不同的桶,故最大差值不在桶内,在桶之间
        int ans = 0;
        Bucket pre = buckets[0];
        for (int i = 1; i < bucketSize; i++) {
            if (buckets[i] != null && pre != null) {
                ans = Math.max(ans, buckets[i].min - pre.max);
                pre = buckets[i];
            }
        }

        return ans;
    }

    static class Bucket {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
    }
}

