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
}