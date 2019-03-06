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
        String str = "1.52,1.44,1.4,1.37,1.37,1.36,1.35,1.34,1.34,1.33";
        MatrixTools.StringToData(str);
    }
}