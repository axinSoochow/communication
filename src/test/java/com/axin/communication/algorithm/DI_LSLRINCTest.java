package com.axin.communication.algorithm;

import com.axin.communication.BaseTest;
import com.axin.communication.domain.TaskResult;
import com.axin.communication.service.ComputeDelayService;
import com.axin.communication.service.ComputeSignalService;
import com.axin.communication.service.ComputeTBService;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DI_LSLRINCTest extends BaseTest {

    @Autowired
    @Qualifier("LSLRINCService")
    private ComputeTBService LSLRINCService;

    @Autowired
    @Qualifier("LSLRINCService")
    private ComputeDelayService LSLDelayService;

    @Autowired
    @Qualifier("LSLRINCService")
    private ComputeSignalService LSLSignalService;

    @Test
    public void name1() {
        DI_LSLRINC di_lslrinc = new DI_LSLRINC();
        int packetNumber = 1000;

        TaskResult res = di_lslrinc.getPerformance(7, packetNumber, 0.15, 10, 10);
        double bandWith = NetworkCodeTools.computeDivide(res.getReNumber() + packetNumber, packetNumber);
        System.out.println(bandWith);
        System.out.println(res.getDelay());
        System.out.println(res.getSignalLoss());

    }

    @Test
    public void name2() {
        double res = LSLRINCService.computeTB(7, 100, 10, 0.2, 10);
        System.out.println("平均传输次数"+res);

        double delay = LSLDelayService.computeDelay(7, 100, 10, 0.2, 10);
        System.out.println("系统信令效率" + delay);

        double signalLoss = LSLSignalService.computeSignalLoss(7, 100, 0.2, 10);
        System.out.println("系统信令效率" + signalLoss);
    }
}