package com.axin.communication.algorithm;


import com.axin.communication.domain.TaskResult;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 配备双接口的网络编码场景Network coding for dual interface,NCDI</br>
 * 基于簇头缓存的网络编码算法Network coding based on cluster heads cache
 *
 * @author Axin
 * @date 18-11-2
 */
public class HcdiNcBsc {
    /**
     * @param numebr
     * @param packetNumber
     * @param packetLoss
     * @param originalPacket 初始发包数
     * @param timeToLive     TTL
     * @param cacheThreshold 缓存阈值
     * @param nextPacket     下一组发包数
     * @param promote        重传后减少丢包率百分比
     * @return 平均传输带宽和重传时延
     */
    public TaskResult getBandWithAndDelay(int numebr, int packetNumber, double packetLoss,
                                          int originalPacket, int timeToLive, int cacheThreshold, int nextPacket,
                                          double promote) {

        if (originalPacket > packetNumber) {
            throw new RuntimeException("The orginalPacket number is no mean");
        }
        //定义：缓存=缓存重传阈值+下一组编码包分组
        int cache = cacheThreshold + nextPacket;
        int delayIndex = numebr + 1;
        //初始化簇头缓存矩阵
        int[][] delayMPEMCache = new int[][]{};
        int[] hashcode;
        //重传次数
        int reNumber = 0;
        //重传时延
        int delay = 0;


        //发一组初始包
        packetNumber -= originalPacket;
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(numebr, originalPacket, packetLoss);

        while (true) {
            //dealyMPEM矩阵剪枝和拼接
            delayMPEM = deleteZero(delayMPEM);
            delayMPEMCache = MatrixTools.jointMatrix(delayMPEMCache, delayMPEM);

            if (!MatrixTools.isZeroMatrix(delayMPEMCache)) {
                //计算缓存中每列hash值
                hashcode = computeHashcode(delayMPEMCache);

                int[] codePacket;
                //寻找TTL大于timeToLive的包-编码重传
                if (delayMPEMCache[delayIndex][0] >= timeToLive) {
                    codePacket = findTTLPacket(delayMPEMCache, hashcode);
                } else {
                    //得到最优编码包
                    codePacket = findOCPacket();

                    //簇头缓存溢出-时延计算
                    //矩阵解码-计算时延-增加簇头缓存时延数量
                }
            }
            //dealyMPEMCache矩阵剪枝
            delayMPEMCache = deleteZero(delayMPEMCache);

            //如果包发完了
            if (packetNumber <= 0) {
                //清空缓存
                break;
            }
            //发下一组包-计算packNumber-计算时延
            packetNumber -= nextPacket;
            if (packetNumber > 0) {
                delayMPEM = NetworkCodeTools.creatDelayMPEM(numebr, nextPacket, packetLoss);
            } else {
                delayMPEM = NetworkCodeTools.creatDelayMPEM(numebr, packetNumber + nextPacket, packetLoss);
            }
        }
        return null;
    }

    /**
     * 时延到达阈值，达到时延的包必须加入编码包，贪婪得到OCP，或者SCP
     *
     * @param delayMPEMCache
     * @param hashcode
     * @return
     */
    private int[] findTTLPacket(int[][] delayMPEMCache, int[] hashcode) {
        int number = delayMPEMCache.length - 1;
        int m = delayMPEMCache[0].length;

        List<Integer> code = new ArrayList<>();
        code.add(0);

        int fullCode = (int) Math.pow(2, number) - 1;
        int nowCode = hashcode[0];
        int index = 0;
        while (nowCode != fullCode && index != m) {
            if (NetworkCodeTools.detection(nowCode, hashcode[index])) {
                code.add(index);
                nowCode += hashcode[index];
            }
            index++;
        }
        int[] codePacket = MatrixTools.listToArray(code);
        return codePacket;
    }


    /**
     * 矩阵剪枝——将数据包传输成功的列删除
     *
     * @param delayMPEM
     * @return
     */
    private int[][] deleteZero(int[][] delayMPEM) {
        int n = delayMPEM.length;
        int m = delayMPEM[0].length;
        List<Integer> target = new ArrayList<>();

        //寻找非零列
        for (int i = 0; i < m; i++) {
            int code = 0;
            int carry = 1;
            for (int j = 0; j < n; j++) {
                code += delayMPEM[j][i];
                carry *= 2;
            }
            if (code != 0) {
                target.add(i);
            }
        }

        if (target.size() < 1) {
            return new int[][]{};
        } else {
            //将非零列赋值到新矩阵
            int[][] res = new int[n][target.size()];
            int resIndex = 0;
            for (int index : target) {
                for (int i = 0; i < n; i++) {
                    res[i][resIndex] = delayMPEM[i][index];
                }
                resIndex++;
            }
            return res;
        }
    }


    /**
     * 计算每个数据包丢失的Hashcode，返回一行hashcode向量
     *
     * @param delayMPEM
     * @return
     */
    private int[] computeHashcode(int[][] delayMPEM) {
        int n = delayMPEM.length - 1;
        int m = delayMPEM[0].length;
        int[] hashcode = new int[m];
        //计算每列的hash值
        for (int i = 0; i < m; i++) {
            int code = 0;
            int carry = 1;
            for (int j = 0; j < n; j++) {
                code += delayMPEM[j][i] * carry;
                carry *= 2;
            }
            hashcode[i] = code;
        }
        return hashcode;
    }
}
