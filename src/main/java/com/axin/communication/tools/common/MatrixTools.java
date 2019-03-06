package com.axin.communication.tools.common;


import java.util.List;

/**
 * 矩阵操作工具类
 * @author Axin
 * @date 18-10-18
 */
public class MatrixTools {

    /**
     * @param matrix
     * @return 是否是零矩阵
     */
    public static boolean isZeroMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 展示矩阵
     *
     * @param MPEM
     */
    public static void printMatrix(int[][] MPEM) {
        System.out.println("矩阵展示：");
        for (int i = 0; i < MPEM.length; i++) {
            for (int j = 0; j < MPEM[0].length; j++) {
                System.out.print(MPEM[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * 矩阵拼接
     *
     * @param a
     * @param b
     * @return
     */
    public static int[][] jointMatrix(int[][] a, int[][] b) {
        //空矩阵判定
        if (isZeroMatrix(a) && !isZeroMatrix(b)) {
            return b;
        }
        if (!isZeroMatrix(a) && isZeroMatrix(b)) {
            return a;
        }
        if (isZeroMatrix(a) && isZeroMatrix(b)) {
            return a;
        }
        if (a.length != b.length) {
            throw new RuntimeException("the length of A is different from b ");
        }

        int n = a.length;
        int m = a[0].length + b[0].length;
        int[][] res = new int[n][m];
        //赋值a矩阵
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                res[i][j] = a[i][j];
            }
        }
        //赋值b矩阵
        for (int i = 0; i < a.length; i++) {
            for (int j = a[0].length; j < m; j++) {
                res[i][j] = b[i][j - a[0].length];
            }
        }
        return res;
    }

    /**
     * List结构转换为数据结构
     *
     * @param list
     * @return
     */
    public static int[] listToArray(List<Integer> list) {
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public static void StringToData(String str) {
        String[] datas = str.split(",");
        for (String data : datas) {
            System.out.println(data);
        }
    }
}
