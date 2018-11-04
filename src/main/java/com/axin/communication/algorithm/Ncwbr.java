package com.axin.communication.algorithm;

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
