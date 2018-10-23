package com.axin.communication.tools.algorithm;

import com.axin.communication.CommunicationApplicationTests;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;

import static org.junit.Assert.*;

public class OncsbTest extends CommunicationApplicationTests {
    @Autowired
    NetworkCode oncsb;
    @Test
    public void getCodePacket() {
        int[][] MPEM = NetworkCodeTools.creatMPEM(5, 7, 0.2);
        MatrixTools.printMatrix(MPEM);
        System.out.println();
        int[] codePacket = oncsb.getCodePacket(MPEM);
        System.out.println(Arrays.toString(codePacket));
    }

    @Test
    public void computeHashcode() {

    }
}
