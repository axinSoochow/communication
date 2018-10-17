package com.axin.communication.tools;

import com.axin.communication.tools.common.RandomTools;
import org.junit.Test;

import java.util.Arrays;

public class RandomToolsTest {
    @Test
    public void getRadomDouble() {
        double[] test = RandomTools.getRadomDouble(0, 10, 2, 10);
        System.out.println(Arrays.toString(test));
    }

}