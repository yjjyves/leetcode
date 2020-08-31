package com.yves.yvesleetcode.titles;

import java.util.HashMap;
import java.util.Map;

/**
 * 分数到小数
 * 给定两个整数，分别表示分数的分子 numerator 和分母 denominator，以字符串形式返回小数。
 * <p>
 * 如果小数部分为循环小数，则将循环的部分括在括号内。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/fraction-to-recurring-decimal
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author yijinjin
 * @date 2020/8/28 -15:36
 */
public class Solution166 {
    public static void main(String[] args) {
        String result = fractionToDecimal(1, 6);
        System.err.println("result = " + result);
    }

    public static String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }

        StringBuilder fraction = new StringBuilder();
        //判断结果的正负
        if (numerator < 0 ^ denominator < 0) {
            fraction.append("-");
        }

        //取分子分母的绝对值,结果的正负已经在前面处理了
        long devidend = Math.abs(Long.valueOf(numerator));
        long devisor = Math.abs(Long.valueOf(denominator));

        //两数相除的结果
        fraction.append(devidend / devisor);
        //余数
        long remainder = devidend % devisor;
        //余数为0,代表能整除
        if (remainder == 0) {
            return fraction.toString();
        }

        fraction.append(".");
        //用一个哈希表记录余数出现在小数部分的位置
        Map<Long, Integer> map = new HashMap<>();
        while (remainder != 0) {
            if (map.containsKey(remainder)) {
                fraction.insert(map.get(remainder), "(").append(")");
                break;
            }

            map.put(remainder, fraction.length());
            remainder *= 10;
            fraction.append(remainder / devisor);
            remainder %= devisor;
        }


        return fraction.toString();
    }
}
