package com.axin.communication.algorithm;

import com.axin.communication.BaseTest;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;


public class HwnerTest extends BaseTest {

    @Autowired
    NetworkCode hwner;

    @Test
    public void name() {
        double res = NetworkCodeTools.getCommonBandwidth(hwner, 5, 50, 5, 0.3, 1);
        System.out.println(res);
    }

    @Test
    public void getPacket() {
        int[][] MPEM = NetworkCodeTools.creatMPEM(5, 7, 0.4);
        MatrixTools.printMatrix(MPEM);
        System.out.println();
        int[] codePacket = hwner.getCodePacket(MPEM);
        System.out.println(Arrays.toString(codePacket));
    }
}