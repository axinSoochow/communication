package com.axin.communication.algorithm;

import com.axin.communication.BaseTest;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class NcwbrTest extends BaseTest {

    @Autowired
    NetworkCode ncwbr;

    @Test
    public void getAveBandwidth() {
        double res = NetworkCodeTools.getCommonBandwidth(ncwbr,5, 50, 5, 0.5, 1);
        System.out.println(res);
    }

    @Test
    public void getCodePacket() {
    }
}