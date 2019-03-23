package com.axin.communication.web;

import com.axin.communication.service.ComputePerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AxinController {

    @Value("${networkcode.number}")
    private int number;

    @Value("${networkcode.packetNumber}")
    private int packetNumber;

    @Value("${networkcode.interval}")
    private int interval;

    @Value("${networkcode.packetLoss}")
    private double packetLoss;

    @Value("${networkcode.times}")
    private int times;

    @Autowired
    private ComputePerformanceService computePerformanceService;

    @RequestMapping("/number")
    public String computeNumber() {

        computePerformanceService.computePerformanceWithNumberChange(number, packetNumber, interval, packetLoss, times);
        return "计算完毕!";
    }

    @RequestMapping("/packetloss")
    public String computePacketLoss() {

        computePerformanceService.computePerformanceWithPacketLossChange(number, packetNumber, interval, packetLoss, times);
        return "计算完毕!";
    }

    @RequestMapping("/cache")
    public String computeCache() {
//        packetLoss = 0.20;
        computePerformanceService.computePerformanceWithCacheChange(number, packetNumber, interval, packetLoss, times);
        return "计算完毕！";
    }


    @RequestMapping("/ttl")
    public String computeTTL() {
        computePerformanceService.computePerformanceWithTTLChange(number, packetNumber, interval, packetLoss, times);
        return "计算完毕！";
    }

    @RequestMapping("/delay")
    public String computeDelay() {
        computePerformanceService.computePerformanceWithDelay(number, packetNumber, interval, packetLoss, times);
        return "计算完毕！";
    }

    /**
     * 计算LSLRINC编码阈值变化下——平均传输次数
     * @return
     */
    @RequestMapping("/thresholdLSL")
    public String computeTBAboutLSLRINCWithThreshold() {
        computePerformanceService.computeLSLRINCTBWithThreshold(number, packetNumber, interval, packetLoss, times);
        return "计算完毕！";
    }

    /**
     * 计算LSLRINC的TTL变化下——平均传输次数
     * @return
     */
    @RequestMapping("/ttlLSL")
    public String computeTBWithLSLTTLChange() {
        computePerformanceService.computeLSLRINCWithTTL(number, packetNumber, interval, packetLoss, times);
        return "计算完毕！";
    }


    /**
     * 计算LSLRINC 与ARQ、NCBHC方法的信令效率比较
     * @return
     */
    @RequestMapping("/signalLoss")
    public String computeSignalWithNumberChange() {
        computePerformanceService.computeSignalLossWithNumber(number, packetNumber, interval, packetLoss, times);
        return "计算完毕！";
    }
}







