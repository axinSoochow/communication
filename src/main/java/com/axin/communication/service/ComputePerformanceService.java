package com.axin.communication.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author axin
 * @Date 18-12-11
 * @summy 算法性能计算服务
 * @
 */
@Slf4j
@Service
public class ComputePerformanceService {

    /**
     * 1.1 计算接收端数量从2-12:1变化，得出数量变化与传输带宽关系图
     * 1.2 对应的编码增益图
     */
    public void computePerformanceWithNumberChange() {
        //1.arq


        //2.Ncwbr

    }

    /**
     * 2.1 链路丢包率0.1-0.5:0.1变化，得出丢包率变化与传输带宽关系图
     * 2.2 对应的编码增益图
     */
    public void computePerformanceWithPacketLossChange() {

    }

    /**
     * 3 缓存大小10-100:5变化，得出缓存大小变化与传输带宽关系图
     */
    public void computePerformanceWithCacheChange() {

    }

    /**
     * 4 TTL 50-500:10变换，得出TT L大小变化与传输带宽关系图
     */
    public void computePerformanceWithTTLChange() {

    }

    /**
     * 5.1 丢包率0.1-0.5:0.1变化，得出丢包率变化与重传时延关系图
     * 5.2 接收端数量从2-12:1变化，得出接收端数量变化与重传时延
     */
    public void computePerformanceWithDelay() {

    }

}
