package com.axin.communication.tools.common;


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
}
