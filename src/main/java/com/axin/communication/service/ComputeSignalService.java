package com.axin.communication.service;

/**
 * @author axin
 * @date 19-3-22
 */
public interface ComputeSignalService {

    /**
     * 计算信令损耗接口
     * @param number
     * @param packetNumber
     * @param packetLoss
     * @param times
     * @return
     */
    double computeSignalLoss(int number,int packetNumber,double packetLoss,int times);
}
