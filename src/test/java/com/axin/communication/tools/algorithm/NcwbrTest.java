package com.axin.communication.tools.algorithm;

import com.axin.communication.CommunicationApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class NcwbrTest extends CommunicationApplicationTests {

    @Autowired
    NetworkCode ncwbr;

    @Test
    public void getAveBandwidth() {
        double res = ncwbr.getAveBandwidth(5, 5, 5, 0.5, 1);
        System.out.println(res);
    }

    @Test
    public void getCodePacket() {
    }
}