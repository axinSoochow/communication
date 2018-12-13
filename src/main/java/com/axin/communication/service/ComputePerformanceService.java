package com.axin.communication.service;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author axin
 * @Date 18-12-11
 * @summy 算法性能计算服务
 * @
 */
@Slf4j
@Service
@Data
public class ComputePerformanceService {

    @Autowired
    @Qualifier("arqService")
    private ComputeTBService arqService;

    @Autowired
    @Qualifier("ncwbrService")
    private ComputeTBService ncwbrService;

    @Autowired
    @Qualifier("oncsbService")
    private ComputeTBService oncsbService;

    /**
     * 1.1 计算接收端数量从2-12:1变化，得出数量变化与传输带宽关系图
     * 1.2 对应的编码增益图
     */
    public void computePerformanceWithNumberChange(int number, int packetNumber, int interval, double packetLoss, int times) {
        List<Map> resAboutNumber = new ArrayList<>(8);


        Map<Integer, Double> arqMap = new HashMap<>();
        Map<Integer, Double> ncwbrMap = new HashMap<>();
        Map<Integer, Double> oncsbMap = new HashMap<>();





        log.info("开始计算：接收端数量变化2-12：1与传输带宽关系");
        for (int i = 2; i <= 12; i++) {
            //1.arq
            arqMap.put(i, arqService.computeTB(i, packetNumber, interval, packetLoss, times));
            //2.ncwbr
            ncwbrMap.put(i, ncwbrService.computeTB(i, packetNumber, interval, packetLoss, times));
            //3.oncsb
            oncsbMap.put(i, oncsbService.computeTB(i, packetNumber, interval, packetLoss, times));

        }

        log.info("计算完毕");
        log.info("arq数据为：{}\n" +
                        "ncwbr数据为{}\n" +
                        "oncsb数据为{}"
                , JSON.toJSONString(arqMap)
                , JSON.toJSONString(ncwbrMap)
                , JSON.toJSONString(oncsbMap));
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
