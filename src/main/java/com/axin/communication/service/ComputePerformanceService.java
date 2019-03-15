package com.axin.communication.service;

import com.alibaba.fastjson.JSON;
import com.axin.communication.algorithm.NcdiNcBhc;
import com.axin.communication.domain.TaskResult;
import com.axin.communication.tools.common.NetworkCodeTools;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author axin
 * @Date 18-12-11
 * @summy 算法性能计算服务
 */
@Slf4j
@Service
public class ComputePerformanceService {

    @Value("${networkcode.promote}")
    private double promote;

    @Value("${networkcode.hcdi.originalPacket}")
    private int orginalPacket;

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
    private ComputeDelayService hencDelayService;

    @Autowired
    @Qualifier("ncbscService")
    private ComputeTBService ncbscService;

    @Autowired
    private NcdiNcBhc hcdiNcBsc;


    /**
     * 1.1 计算接收端数量从2-12:1变化，得出数量变化与传输带宽关系图
     * 1.2 对应的编码增益图
     */
    public void computePerformanceWithNumberChange(int number, int packetNumber, int interval, double packetLoss, int times)
    {
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
            ncbsc = ncbscService.computeTB(i, packetNumber, 0, packetLoss, times);
            ncbscData.add(ncbsc);
            ncbscGData.add(NetworkCodeTools.computeGain(arq, ncbsc));
        }

        //Log
        log.info("基本参数：（接收端数量：{}），（总传输数据包数：{}，（传输时间间隔：{}），（链路丢包率：{}），（实验重复次数：{}）",
                number, packetNumber, interval, packetLoss, times);

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
     * 2.1 丢包率变化与传输带宽关系图
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

        log.info("开始计算：链路丢包率变化0.02-0.20:0.01与传输带宽关系");
        for (int i = 2; i <= 20; i += 1) {

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
            ncbsc = ncbscService.computeTB(number, packetNumber, 0, packetLoss, times);
            ncbscData.add(ncbsc);
            ncbscGData.add(NetworkCodeTools.computeGain(arq, ncbsc));
        }


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
     * 3 缓存大小10-100:5变化，得出缓存大小变化与传输带宽关系图
     */
    public void computePerformanceWithCacheChange(int number, int packetNumber, int interval, double packetLoss, int times) {
        int number5 = 5;
        int number10 = 10;
        double packetLoss01 = 0.1;
        double packetLoss02 = 0.2;

        //0.x轴
        List<Double> axis = new ArrayList<>();

        List<Double> arqData51 = new ArrayList<>();
        List<Double> arqData52 = new ArrayList<>();
        List<Double> arqData101 = new ArrayList<>();
        List<Double> arqData102 = new ArrayList<>();

        List<Double> ncbhcData51 = new ArrayList<>();
        List<Double> ncbhcData52 = new ArrayList<>();
        List<Double> ncbhcData101 = new ArrayList<>();
        List<Double> ncbhcData102 = new ArrayList<>();

        List<Double> theoryData01 = new ArrayList<>();
        List<Double> theoryData02 = new ArrayList<>();

        double arqN5P01;
        double arqN5P02;
        double arqN10P01;
        double arqN10P02;

        double ncbhcN5P01;
        double ncbhcN5P02;
        double ncbhcN10P01;
        double ncbhcN10P02;

        double theory01 = NetworkCodeTools.computeTheoryBandWith(packetLoss01);
        double theory02 = NetworkCodeTools.computeTheoryBandWith(packetLoss02);

        log.info("开始计算：缓存大小10-100:5变化，得出缓存大小变化与传输带宽关系");
        for (int i = 10; i <= 100; i += 5) {
            axis.add(new Double(i));

            //1.arq
            arqN5P01 = arqService.computeTB(number5, packetNumber, interval, packetLoss01, times);
            arqData51.add(arqN5P01);

            arqN5P02 = arqService.computeTB(number5, packetNumber, interval, packetLoss02, times);
            arqData52.add(arqN5P02);

            arqN10P01 = arqService.computeTB(number10, packetNumber, interval, packetLoss01, times);
            arqData101.add(arqN10P01);

            arqN10P02 = arqService.computeTB(number10, packetNumber, interval, packetLoss02, times);
            arqData102.add(arqN10P02);

            //2.ncbhc
            ncbhcN5P01 = ncbscService.computeTB(number5, packetNumber, i, packetLoss01, times);
            ncbhcData51.add(ncbhcN5P01);

            ncbhcN5P02 = ncbscService.computeTB(number5, packetNumber, i, packetLoss02, times);
            ncbhcData52.add(ncbhcN5P02);

            ncbhcN10P01 = ncbscService.computeTB(number10, packetNumber, i, packetLoss01, times);
            ncbhcData101.add(ncbhcN10P01);

            ncbhcN10P02 = ncbscService.computeTB(number10, packetNumber, i, packetLoss02, times);
            ncbhcData102.add(ncbhcN10P02);

            //理论
            theoryData01.add(theory01);
            theoryData02.add(theory02);
        }
        //Log
        log.info("\n" +
                "X轴的数值为{}\n" +
                "arq51数据为{}\n" +
                "arq52数据为{}\n" +
                "arq101数据为{}\n" +
                "arq102数据为{}\n" +
                "ncbhc51数据为{}\n" +
                "ncbhc52数据为{}\n" +
                "ncbhc101数据为{}\n" +
                "ncbhc102数据为{}\n" +
                "理论01数据为{}\n" +
                "理论02数据为{}"
            , JSON.toJSONString(axis)
            , JSON.toJSONString(arqData51)
            , JSON.toJSONString(arqData52)
            , JSON.toJSONString(arqData101)
            , JSON.toJSONString(arqData102)
            , JSON.toJSONString(ncbhcData51)
            , JSON.toJSONString(ncbhcData52)
            , JSON.toJSONString(ncbhcData101)
            , JSON.toJSONString(ncbhcData102)
            , JSON.toJSONString(theoryData01)
            , JSON.toJSONString(theoryData02)
        );
    }

    /**
     * 4.1 TTL 10-200:10变化，得出TTL大小变化与传输带宽关系图
     * 4.2 对应增益图
     * 4.3 对应的ttl超时触发次数
     * 4.4 对应缓存阈值触发次数
     */
    public void computePerformanceWithTTLChange(int number, int packetNumber, int interval, double packetLoss, int times) {
        int number5 = 5;
        int number10 = 10;
        double packetLoss01 = 0.1;
        double packetLoss02 = 0.2;

        //0.x轴
        List<Double> axis = new ArrayList<>();

        //1.y轴
        List<Double> ncbhcData51 = new ArrayList<>();
        List<Double> ncbhcData52 = new ArrayList<>();
        List<Double> ncbhcData101 = new ArrayList<>();
        List<Double> ncbhcData102 = new ArrayList<>();

        double ncbhcN5P01;
        double ncbhcN5P02;
        double ncbhcN10P01;
        double ncbhcN10P02;

        log.info("开始计算：TTL 10-200:10变化，得出TTL大小变化与传输带宽关系");
        //细粒度控制ncbhc的ttl变量
        for (int i = 10; i <= 200; i += 10) {

            axis.add(new Double(i));

            //ncbhc计算
            ncbhcN5P01 = computeNCBHC(number5, packetNumber, packetLoss01, orginalPacket,
                packetNumber, i, promote, times);
            ncbhcData51.add(ncbhcN5P01);

            ncbhcN5P02 = computeNCBHC(number5, packetNumber, packetLoss02, orginalPacket,
                packetNumber, i, promote, times);
            ncbhcData52.add(ncbhcN5P02);

            ncbhcN10P01 = computeNCBHC(number10, packetNumber, packetLoss01, orginalPacket,
                packetNumber, i, promote, times);
            ncbhcData101.add(ncbhcN10P01);

            ncbhcN10P02 = computeNCBHC(number10, packetNumber, packetLoss02, orginalPacket,
                packetNumber, i, promote, times);
            ncbhcData102.add(ncbhcN10P02);
        }

        //Log
        log.info("\n" +
                "X轴的数值为{}\n" +
                "ncbhc51数据为{}\n" +
                "ncbhc512数据为{}\n" +
                "ncbhc101数据为{}\n" +
                "ncbhc102数据为{}\n"
            , JSON.toJSONString(axis)
            , JSON.toJSONString(ncbhcData51)
            , JSON.toJSONString(ncbhcData52)
            , JSON.toJSONString(ncbhcData101)
            , JSON.toJSONString(ncbhcData102)
        );
    }

    /**
     * 重复计算返回平均传输次数的平均值
     */
    public double computeNCBHC(int number, int packetNumber, double packetLoss, int orginalPacket,
        int cacheThreshold, int ttl, double promote, int times) {
        //重复实验
        double res = 0;
        for (int j = 0; j < times; j++) {
            TaskResult result = hcdiNcBsc
                .getBandWithAndDelay(number, packetNumber, packetLoss, orginalPacket, ttl,
                    cacheThreshold, 1, promote);
            int reNumber = result.getReNumber();
            res += NetworkCodeTools.computeBandwidth(reNumber, packetNumber);
        }
        return NetworkCodeTools.computeDivide(res, times);
    }

    /**
     * 5.1 TTL10-200:10变化，得出TTL变化与重传时延关系
     * 5.2 henc与ncbsc时延对比
     */
    public void computePerformanceWithDelay(int number, int packetNumber, int interval, double packetLoss, int times) {
        int number5 = 5;
        int number10 = 10;
        double packetLoss01 = 0.1;
        double packetLoss02 = 0.2;

        //0.x轴
        List<Double> axis = new ArrayList<>();

        List<Double> hencData51 = new ArrayList<>();
        List<Double> hencData52 = new ArrayList<>();
        List<Double> hencData101 = new ArrayList<>();
        List<Double> hencData102 = new ArrayList<>();

        List<Double> ncbhcData51 = new ArrayList<>();
        List<Double> ncbhcData52 = new ArrayList<>();
        List<Double> ncbhcData101 = new ArrayList<>();
        List<Double> ncbhcData102 = new ArrayList<>();

        double hencN5P01;
        double hencN5P02;
        double hencN10P01;
        double hencN10P02;

        double ncbhcN5P01;
        double ncbhcN5P02;
        double ncbhcN10P01;
        double ncbhcN10P02;

        log.info("开始计算：TTL 10-200:10变化，得出TTL大小变化与平均传输时延关系");
        for (int i = 10; i <= 200; i += 10) {
            axis.add(new Double(i));

            //1.henc
            hencN5P01 = hencDelayService
                .computeDelay(number5, packetNumber, interval, packetLoss01, times);
            hencData51.add(hencN5P01);

            hencN5P02 = hencDelayService
                .computeDelay(number5, packetNumber, interval, packetLoss02, times);
            hencData52.add(hencN5P02);

            hencN10P01 = hencDelayService
                .computeDelay(number10, packetNumber, interval, packetLoss01, times);
            hencData101.add(hencN10P01);

            hencN10P02 = hencDelayService
                .computeDelay(number10, packetNumber, interval, packetLoss02, times);
            hencData102.add(hencN10P02);

            //2.ncbhd
            ncbhcN5P01 = computeNCBHCDelay(number5, packetNumber, packetLoss01, orginalPacket,
                packetNumber, i, promote, times);
            ncbhcData51.add(ncbhcN5P01);

            ncbhcN5P02 = computeNCBHCDelay(number5, packetNumber, packetLoss02, orginalPacket,
                packetNumber, i, promote, times);
            ncbhcData52.add(ncbhcN5P02);

            ncbhcN10P01 = computeNCBHCDelay(number10, packetNumber, packetLoss01, orginalPacket,
                packetNumber, i, promote, times);
            ncbhcData101.add(ncbhcN10P01);

            ncbhcN10P02 = computeNCBHCDelay(number10, packetNumber, packetLoss02, orginalPacket,
                packetNumber, i, promote, times);
            ncbhcData102.add(ncbhcN10P02);
        }

        //Log
        log.info("\n" +
                "X轴数值为{}\n" +
                "henc51时延数据为{}\n" +
                "henc52时延数据为{}\n" +
                "henc101时延数据为{}\n" +
                "henc102时延数据为{}\n" +
                "ncbhc51时延数据为{}\n" +
                "ncbhc52时延数据为{}\n" +
                "ncbhc101时延数据为{}\n" +
                "ncbhc102时延数据为{}\n"
            , JSON.toJSONString(axis)
            , JSON.toJSONString(hencData51)
            , JSON.toJSONString(hencData52)
            , JSON.toJSONString(hencData101)
            , JSON.toJSONString(hencData102)
            , JSON.toJSONString(ncbhcData51)
            , JSON.toJSONString(ncbhcData52)
            , JSON.toJSONString(ncbhcData101)
            , JSON.toJSONString(ncbhcData102)
        );
    }

    public double computeNCBHCDelay(int number, int packetNumber, double packetLoss,
        int orginalPacket, int cacheThreshold, int ttl, double promote, int times) {

        double res = 0;
        for (int j = 0; j < times; j++) {
            TaskResult result = hcdiNcBsc
                .getBandWithAndDelay(number, packetNumber, packetLoss, orginalPacket, ttl,
                    cacheThreshold, 1, promote);
            res += result.getDelay();
        }
        return NetworkCodeTools.computeDivide(res, times);
    }
}