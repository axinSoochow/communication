package com.axin.communication.algorithm;

import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;


public class NetworkCodeToolsTest {
    @Test
    public void creatMPEM() {
        double packetLoss = 0.5;
        int[][] test = NetworkCodeTools.creatDelayMPEM(5,5,  packetLoss);
        MatrixTools.printMatrix(test);
    }

    @Test
    public void decodeProcess() {
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(5, 10, 0.4);
        MatrixTools.printMatrix(delayMPEM);
    }

    @Test
    public void computeBandWith() {
        int a = 100;
        int b = 1000;
    }
}