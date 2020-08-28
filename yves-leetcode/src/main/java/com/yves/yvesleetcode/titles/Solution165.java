package com.yves.yvesleetcode.titles;

/**
 * 比较两个版本号 version1 和 version2。
 * 如果 version1 > version2 返回 1，如果 version1 < version2 返回 -1， 除此之外返回 0。
 * <p>
 * 你可以假设版本字符串非空，并且只包含数字和 . 字符。
 * <p>
 *  . 字符不代表小数点，而是用于分隔数字序列。
 * <p>
 * 例如，2.5 不是“两个半”，也不是“差一半到三”，而是第二版中的第五个小版本。
 * <p>
 * 你可以假设版本号的每一级的默认修订版号为 0。例如，版本号 3.4 的第一级（大版本）和第二级（小版本）修订号分别为 3 和 4。其第三级和第四级修订号均为 0。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/compare-version-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author yijinjin
 * @date 2020/8/28 -15:09
 */
public class Solution165 {

    public static void main(String[] args) {
        int result = compareVersion("1.0.1", "1");
        System.err.println("result: " + result);

        System.err.println("=============================");
        int result2 = compareVersion("7.5.2.4", "7.5.3");
        System.err.println("result: " + result2);

        System.err.println("=============================");
        int result3 = compareVersion("1.01", "1.001");
        System.err.println("result: " + result3);
    }

    /**
     * 示例 2:
     * <p>
     * 输入: version1 = "1.0.1", version2 = "1"
     * 输出: 1
     * 示例 3:
     * <p>
     * 输入: version1 = "7.5.2.4", version2 = "7.5.3"
     * 输出: -1
     * 示例 4：
     * <p>
     * 输入：version1 = "1.01", version2 = "1.001"
     * 输出：0
     * 解释：忽略前导零，“01” 和 “001” 表示相同的数字 “1”。
     * 示例 5：
     * <p>
     * 输入：version1 = "1.0", version2 = "1.0.0"
     * 输出：0
     * 解释：version1 没有第三级修订号，这意味着它的第三级修订号默认为 “0”。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/compare-version-numbers
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * @return
     * @author yijinjin
     * @date 2020/8/28 -15:12
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version1.length() < 1) {
            return -1;
        }
        if (version2 == null || version2.length() < 1) {
            return 1;
        }

        String[] version1Attr = version1.split("\\.");
        int v1Length = version1Attr.length;
        String[] version2Attr = version2.split("\\.");
        int v2Length = version2Attr.length;

        int maxLength = Math.max(v1Length, v2Length);

        for (int i = 0; i < maxLength; i++) {
            int v1 = i < v1Length ? Integer.valueOf(version1Attr[i]) : 0;
            int v2 = i < v2Length ? Integer.valueOf(version2Attr[i]) : 0;
            if (v1 > v2) {
                return 1;
            } else if (v1 < v2) {
                return -1;
            }
        }

        return 0;
    }
}
