package com.axin.communication.tools.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算时延工具
 */
public class DelayTools {


    /**
     * 计算解码后，累加通信成功的包的时延
     */
    public static int computeDelay(int[][] delayMPEM) {
        int number = delayMPEM.length - 1;
        int packet = delayMPEM[0].length;

        int res = 0;
        for (int j = 0; j < packet; j++) {
            int temp = 0;
            for (int i = 0; i < number; i++) {
                temp += delayMPEM[i][j];
            }
            if (temp == 0) {
                res += delayMPEM[number][j];
            }
        }
        return res;
    }

    /**
     * 增加时延
     *
     * @param delayMPEM
     */
    public static void addDelay(int[][] delayMPEM) {
        //检验是否delayMPEM无数据
        try {
            int test = delayMPEM[0].length;
        } catch (Exception e) {
            return;
        }

        int number = delayMPEM.length - 1;
        int packet = delayMPEM[0].length;

        for (int j = 0; j < packet; j++) {
            delayMPEM[number][j]++;
        }
    }

    /**
     * @param delayMPEM
     * @return 是否是零时延矩阵
     */
    public static boolean isZeroMatrix(int[][] delayMPEM) {
        for (int i = 0; i < delayMPEM.length - 1; i++) {
            for (int j = 0; j < delayMPEM[0].length; j++) {
                if (delayMPEM[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * delayMPEM矩阵转化为MPEM矩阵
     *
     * @param delayMPEM
     * @return
     */
    public static int[][] delayMPEM2MPEM(int[][] delayMPEM) {
        int row = delayMPEM.length;
        int col = delayMPEM[0].length;
        int[][] MPEM = new int[row - 1][col];

        for (int i = 0; i < row - 1; i++) {
            System.arraycopy(delayMPEM[i], 0, MPEM[i], 0, col);
        }
        return MPEM;
    }

    /**
     * 矩阵剪枝
     */
    public static int[][] deleteZero(int[][] delayMPEM) {
        int n = delayMPEM.length;
        int m = delayMPEM[0].length;
        List<Integer> target = new ArrayList<>();

        //寻找非零列
        for (int i = 0; i < m; i++) {
            int code = 0;
            for (int j = 0; j < n - 1; j++) {
                code += delayMPEM[j][i];
            }
            if (code != 0) {
                target.add(i);
            }
        }

        if (target.size() < 1) {
            return new int[][]{};
        } else {
            //将非零列赋值到新矩阵
            int[][] res = new int[n][target.size()];
            int resIndex = 0;
            for (int index : target) {
                for (int i = 0; i < n; i++) {
                    res[i][resIndex] = delayMPEM[i][index];
                }
                resIndex++;
            }
            return res;
        }
    }
}
