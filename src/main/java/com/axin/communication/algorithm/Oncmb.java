package com.axin.communication.algorithm;


import org.springframework.stereotype.Component;

/**
 * 多组合分组广播传输——《基于机会式网络编码的高效广播传输算法》
 * oncmb算法要求接收点具有缓存功能，其算法步骤和ncwbr算法一样，解码步骤不同
 *
 * @author Axin
 * @date 2018-11-1
 */
@Component("oncmb")
public class Oncmb implements NetworkCode {

    NetworkCode ncwbr = new Ncwbr();


    @Override
    public int[] getCodePacket(int[][] MPEM) {
        return ncwbr.getCodePacket(MPEM);
    }
}
