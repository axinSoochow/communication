package com.axin.communication.tools.algorithm;

public interface NetworkCode {
    /**
     * 获得传输带宽消耗
     * @param number
     * @param packetNumber
     * @param interval
     * @param packetLoss
     * @param promote
     * @return
     */
    double getAveBandwidth(int number, int packetNumber, int interval, double packetLoss, double promote);

    /**
     * 获得编码包
     * @param MPEM
     * @return
     */
    int[] getCodePacket(int[][] MPEM);
}

