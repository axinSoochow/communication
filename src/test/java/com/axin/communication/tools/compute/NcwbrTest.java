package com.axin.communication.tools.compute;

import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;

public class NcwbrTest {


    @Test
    public void getAveBandwidth() {
        double res = Ncwbr.getAveBandwidth(4, 5, 5, 0.2, 1);
        System.out.println(res);
    }

    @Test
    public void getCodePacket() {

        int[][] MPEM = NetworkCodeTools.creatMPEM(5, 5, 0.5);
        MatrixTools.printMatrix(MPEM);

//        int[] res = ncwbr.getCodePacket(MPEM);
//        System.out.println(Arrays.toString(res));
    }
}