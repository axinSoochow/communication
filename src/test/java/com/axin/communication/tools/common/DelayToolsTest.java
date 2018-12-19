package com.axin.communication.tools.common;

import org.junit.Test;

public class DelayToolsTest {

    @Test
    public void addDelay() {
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(5, 5, 0.5);
        MatrixTools.printMatrix(delayMPEM);

        DelayTools.addDelay(delayMPEM);
        MatrixTools.printMatrix(delayMPEM);

    }

    @Test
    public void copy() {
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(7, 1000, 0.3);
        MatrixTools.printMatrix(delayMPEM);
        int[][] MPEM = DelayTools.delayMPEM2MPEM(delayMPEM);
        MatrixTools.printMatrix(MPEM);
    }

}