package com.axin.communication.service.algorithmService;

import com.axin.communication.algorithm.DI_LSLRINC;
import com.axin.communication.domain.TaskResult;
import com.axin.communication.service.ComputeDelayService;
import com.axin.communication.service.ComputeSignalService;
import com.axin.communication.service.ComputeTBService;
import com.axin.communication.tools.common.NetworkCodeTools;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service("LSLRINCService")
public class LSLRINCService implements ComputeTBService, ComputeDelayService, ComputeSignalService {

    @Autowired
    DI_LSLRINC lslrinc;

    @Value("${networkcode.lslrinc.threshold}")
    private int threshold;

    @Value("${networkcode.lslrinc.timeToLive}")
    private int timeToLive;

    @Override
    public double computeTB(int number, int packetNumber, int interval, double packetLoss, int times) {
        double aveRenumber=0;
//        log.info("阈值为{},TTL为{}", threshold, timeToLive);
        for (int i = 0; i < times; i++) {
            TaskResult result = lslrinc.getPerformance(number, packetNumber, packetLoss, threshold, timeToLive);
            aveRenumber += result.getReNumber();
        }
        aveRenumber = NetworkCodeTools.computeDivide(aveRenumber, times);
        aveRenumber = NetworkCodeTools.computeDivide(aveRenumber + packetNumber, packetNumber);
        return aveRenumber;
    }

    @Override
    public double computeDelay(int number, int packetNumber, int interval, double packetLoss, int times) {
        double aveDelay =0;
        for (int i = 0; i < times; i++) {
            TaskResult result = lslrinc.getPerformance(number, packetNumber, packetLoss, threshold, timeToLive);
            aveDelay += result.getDelay();
        }
        aveDelay = NetworkCodeTools.computeDivide(aveDelay, times);
        return aveDelay;
    }

    @Override
    public double computeSignalLoss(int number, int packetNumber, double packetLoss, int times) {
        double signalNumber=0;
        for (int i = 0; i < times; i++) {
            TaskResult result = lslrinc.getPerformance(number, packetNumber, packetLoss, threshold, timeToLive);
            signalNumber += result.getSignalLoss();
        }
        signalNumber = NetworkCodeTools.computeDivide(signalNumber, times);
        return signalNumber;
    }

}
