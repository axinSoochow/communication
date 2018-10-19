package com.axin.communication.tools.algorithm;

import com.axin.communication.CommunicationApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ArqTest extends CommunicationApplicationTests {

    @Autowired
    NetworkCode arq;
    @Test
    public void getAveBandwidth() {
        double res = arq.getAveBandwidth(5,5,5,0.5,1);
        System.out.println(res);
    }
}

