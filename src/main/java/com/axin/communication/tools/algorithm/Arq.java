package com.axin.communication.tools.algorithm;

import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.springframework.stereotype.Component;

/**
 * 传统的ARQ(Automatic Repeat Request)
 *
 * @author Axin
 * @date 18-10-17
 */
@Component("arq")
public class Arq implements NetworkCode{
    /**
     * @param number
     * @param packatNumber
     * @param interval
     * @param packetLoss
     * @param promote
     * @return arq的传输带宽消耗
     */
    @Override
    public double getAveBandwidth(int number, int packatNumber, int interval, double packetLoss, double promote) {
        packetLoss = packetLoss > 0 ? packetLoss : 0;
        //重传次数
        int reNumber = 0;
        //剩余传输包数
        int restPacketNumber = packatNumber;
        int[][] MPEM;
        while (restPacketNumber > 0) {
            restPacketNumber -= interval;
            MPEM = NetworkCodeTools.multicastProcess(restPacketNumber, number, interval, packetLoss);
            while (!MatrixTools.isZeroMatrix(MPEM)) {
                int[] codePacket = getCodePacket(MPEM);
                reNumber++;
                MPEM = NetworkCodeTools.decodeProcess(MPEM, codePacket, packetLoss, promote);
            }
        }

        double aveBandwidth = NetworkCodeTools.computeAveBandwidth(reNumber, packatNumber);
        return aveBandwidth;
    }

    /**
     * 传统ARQ的重传包
     *
     * @param MPEM
     * @return
     */
    @Override
    public int[] getCodePacket(int[][] MPEM) {
        for (int i = 0; i < MPEM.length; i++) {
            for (int j = 0; j < MPEM[0].length; j++) {
                if (MPEM[i][j] == 1) {
                    return new int[]{j};
                }
            }
        }
        return new int[]{};
    }
}