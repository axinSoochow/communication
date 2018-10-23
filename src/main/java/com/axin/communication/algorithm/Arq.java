package com.axin.communication.algorithm;

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