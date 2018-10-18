package com.axin.communication.tools.compute;

import org.junit.Test;

public class ArqTest {
    @Test
    public void getAveBandwidth() {
        double res = Arq.getAveBandwidth(5, 5, 5, 0.5, 1);
        System.out.println(res);
    }


}