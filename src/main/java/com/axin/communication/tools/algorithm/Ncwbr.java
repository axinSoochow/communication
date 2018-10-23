package com.axin.communication.tools.algorithm;

import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>肖潇团队提出的一种基于网络编码的无线网络广播重传策略<br/>
 * network coding wireless broadcasting retransmission<br>
 * 2009通信学报</p>
 *
 * @author Axin
 * @date 18-10-17
 */
@Component("ncwbr")
    public class Ncwbr implements NetworkCode {

    @Override
    public double getAveBandwidth(int number, int packetNumber, int interval, double packetLoss, double promote) {
        //重传次数
        int reNumber = 0;
        //剩余传输包数
        int restPacketNumber = packetNumber;
        int[][] MPEM;
        while (restPacketNumber >0) {
            restPacketNumber -= interval;
            //多播数据包得到MPEM矩阵
            MPEM = NetworkCodeTools.multicastProcess(restPacketNumber, number, interval, packetLoss);
            //开始重传
            while (!MatrixTools.isZeroMatrix(MPEM)) {
                //得到编码包
                int[] codePacket = getCodePacket(MPEM);
                reNumber++;
                MPEM = NetworkCodeTools.decodeProcess(MPEM, codePacket, packetLoss, promote);
            }
        }
        //精确计算保留两位小数
        double aveBandwidth = NetworkCodeTools.computeAveBandwidth(reNumber, packetNumber);

        return aveBandwidth;
    }

    /**
     * @param MPEM
     * @return 利用NCWBR方法获得编码数据包
     */
    @Override
    public int[] getCodePacket(int[][] MPEM) {
        Set<Integer> codePacket = new HashSet<>();
        for (int i = 0; i < MPEM.length; i++) {
            for (int j = 0; j < MPEM[0].length; j++) {
                if (MPEM[i][j] == 1) {
                    codePacket.add(j);
                    break;
                }
            }
        }
        int[] res = new int[codePacket.size()];
        int index = 0;
        for (int packet : codePacket) {
            res[index++] = packet;
        }
        return res;
    }
}
