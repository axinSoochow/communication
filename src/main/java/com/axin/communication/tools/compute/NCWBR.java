package com.axin.communication.tools.compute;

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
public class NCWBR {

    /**
     *
     * @param number 接收端个数
     * @param packageNumber 传输数据包数量
     * @param interval 重传时间间隔
     * @param packetLoss 平均链接丢包率
     * @return 传输带宽消耗（平均传输次数）
     */
    public static double getAveBandwidth(int number,int packageNumber,int interval,int packetLoss) {
        //剩余传输包数
        int restPacketNumber = packageNumber;
        while (restPacketNumber != 0) {
            int[][] MPEM;
            restPacketNumber -= interval;
            //多播后得到MPEM矩阵
            if (restPacketNumber >= 0) {
                MPEM = NetworkCodeTool.creatMPEM(interval, number, packetLoss);
            }else {
                MPEM = NetworkCodeTool.creatMPEM(Math.abs(restPacketNumber), number, packetLoss);
            }

        }
        return 0;
    }

    /**
     * @param MPEM
     * @return 网络编码数据包
     */
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
