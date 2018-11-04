package com.axin.communication.algorithm;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 配备双接口的网络编码场景Network coding for dual interface,NCDI</br>
 * 高效编码算法High-efficiency Network coding
 * @author Axin
 * @date 18-11-2
 */
public class NcdiHeNc implements NetworkCode {

    @Qualifier("oncsb")
    NetworkCode oncsb;
    @Override
    public int[] getCodePacket(int[][] MPEM) {
        return oncsb.getCodePacket(MPEM);
    }
}
