package com.axin.communication.service;

/**
 * @author axin
 * @date 18-12-11
 * @summy 计算传输带宽服务接口
 */
public interface ComputeTBService {

    /**
     * 计算传输带宽接口
     * @param number
     * @param packetNumber
     * @param interval
     * @param packetLoss
     * @param times 实验重复次数
     */
    double computeTB(int number, int packetNumber, int interval, double packetLoss,int times);

}
