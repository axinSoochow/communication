package com.axin.communication.service;


/**
 * @author axin
 * @date 18-12-11
 * @summy 计算时延接口
 */
public interface ComputeDelayService {

    /**
     * @summy 计算时延接口
     * @param number
     * @param packetNumber
     * @param interval
     * @param packetLoss
     * @param times
     * @return
     */
    double computeDelay(int number, int packetNumber, int interval, double packetLoss, int times);

}
