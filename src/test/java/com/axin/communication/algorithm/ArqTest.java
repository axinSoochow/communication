package com.axin.communication.algorithm;

import com.axin.communication.BaseTest;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ArqTest extends BaseTest {

    @Autowired
    NetworkCode arq;
    @Test
    public void getAveBandwidth() {
        double res = NetworkCodeTools.getCommonBandwidth(arq,5,5,5,0.5,1);
        System.out.println(res);
    }
}

