package com.axin.communication.tools.compute;

/**
 * 网络编码所需要的基本工具类
 *
 * @author Axin
 * @date 18-10-17
 */
public class NetworkCodeTool {

    /**
     * 生成多播数据包丢失矩阵MPEM
     * @param n 行
     * @param m 列
     * @param packetLoss 丢包率
     * @return n行m列的 0 1 矩阵
     */
    public static int[][] creatMPEM(int n, int m, double packetLoss) {
        if (packetLoss >= 1){ throw new RuntimeException("the packetLoss is no means");}
        int[][] matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = Math.random() < packetLoss ? 1 : 0;
            }
        }
        return matrix;
    }

    /**
     * 展示矩阵
     * @param MPEM
     */
    public static void printMPEM(int[][] MPEM) {
        System.out.println("MPEM矩阵：");
        for (int i = 0; i < MPEM.length; i++) {
            for (int j = 0; j < MPEM[0].length; j++) {
                System.out.print(MPEM[i][j]+" ");
            }
            System.out.println();
        }
    }
}
