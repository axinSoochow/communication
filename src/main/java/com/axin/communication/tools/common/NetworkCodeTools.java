package com.axin.communication.tools.common;

import com.axin.communication.tools.algorithm.NetworkCode;

import java.math.BigDecimal;

/**
 * 网络编码所需要的基本工具类
 *
 * @author Axin
 * @date 18-10-17
 */
public class NetworkCodeTools {

    /**
     * 生成多播数据包丢失矩阵MPEM
     *
     * @param n          行
     * @param m          列
     * @param packetLoss 丢包率
     * @return n行m列的 0 1 矩阵
     */

    public static int[][] creatMPEM(int n, int m, double packetLoss) {
        if (packetLoss >= 1) {
            throw new RuntimeException("the packetLoss is no means");
        }
        if (packetLoss < 0) {
            packetLoss = 0;
        }
        int[][] matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = Math.random() < packetLoss ? 1 : 0;
            }
        }
        return matrix;
    }

    /**
     * 多播过程——发送端得到MPEM矩阵
     *
     * @param restPacketNumber 剩余待发送数据包
     * @param number           接收端数量
     * @param interval         重传时间间隔
     * @param packetLoss       丢包率
     * @return
     */
    public static int[][] multicastProcess(int restPacketNumber, int number, int interval, double packetLoss) {
        int[][] MPEM;
        if (restPacketNumber >= 0) {
            MPEM = NetworkCodeTools.creatMPEM(number, interval, packetLoss);
        } else {
            //如果剩余数据包小于重传时间间隔
            MPEM = NetworkCodeTools.creatMPEM(number, restPacketNumber + interval, packetLoss);
        }
        return MPEM;
    }


    /**
     * 网络编码解码步骤
     *
     * @param MPEM
     * @param codePacket 重传包
     * @param packetLoss 原始丢包率
     * @param promote      重传丢包率降低百分比%
     * @return 解码后的MPEM矩阵
     */
    public static int[][] decodeProcess(int[][] MPEM, int[] codePacket, double packetLoss, double promote) {
        //精确计算保留三位小数
        //重传后链路丢包率会有减小

        packetLoss = new BigDecimal(packetLoss).multiply(new BigDecimal(1).subtract(new BigDecimal(promote)))
                .setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (packetLoss < 0) {
            packetLoss = 0;
        }

        for (int i = 0; i < MPEM.length; i++) {
            //模拟丢包
            boolean receive = Math.random() > packetLoss ? true : false;
            if (receive) {
                int sum = 0;
                for (int j = 0; j < codePacket.length; j++) {
                    sum += MPEM[i][codePacket[j]];
                }
                //解码成功
                if (sum <= 1) {
                    for (int j = 0; j < codePacket.length; j++) {
                        MPEM[i][codePacket[j]] = 0;
                    }
                }
            }
        }
        return MPEM;
    }

    /**
     * 精确计算传输带宽消耗
     * @param reNumber
     * @param packetNumber
     * @return
     */
    public static double computeAveBandwidth(int reNumber,int packetNumber) {
        return new BigDecimal(reNumber + packetNumber).divide(new BigDecimal(packetNumber))
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 模拟多播过程计算传输带宽消耗
     * @param networkCode
     * @param number
     * @param packetNumber
     * @param interval
     * @param packetLoss
     * @param promote
     * @return 传输带宽消耗
     */
    public static double getAveBandwidth(NetworkCode networkCode, int number, int packetNumber, int interval, double packetLoss, double promote) {
        //重传次数
        int reNumber = 0;
        //剩余传输包数
        int restPacketNumber = packetNumber;
        int[][] MPEM;
        while (restPacketNumber >0) {
            restPacketNumber -= interval;
            //多播数据包得到MPEM矩阵
            MPEM = NetworkCodeTools.multicastProcess(restPacketNumber, number, interval, packetLoss);
            //开始重传
            while (!MatrixTools.isZeroMatrix(MPEM)) {
                //得到编码包
                int[] codePacket = networkCode.getCodePacket(MPEM);
                reNumber++;
                MPEM = NetworkCodeTools.decodeProcess(MPEM, codePacket, packetLoss, promote);
            }
        }
        //精确计算保留两位小数
        double aveBandwidth = NetworkCodeTools.computeAveBandwidth(reNumber, packetNumber);
        return aveBandwidth;
    }
}
