package com.axin.communication.algorithm;

import com.axin.communication.CommunicationApplicationTests;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.util.Arrays;


public class HcdiNcBscTest extends CommunicationApplicationTests{
    @Test
    public void name() {
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(4, 7, 0.2);
        MatrixTools.printMatrix(delayMPEM);
//        int[][] deleteZero= HcdiNcBsc.deleteZero(delayMPEM);
//        MatrixTools.printMatrix(deleteZero);
    }

    @Test
    public void findTTLPacket() {
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(4, 7, 0.2);
        MatrixTools.printMatrix(delayMPEM);
//        int[] hashcode = HcdiNcBsc.computeHashcode(delayMPEM);
//        System.out.println(Arrays.toString(hashcode));

//        int[] codePacket = HcdiNcBsc.findTTLPacket(delayMPEM, hashcode);

//        System.out.println(Arrays.toString(codePacket));
    }
}