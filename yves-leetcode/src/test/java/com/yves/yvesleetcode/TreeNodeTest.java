package com.yves.yvesleetcode;

import com.alibaba.fastjson.JSONObject;
import com.yves.yvesleetcode.titles.TreeNode;
import org.junit.Test;

import java.util.Arrays;

public class TreeNodeTest {

    @Test
    public void test_01() {
        Person p = new Person();
        p.getClass().getGenericInterfaces();

        TreeNode rootNode = new TreeNode(100);

        rootNode.addTreeNode(rootNode, 110);
        rootNode.addTreeNode(rootNode, 105);
        rootNode.addTreeNode(rootNode, 70);
        rootNode.addTreeNode(rootNode, 80);
        rootNode.addTreeNode(rootNode, 90);

        System.err.println(JSONObject.toJSONString(rootNode));
    }

    @Test
    public void test_02() {
        int nums[] = new int[]{3, 3, 4, 4};
        System.err.println(Arrays.stream(nums).parallel().distinct().count());
        System.err.println(Integer.valueOf(1) == Integer.valueOf(1));
        System.err.println(Integer.valueOf(127) == Integer.valueOf(127));
        System.err.println(Integer.valueOf(128) == Integer.valueOf(128));
    }
}
