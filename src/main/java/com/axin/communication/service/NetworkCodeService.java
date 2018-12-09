package com.axin.communication.service;


import com.axin.communication.algorithm.NetworkCode;
import com.axin.communication.domain.TaskResult;

/**
 * 网络编码服务
 * @author Axin
 * @date 18-12-5
 *
 */
public interface NetworkCodeService {


  TaskResult computePerformance(NetworkCode networkCode, int number, int packetNumber, int interval, double packetLoss, double promote);

}
