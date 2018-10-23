package com.axin.communication.tools.algorithm;


/**
 * 基于汉明重量网络编码的无线广播重传
 * Hamming Weight Network Encoding Retransmission
 * @author Axin
 * @date 18-10-19
 */
public class Hwner implements NetworkCode {
    @Override
    public double getAveBandwidth(int number, int packetNumber, int interval, double packetLoss, double promote) {
        return 0;
    }

    @Override
    public int[] getCodePacket(int[][] MPEM) {
        return new int[0];
    }
}
