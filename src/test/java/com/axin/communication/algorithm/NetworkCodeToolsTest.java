package com.axin.communication.algorithm;

import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.junit.Test;


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
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(5, 10, 0.4);
        MatrixTools.printMatrix(delayMPEM);
    }
}