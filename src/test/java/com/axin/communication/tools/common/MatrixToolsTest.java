package com.axin.communication.tools.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatrixToolsTest {

    @Test
    public void name() {
        int[][] b = new int[][]{};
        int[][] a = NetworkCodeTools.creatDelayMPEM(4, 5, 0.4);
        MatrixTools.printMatrix(a);
        MatrixTools.printMatrix(b);
        int[][] res = MatrixTools.jointMatrix(a, b);
        MatrixTools.printMatrix(res);
    }

    @Test
    public void name2() {
        String str = "36.35,30.96,26.38,22.76,19.53,17.31,15.19,13.74,12.35,11.21,10.39,9.53,8.91,8.33,7.79,7.35,6.93,6.57,6.24";
        MatrixTools.StringToData(str);
    }
}