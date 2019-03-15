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
        String str = "1.46,1.38,1.35,1.33,1.32,1.31,1.3,1.3,1.29,1.29,1.29,1.29,1.28,1.28,1.28,1.28,1.28,1.28,1.28,1.28";
        MatrixTools.StringToData(str);
    }
}