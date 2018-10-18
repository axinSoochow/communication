package com.axin.communication.tools.compute;

import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;

import java.util.Arrays;

public class NetworkCodeToolsTest {
    @Test
    public void creatMPEM() {
        double packetLoss = 0.5;
        int[][] test = NetworkCodeTools.creatMPEM(5000, 5000, packetLoss);
        int one = 0;
        int zero = 0;
        double packageLossReal = 0;
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[0].length; j++) {
                int temp =  test[i][j] == 1 ? one++ : zero++;
//                System.out.print(test[i][j]+" ");
            }
//            System.out.println();
        }

        Double sum = new Double(one + zero);
        packageLossReal = one / sum;
        System.out.println("期望丢包率："+packetLoss);
        System.out.println("实际丢包率："+packageLossReal);
    }

    @Test
    public void decodeProcess() {
        double packetLoss = 0.5;
        int[][] MPEM = NetworkCodeTools.creatMPEM(5,5,packetLoss);
        MatrixTools.printMatrix(MPEM);
        int[] codePacket = Ncwbr.getCodePacket(MPEM);
        System.out.println("重传包为：");
        System.out.println(Arrays.toString(codePacket));
        MPEM = NetworkCodeTools.decodeProcess(MPEM, codePacket, packetLoss, 1);
        System.out.println("重传一次后的MPEM矩阵");
        MatrixTools.printMatrix(MPEM);
    }
}