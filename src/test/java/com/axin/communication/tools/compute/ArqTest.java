package com.axin.communication.tools.compute;

import org.junit.Test;

public class ArqTest {
    @Test
    public void getAveBandwidth() {
        double res = Arq.getAveBandwidth(4, 50, 50, 0.2, 1);
        System.out.println(res);
    }
}

