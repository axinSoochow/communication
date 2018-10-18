package com.axin.communication.tools.compute;

import org.junit.Test;


public class NcwbrTest {

    @Test
    public void getAveBandwidth() {
        double res = Ncwbr.getAveBandwidth(4, 50, 50, 0.2, 1);
        System.out.println(res);
    }

    @Test
    public void getCodePacket() {
    }
}