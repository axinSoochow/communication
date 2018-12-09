package com.axin.communication.algorithm;

import com.axin.communication.domain.TaskResult;

/**
 * @author Axin
 * @date 18-10-20
 */
public interface NetworkCode {
    /**
     * 获得编码包
     * @param MPEM
     * @return
     */
    int[] getCodePacket(int[][] MPEM);



}

