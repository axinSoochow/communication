package com.axin.communication.tools;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class RandomToolsTest {
    @Test
    public void test() {

        double[] test = RandomTools.getRadomDouble(0, 10, 2, 10);
        System.out.println(Arrays.toString(test));
    }

}