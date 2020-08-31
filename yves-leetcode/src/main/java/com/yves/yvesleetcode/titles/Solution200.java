package com.yves.yvesleetcode.titles;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 岛屿数量
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 * <p>
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向或竖直方向上相邻的陆地连接形成。
 * <p>
 * 此外，你可以假设该网格的四条边均被水包围
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-islands
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author yijinjin
 * @date 2020/8/31 -14:24
 */
public class Solution200 {
    public static void main(String[] args) {
        char[][] grid = new char[][]{
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}};

        int result = numIslands(grid);
        System.err.println("result : " + result);
    }

    /**
     * 示例 1:
     *
     * 输入:
     * [
     * ['1','1','1','1','0'],
     * ['1','1','0','1','0'],
     * ['1','1','0','0','0'],
     * ['0','0','0','0','0']
     * ]
     * 输出: 1
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/number-of-islands
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * @return
     * @author yijinjin
     * @date 2020/8/31 -14:26
     */


    /**
     * 深度优先遍历
     * <p>
     * 我们可以将二维网格看成一个无向图，竖直或水平相邻的 11 之间有边相连。
     * <p>
     * 为了求出岛屿的数量，我们可以扫描整个二维网格。如果一个位置为 11，则以其为起始节点开始进行深度优先搜索。在深度优先搜索的过程中，每个搜索到的 11 都会被重新标记为 00。
     * <p>
     * 最终岛屿的数量就是我们进行深度优先搜索的次数。
     * <p>
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/number-of-islands/solution/dao-yu-shu-liang-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @return
     * @author yijinjin
     * @date 2020/8/31 -14:38
     */
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length <= 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;

        int count = 0;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                int num = grid[i][j];
                //以第一个遇到的1为基准 向四周扩散遍历
                if (num == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }

        return count;
    }

    private static void dfs(char[][] grid, int i, int j) {
        int nr = grid.length;
        int nc = grid[0].length;

        if (i < 0 || j < 0 || j >= nc || i >= nr || grid[i][j] == '0') {
            return;
        }

        grid[i][j] = '0';
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j + 1);
    }


    //广度优先算法
    public static int numIslands_2(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;

        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    ++num_islands;
                    grid[r][c] = '0';
                    Queue<Integer> neighbors = new LinkedList<>();
                    neighbors.add(r * nc + c);
                    while (!neighbors.isEmpty()) {
                        int id = neighbors.remove();
                        int row = id / nc;
                        int col = id % nc;
                        if (row - 1 >= 0 && grid[row-1][col] == '1') {
                            neighbors.add((row-1) * nc + col);
                            grid[row-1][col] = '0';
                        }
                        if (row + 1 < nr && grid[row+1][col] == '1') {
                            neighbors.add((row+1) * nc + col);
                            grid[row+1][col] = '0';
                        }
                        if (col - 1 >= 0 && grid[row][col-1] == '1') {
                            neighbors.add(row * nc + col-1);
                            grid[row][col-1] = '0';
                        }
                        if (col + 1 < nc && grid[row][col+1] == '1') {
                            neighbors.add(row * nc + col+1);
                            grid[row][col+1] = '0';
                        }
                    }
                }
            }
        }

        return num_islands;
    }

}
