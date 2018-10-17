package com.axin.communication.tools.compute;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class NCWBRTest {


    @Test
    public void getAveBandwidth() {
    }

    @Test
    public void getCodePacket() {
        NCWBR ncwbr = new NCWBR();

        int[][] MPEM = NetworkCodeTool.creatMPEM(5, 5, 0.5);
        NetworkCodeTool.printMPEM(MPEM);

        int[] res = ncwbr.getCodePacket(MPEM);
        System.out.println(Arrays.toString(res));
    }
}