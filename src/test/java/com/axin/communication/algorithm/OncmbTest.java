package com.axin.communication.algorithm;

import com.axin.communication.BaseTest;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class OncmbTest extends BaseTest {
    @Autowired
    NetworkCode oncmb;
    @Test
    public void name() {
        double res = NetworkCodeTools.getCacheBandwidth(oncmb, 5, 50, 5, 0.5, 1);
        System.out.println(res);
    }
}