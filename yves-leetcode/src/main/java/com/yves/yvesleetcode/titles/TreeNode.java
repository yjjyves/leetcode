package com.yves.yvesleetcode.titles;

import lombok.Data;

@Data
public class TreeNode {
    private TreeNode leftTreeNode;

    private TreeNode rightTreeNode;

    private int value;

    public TreeNode() {
    }

    public TreeNode(int value) {
        this.value = value;
    }

    //获取二叉树的最大高度
    public int getHeight(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        }

        int leftHeight = getHeight(treeNode.getLeftTreeNode());
        int rightHeight = getHeight(treeNode.getLeftTreeNode());

        int max = Math.max(leftHeight, rightHeight);

        //表示加上当前结点，深度为1
        return max + 1;
    }

    //获取二叉树中的最大值
    public int getMaxValue(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        }

        int leftMax = getMaxValue(treeNode.getLeftTreeNode());
        int rightMax = getMaxValue(treeNode.getRightTreeNode());

        int max = Math.max(leftMax, rightMax);

        return max;
    }

    //二叉搜索树的插入：在treeNode中插入value，若value已存在则不变
    public TreeNode addTreeNode(TreeNode treeNode, int value) {
        if (treeNode == null) {
            return new TreeNode(value);
        }

        if (value > treeNode.getValue()) {
            treeNode.setRightTreeNode(addTreeNode(treeNode.getRightTreeNode(), value));
        } else {
            treeNode.setLeftTreeNode(addTreeNode(treeNode.getLeftTreeNode(), value));
        }
        //TODO 此处为啥不是返回value的节点,而是父节点 : 递归方法 需要重新设置父节点的子节点
        return treeNode;
    }

    //二叉搜索树的查找指定的值： 因为一棵由n个结点随机构造的二叉查找树的高度为lgn，所以顺理成章，二叉查找树的一般操作的执行时间为O(lgn)。
    // 但二叉查找树若退化成了一棵具有n个结点的线性链后，则这些操作最坏情况运行时间为O(n)。
    public TreeNode getTreeNode(TreeNode treeNode, int value) {
        if (treeNode == null) {
            return null;
        }
        if (treeNode.getValue() == value) {
            return treeNode;
        }
        if (value > treeNode.getValue()) {
            return getTreeNode(treeNode.getRightTreeNode(), value);
        } else {
            return getTreeNode(treeNode.getLeftTreeNode(), value);
        }
    }


    //删除是二叉查找树中最为复杂的一个操作，可以分成三种情况来考虑：
    //
    //①若是叶子节点的话，只需要将其赋值为空即可；
    //
    //②若仅包含左节点或者右节点，则将其左节点或者右节点的值赋给其本身，将左右节点赋值为空；
    //
    //③若既包含左节点，也包含右节点，则可以通过中序遍历将其前驱（或后继）节点的值赋给其本身，将前驱（后继）节点删除。
    public void deleteTreeNode(TreeNode treeNode, int value) {
        if (treeNode == null) {
            return;
        }
        //1.首先查找到值为value的节点
        TreeNode valueNode = getTreeNode(treeNode, value);
        if (valueNode == null) {
            return;
        }
        //2.若是叶子节点的话，只需要将其赋值为空即可；
        if (valueNode.getLeftTreeNode() == null && valueNode.getRightTreeNode() == null) {
            valueNode = null;
        }
        //3.若仅包含左节点或者右节点，则将其左节点或者右节点的值赋给其本身，将左右节点赋值为空；
        if (valueNode.getLeftTreeNode() != null && valueNode.getRightTreeNode() == null) {
            valueNode = valueNode.getLeftTreeNode();
        } else if (valueNode.getLeftTreeNode() == null && valueNode.getRightTreeNode() != null) {
            valueNode = valueNode.getRightTreeNode();
        } else {//③若既包含左节点，也包含右节点，则可以通过中序遍历将其前驱（或后继）节点的值赋给其本身，将前驱（后继）节点删除。
            valueNode.setValue(getMaxValue(valueNode.getLeftTreeNode()));
            //删除相应的 前驱值的节点
            deleteTreeNode(valueNode.getLeftTreeNode(), value);
        }

    }


}
