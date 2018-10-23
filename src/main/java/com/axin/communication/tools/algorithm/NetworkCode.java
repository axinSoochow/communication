package com.axin.communication.tools.algorithm;

public interface NetworkCode {
    /**
     * @param number 接收端个数
     * @param packetNumber 传输数据包数量
     * @param interval 重传时间间隔
     * @param packetLoss 平均链接丢包率
     * @param promote 重传丢包率降低百分比
     * @return 传输带宽消耗（平均传输次数）
     */
    double getAveBandwidth(int number, int packetNumber, int interval, double packetLoss, double promote);

    /**
     * 获得编码包
     * @param MPEM
     * @return
     */
    int[] getCodePacket(int[][] MPEM);
}

