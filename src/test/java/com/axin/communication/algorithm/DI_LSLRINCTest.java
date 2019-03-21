package com.axin.communication.algorithm;

import com.axin.communication.domain.TaskResult;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;

import static org.junit.Assert.*;

public class DI_LSLRINCTest {

    @Test
    public void name1() {
        DI_LSLRINC di_lslrinc = new DI_LSLRINC();
        int packetNumber = 1000;

        TaskResult res = di_lslrinc.getPerformance(7, packetNumber, 0.15, 50, 50);
        double bandWith = NetworkCodeTools.computeDivide(res.getReNumber() + packetNumber, packetNumber);
        System.out.println(bandWith);
        System.out.println(res.getDelay());
        System.out.println(res.getSignalLoss());

    }
}