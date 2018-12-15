package com.axin.communication.tools.common;

import com.axin.communication.algorithm.NetworkCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
     * <p>初始化带时延的MPEM矩阵<br>
     * <p>重传时延并不考虑数据包在传输过程中所经历的链路时
     * 延，而是一个数据包接收错误后，经过多少个单位时间间隔后重新接收这个数据包</p>
     * <p>传输一个数据包需要一个单位时间间隔</p>
     *
     * @param n
     * @param m
     * @param packetLoss
     * @return
     */
    public static int[][] creatDelayMPEM(int n, int m, double packetLoss) {
        int[][] MPEM = creatMPEM(n, m, packetLoss);
        int[][] delayMPEM = new int[MPEM.length + 1][MPEM[0].length];
        for (int i = 0; i < MPEM.length; i++) {
            for (int j = 0; j < MPEM[0].length; j++) {
                delayMPEM[i][j] = MPEM[i][j];
            }
        }
        for (int i = 0; i < m; i++) {
            delayMPEM[MPEM.length][i] = m - 1 - i;
        }
        return delayMPEM;
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
     * @param promote    重传丢包率降低百分比%
     * @return 解码后的MPEM矩阵
     */
    public static int[][] decodeProcess(int[][] MPEM, int[] codePacket, double packetLoss, double promote) {
        //精确计算保留三位小数
        //重传后链路丢包率会有减小
        MPEM = decodeProcess(MPEM, codePacket, packetLoss, promote, false);
        return MPEM;
    }

    /**
     * @param delayMPEM
     * @param codePacket
     * @param packetLoss
     * @param promote
     * @return
     */
    public static int[][] decodeProcess(int[][] delayMPEM, int[] codePacket, double packetLoss,
                                        double promote, Boolean isDelayMPEM) {

        int delay = isDelayMPEM ? 1 : 0;

        packetLoss = new BigDecimal(packetLoss)
                .multiply(new BigDecimal(1).subtract(new BigDecimal(promote)))
                .setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (packetLoss < 0) {
            packetLoss = 0;
        }

        for (int i = 0; i < delayMPEM.length - delay; i++) {
            //模拟丢包
            boolean receive = Math.random() > packetLoss ? true : false;
            if (receive) {
                int sum = 0;
                for (int j = 0; j < codePacket.length; j++) {
                    sum += delayMPEM[i][codePacket[j]];
                }
                //解码成功
                if (sum <= 1) {
                    for (int j = 0; j < codePacket.length; j++) {
                        delayMPEM[i][codePacket[j]] = 0;
                    }
                }
            }
        }
        return delayMPEM;
    }

    /**
     * 多态：接收端具有缓存时
     *
     * @param MPEM
     * @param codePacket
     * @param packetLoss
     * @param cache
     * @param promote
     * @return
     */
    public static int[][] decodeProcess(int[][] MPEM, int[] codePacket, double packetLoss, List cache, double promote) {
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
        //将接收的编码包加入缓存
        for (int n = 0; n < codePacket.length; n++) {
            cache.add(codePacket[n]);
        }
        return MPEM;
    }


    /**
     * 精确计算传输带宽消耗
     *
     * @param reNumber
     * @param packetNumber
     * @return
     */
    public static double computeBandwidth(int reNumber, int packetNumber) {
        return new BigDecimal(reNumber + packetNumber).divide(new BigDecimal(packetNumber))
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确相除
     *
     * @param a
     * @param b
     * @return
     */
    public static double computeDivide(double a, double b) {
        double res2 = new BigDecimal(a).divide(new BigDecimal(b),2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return res2;
    }

    /**
     * 计算编码增益Gain
     * 10*log(arq方法/networkcode方法)
     *
     * @param arq
     * @param networkcode
     * @return
     */
    public static double computeGain(double arq, double networkcode) {
        double ratio = NetworkCodeTools.computeDivide((arq - networkcode), arq);
        return ratio;
    }

    /**
     * 检测两个编码是否满足网络编码解码条件
     *
     * @param code1
     * @param code2
     * @return
     */
    public static boolean detection(int code1, int code2) {
        if ((code1 ^ code2) == code1 + code2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 模拟多播过程计算传输带宽消耗
     * 接收端无缓存功能
     *
     * @param networkCode
     * @param number
     * @param packetNumber
     * @param interval
     * @param packetLoss
     * @param promote
     * @return 传输带宽消耗
     */
    public static double getCommonBandwidth(NetworkCode networkCode, int number, int packetNumber, int interval, double packetLoss, double promote) {
        //重传次数
        int reNumber = 0;
        //剩余传输包数
        int restPacketNumber = packetNumber;
        int[][] MPEM;
        while (restPacketNumber > 0) {
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
        double aveBandwidth = NetworkCodeTools.computeBandwidth(reNumber, packetNumber);
        return aveBandwidth;
    }

    /**
     * 多态：接收端具有缓存功能
     *
     * @param networkCode
     * @param number
     * @param packetNumber
     * @param interval
     * @param packetLoss
     * @param promote
     * @return
     */
    public static double getCacheBandwidth(NetworkCode networkCode, int number, int packetNumber, int interval, double packetLoss, double promote) {
        //重传次数
        int reNumber = 0;
        //剩余传输包数
        int restPacketNumber = packetNumber;
        int[][] MPEM;
        //接收端缓存

        while (restPacketNumber > 0) {
            restPacketNumber -= interval;
            //多播数据包得到MPEM矩阵
            MPEM = NetworkCodeTools.multicastProcess(restPacketNumber, number, interval, packetLoss);

            List<List<Integer>> receiptCache = new ArrayList<>();
            //开始重传
            while (!MatrixTools.isZeroMatrix(MPEM)) {
                //临时缓存
                List<Integer> cache = new ArrayList<>();
                //得到编码包
                int[] codePacket = networkCode.getCodePacket(MPEM);
                reNumber++;
                MPEM = NetworkCodeTools.decodeProcess(MPEM, codePacket, packetLoss, cache, promote);
                receiptCache.add(cache);
                //判断是否可以利用缓存进行解码
                if (receiptCache.size() > 1) {
                    for (int n = 0; n < receiptCache.size() - 1; n++) {
                        int[] temp = MatrixTools.listToArray(receiptCache.get(n));
                        MPEM = NetworkCodeTools.decodeProcess(MPEM, temp, packetLoss, promote);
                    }
                }
            }
            //重置缓存
            receiptCache.clear();
        }
        //精确计算保留两位小数
        double aveBandwidth = NetworkCodeTools.computeBandwidth(reNumber, packetNumber);
        return aveBandwidth;
    }
}
