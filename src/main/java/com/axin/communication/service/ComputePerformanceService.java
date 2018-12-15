package com.axin.communication.service;

import com.alibaba.fastjson.JSON;
import com.axin.communication.dao.CommunicationDao;
import com.axin.communication.domain.CommunicationData;
import com.axin.communication.domain.enums.NetworkCodeTypeEnum;
import com.axin.communication.tools.common.NetworkCodeTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author axin
 * @Date 18-12-11
 * @summy 算法性能计算服务
 * @
 */
@Slf4j
@Service
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

    @Autowired
    @Qualifier("oncmbService")
    private ComputeTBService oncmbService;

    @Autowired
    @Qualifier("hwnerService")
    private ComputeTBService hwnerService;

    @Autowired
    @Qualifier("hencService")
    private ComputeTBService hencService;

    @Autowired
    @Qualifier("ncbscService")
    private ComputeTBService ncbscService;

    @Autowired
    private CommunicationDao communicationDao;


    /**
     * 1.1 计算接收端数量从2-12:1变化，得出数量变化与传输带宽关系图
     * 1.2 对应的编码增益图
     */
    public void computePerformanceWithNumberChange(int number, int packetNumber, int interval, double packetLoss, int times) {
        //0.x轴
        List<Double> axis = new ArrayList<>();

        //1.1数据集
        List<Double> arqData = new ArrayList<>();
        List<Double> ncwbrData = new ArrayList<>();
        List<Double> oncsbData = new ArrayList<>();
        List<Double> oncmbData = new ArrayList<>();
        List<Double> hwnerData = new ArrayList<>();
        List<Double> hencData = new ArrayList<>();
        List<Double> ncbscData = new ArrayList<>();

        //1.2增益计算
        List<Double> ncwbrGData = new ArrayList<>();
        List<Double> oncsbGData = new ArrayList<>();
        List<Double> oncmbGData = new ArrayList<>();
        List<Double> hwnerGData = new ArrayList<>();
        List<Double> hencGData = new ArrayList<>();
        List<Double> ncbscGData = new ArrayList<>();


        double arq;
        double ncwbr;
        double oncsb;
        double oncmb;
        double hwner;
        double henc;
        double ncbsc;


        log.info("开始计算：接收端数量变化2-12：1与传输带宽关系");
        for (int i = 2; i <= 12; i++) {
            axis.add(new Double(i));

            //1.arq
            arq = arqService.computeTB(i, packetNumber, interval, packetLoss, times);
            arqData.add(arq);
            //2.ncwbr
            ncwbr = ncwbrService.computeTB(i, packetNumber, interval, packetLoss, times);
            ncwbrData.add(ncwbr);
            ncwbrGData.add(NetworkCodeTools.computeGain(arq, ncwbr));
            //3.oncsb
            oncsb = oncsbService.computeTB(i, packetNumber, interval, packetLoss, times);
            oncsbData.add(oncsb);
            oncsbGData.add(NetworkCodeTools.computeGain(arq, oncsb));
            //4.oncmb
            oncmb = oncmbService.computeTB(i, packetNumber, interval, packetLoss, times);
            oncmbData.add(oncmb);
            oncmbGData.add(NetworkCodeTools.computeGain(arq, oncmb));
            //5.hwner
            hwner = hwnerService.computeTB(i, packetNumber, interval, packetLoss, times);
            hwnerData.add(hwner);
            hwnerGData.add(NetworkCodeTools.computeGain(arq, hwner));
            //6.hcdi-hencService
            henc = hencService.computeTB(i, packetNumber, interval, packetLoss, times);
            hencData.add(henc);
            hencGData.add(NetworkCodeTools.computeGain(arq, henc));
            //7.hcdi-ncbscService
            ncbsc = ncbscService.computeTB(i, packetNumber, interval, packetLoss, times);
            ncbscData.add(ncbsc);
            ncbscGData.add(NetworkCodeTools.computeGain(arq, ncbsc));
        }

        //Dao
        log.info("开始保存计算数据！");
        Map<String, List<Double>> map = new HashMap<>();
        map.put("arq", arqData);

        map.put("ncwbr", ncwbrData);
        map.put("ncwbrG", ncwbrGData);

        map.put("oncsb", oncsbData);
        map.put("oncsbG", oncsbGData);

        map.put("oncmb", oncmbData);
        map.put("oncmbG", oncmbGData);

        map.put("hwner", hwnerData);
        map.put("hwnerG", hwnerGData);

        map.put("henc", hencData);
        map.put("hencG", hencGData);

        map.put("ncbsc", ncbscData);
        map.put("ncbscG", ncbscGData);

        CommunicationData data = new CommunicationData();
        data.setAxis(axis);
        data.setValue(map);
        data.setCreatDate(new Date());
        data.setNetworkCodeType(NetworkCodeTypeEnum.NUMBER);
        communicationDao.addData(data);


        //Log
        log.info("基本参数：（接收端数量：{}），（总传输数据包数：{}，（传输时间间隔：{}），（链路丢包率：{}），（实验重复次数：{}）",
                number, packetNumber, interval, packetLoss, times);
        log.info("X轴数值为：\n{}", JSON.toJSONString(axis));

        log.info("\n" +
                        "X轴数值为{}\n" +
                        "arq数据为{}\n" +
                        "ncwbr数据为{}\n" +
                        "oncsb数据为{}\n" +
                        "oncmb数据为{}\n" +
                        "hwner数据为{}\n" +
                        "henc数据为{}\n" +
                        "ncbsc数据为{}"
                , JSON.toJSONString(axis)
                , JSON.toJSONString(arqData)
                , JSON.toJSONString(ncwbrData)
                , JSON.toJSONString(oncsbData)
                , JSON.toJSONString(oncmbData)
                , JSON.toJSONString(hwnerData)
                , JSON.toJSONString(hencData)
                , JSON.toJSONString(ncbscData));

        log.info("\n" +
                        "X轴数值为{}\n" +
                        "ncwbr增益数据为{}\n" +
                        "oncsb增益数据为{}\n" +
                        "oncmb增益数据为{}\n" +
                        "hwner增益数据为{}\n" +
                        "henc增益数据为{}\n" +
                        "ncbsc增益数据为{}"
                , JSON.toJSONString(axis)
                , JSON.toJSONString(ncwbrGData)
                , JSON.toJSONString(oncsbGData)
                , JSON.toJSONString(oncmbGData)
                , JSON.toJSONString(hwnerGData)
                , JSON.toJSONString(hencGData)
                , JSON.toJSONString(ncbscGData));
    }

    /**
     * 2.1 链路丢包率0.1-0.30:0.1变化，得出丢包率变化与传输带宽关系图
     * 2.2 对应的编码增益图
     */
    public void computePerformanceWithPacketLossChange(int number, int packetNumber, int interval, double packetLoss, int times) {
        //0.x轴
        List<Double> axis = new ArrayList<>();

        //1.1数据集
        List<Double> arqData = new ArrayList<>();
        List<Double> ncwbrData = new ArrayList<>();
        List<Double> oncsbData = new ArrayList<>();
        List<Double> oncmbData = new ArrayList<>();
        List<Double> hwnerData = new ArrayList<>();
        List<Double> hencData = new ArrayList<>();
        List<Double> ncbscData = new ArrayList<>();

        //1.2增益计算
        List<Double> ncwbrGData = new ArrayList<>();
        List<Double> oncsbGData = new ArrayList<>();
        List<Double> oncmbGData = new ArrayList<>();
        List<Double> hwnerGData = new ArrayList<>();
        List<Double> hencGData = new ArrayList<>();
        List<Double> ncbscGData = new ArrayList<>();


        double arq;
        double ncwbr;
        double oncsb;
        double oncmb;
        double hwner;
        double henc;
        double ncbsc;


        log.info("开始计算：链路丢包率变化0.05-0.30:0.05与传输带宽关系");
        for (int i = 5; i <= 30; i+=5) {

            //处理double类型的链路丢包率
            packetLoss = new BigDecimal(new Double(i)).divide(new BigDecimal(100)).doubleValue();

            axis.add(packetLoss);
            //1.arq
            arq = arqService.computeTB(number, packetNumber, interval, packetLoss, times);
            arqData.add(arq);
            //2.ncwbr
            ncwbr = ncwbrService.computeTB(number, packetNumber, interval, packetLoss, times);
            ncwbrData.add(ncwbr);
            ncwbrGData.add(NetworkCodeTools.computeGain(arq, ncwbr));
            //3.oncsb
            oncsb = oncsbService.computeTB(number, packetNumber, interval, packetLoss, times);
            oncsbData.add(oncsb);
            oncsbGData.add(NetworkCodeTools.computeGain(arq, oncsb));
            //4.oncmb
            oncmb = oncmbService.computeTB(number, packetNumber, interval, packetLoss, times);
            oncmbData.add(oncmb);
            oncmbGData.add(NetworkCodeTools.computeGain(arq, oncmb));
            //5.hwner
            hwner = hwnerService.computeTB(number, packetNumber, interval, packetLoss, times);
            hwnerData.add(hwner);
            hwnerGData.add(NetworkCodeTools.computeGain(arq, hwner));
            //6.hcdi-hencService
            henc = hencService.computeTB(number, packetNumber, interval, packetLoss, times);
            hencData.add(henc);
            hencGData.add(NetworkCodeTools.computeGain(arq, henc));
            //7.hcdi-ncbscService
            ncbsc = ncbscService.computeTB(number, packetNumber, interval, packetLoss, times);
            ncbscData.add(ncbsc);
            ncbscGData.add(NetworkCodeTools.computeGain(arq, ncbsc));
        }

        //Dao
        log.info("开始保存计算数据！");
        Map<String, List<Double>> map = new HashMap<>();
        map.put("arq", arqData);

        map.put("ncwbr", ncwbrData);
        map.put("ncwbrG", ncwbrGData);

        map.put("oncsb", oncsbData);
        map.put("oncsbG", oncsbGData);

        map.put("oncmb", oncmbData);
        map.put("oncmbG", oncmbGData);

        map.put("hwner", hwnerData);
        map.put("hwnerG", hwnerGData);

        map.put("henc", hencData);
        map.put("hencG", hencGData);

        map.put("ncbsc", ncbscData);
        map.put("ncbscG", ncbscGData);

        CommunicationData data = new CommunicationData();
        data.setAxis(axis);
        data.setValue(map);
        data.setCreatDate(new Date());
        data.setNetworkCodeType(NetworkCodeTypeEnum.PACKETLOSS);
        communicationDao.addData(data);


        log.info("基本参数：（接收端数量：{}），（总传输数据包数：{}，（传输时间间隔：{}），（链路丢包率：{}），（实验重复次数：{}）",
                number, packetNumber, interval, packetLoss, times);
        log.info("X轴数值为：\n{}", JSON.toJSONString(axis));

        log.info("\n" +
                        "X轴数值为{}\n" +
                        "arq数据为{}\n" +
                        "ncwbr数据为{}\n" +
                        "oncsb数据为{}\n" +
                        "oncmb数据为{}\n" +
                        "hwner数据为{}\n" +
                        "henc数据为{}\n" +
                        "ncbsc数据为{}"
                , JSON.toJSONString(axis)
                , JSON.toJSONString(arqData)
                , JSON.toJSONString(ncwbrData)
                , JSON.toJSONString(oncsbData)
                , JSON.toJSONString(oncmbData)
                , JSON.toJSONString(hwnerData)
                , JSON.toJSONString(hencData)
                , JSON.toJSONString(ncbscData));

        log.info("\n" +
                        "X轴数值为{}\n" +
                        "ncwbr增益数据为{}\n" +
                        "oncsb增益数据为{}\n" +
                        "oncmb增益数据为{}\n" +
                        "hwner增益数据为{}\n" +
                        "henc增益数据为{}\n" +
                        "ncbsc增益数据为{}"
                , JSON.toJSONString(axis)
                , JSON.toJSONString(ncwbrGData)
                , JSON.toJSONString(oncsbGData)
                , JSON.toJSONString(oncmbGData)
                , JSON.toJSONString(hwnerGData)
                , JSON.toJSONString(hencGData)
                , JSON.toJSONString(ncbscGData));
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
