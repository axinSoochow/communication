package com.axin.communication.algorithm;


import com.axin.communication.domain.TaskResult;
import com.axin.communication.tools.common.DelayTools;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 配备双接口的网络编码场景Network coding for dual interface,NCDI</br>
 * 基于簇头缓存的网络编码算法Network coding based on cluster heads cache
 *
 * @author Axin
 * @date 18-11-2
 */
@Slf4j
@Service
public class NcdiNcBhc {

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

        //0.数据校验
        Boolean pass = verifyParams(numebr, packetNumber, packetLoss, originalPacket, timeToLive,
                cacheThreshold, nextPacket, promote);
        //校验不通过
        if (pass == false) {
            return null;
        }

        //定义：缓存容量=缓存重传阈值+下一组编码包分组cacheThreshold + nextPacket
        int cache;
        int delayIndex = numebr;
        //初始化簇头缓存矩阵
        int[][] delayMPEMCache = new int[][]{};
        int[] hashcode;
        //重传次数
        int reNumber = 0;
        //重传时延
        double delay = 0;
        //丢包总数
        int lossPacket = originalPacket;
        //缓存溢出次数
        int cacheOverflow = 0;

        //发一组初始包
        packetNumber -= originalPacket;
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(numebr, originalPacket, packetLoss);
//        log.info("发一组初始包，数量为{}个", originalPacket);

        while (true) {

            //1.dealyMPEM矩阵剪枝和拼接-将接收成功的数据包删除
            delayMPEMCache = MatrixTools.jointMatrix(delayMPEMCache, delayMPEM);
            delayMPEMCache = deleteZero(delayMPEMCache);

            //2. 缓存中的矩阵不是null矩阵
            if (!MatrixTools.isZeroMatrix(delayMPEMCache)) {
                //2.1计算缓存中每列hash值
                hashcode = computeHashcode(delayMPEMCache);

                //2.2有TTL大于timeToLive的包->编码进行重传
                int[] codePacket;
                if (delayMPEMCache[delayIndex][0] >= timeToLive) {

                    codePacket = findTTLPacket(delayMPEMCache, hashcode);
//                    log.info("获得TTL编码包");
//                    System.out.println(Arrays.toString(codePacket));
                } else {
                    //2.3尝试获得到最优编码包
                    codePacket = findOCPacket(delayMPEMCache, hashcode);
//                    log.info("获得最优编码包{}", Arrays.toString(codePacket));
                }

                //2.3矩阵解码
                if (codePacket != null) {
                    delayMPEMCache = NetworkCodeTools
                            .decodeProcess(delayMPEMCache, codePacket, packetLoss, promote, true);
                    //有解码包->重传次数++
                    reNumber++;
                }

                //2.4解码后-计算时延-将所有零列的时延累加
                //dealyMPEMCache矩阵剪枝
                delay += DelayTools.computeDelay(delayMPEMCache);
                delayMPEMCache = deleteZero(delayMPEMCache);

                //计算缓存
                try {
                    cache = delayMPEMCache[0].length;
                } catch (Exception e) {
                    cache = 0;
                }
                //2.5簇头缓存超过阈值->SCP编码重传并计算时延计算直到小于阈值
                while (cache > cacheThreshold) {
//                    log.info("——故缓存超过阈值，发送SCP包！");
                    hashcode = computeHashcode(delayMPEMCache);
                    codePacket = findTTLPacket(delayMPEMCache, hashcode);
                    //矩阵解码
                    delayMPEMCache = NetworkCodeTools
                            .decodeProcess(delayMPEMCache, codePacket, packetLoss, promote, true);
                    //统计延时
                    delay += DelayTools.computeDelay(delayMPEMCache);
                    //矩阵剪枝并重新计算缓存大小
                    delayMPEMCache = deleteZero(delayMPEMCache);
                    cache = delayMPEMCache[0].length;

                    //经过一个时隙，增加时间
                    DelayTools.addDelay(delayMPEMCache);

                    //缓存溢出次数+1;重传次数+1
                    cacheOverflow++;
                    reNumber++;
                }
                //2.6增加簇头缓存时延
                DelayTools.addDelay(delayMPEMCache);
            }

            //3. 检查待发送数据包数
            if (packetNumber <= 0) {
                //清空缓存-计算时延
                int codePacket[];
                while (NetworkCodeTools.getRestPacket(delayMPEMCache) != 0) {
                    delayMPEMCache = deleteZero(delayMPEMCache);
                    hashcode = computeHashcode(delayMPEMCache);
                    codePacket = findTTLPacket(delayMPEMCache, hashcode);
                    //统计时延
                    delayMPEMCache = NetworkCodeTools
                            .decodeProcess(delayMPEMCache, codePacket, packetLoss, promote, true);

                    delay += DelayTools.computeDelay(delayMPEMCache);
                    delayMPEMCache = deleteZero(delayMPEMCache);
                    //重传次数+1
                    reNumber++;
                }
                //跳出循环
                break;
            }

            //发下一组包
            packetNumber -= nextPacket;
            if (packetNumber > 0) {
                delayMPEM = NetworkCodeTools.creatDelayMPEM(numebr, nextPacket, packetLoss);
                //记录丢包数
                lossPacket += computelossPacket(delayMPEM);
            } else {
                delayMPEM = NetworkCodeTools.creatDelayMPEM(numebr, packetNumber + nextPacket, packetLoss);
            }
        }
        delay = NetworkCodeTools.computeDivide(delay, lossPacket);
        return new TaskResult(reNumber, delay, 0, cacheOverflow);
    }

    /**
     * 传参校验
     */
    private boolean verifyParams(int numebr, int packetNumber, double packetLoss,
                                 int originalPacket, int timeToLive, int cacheThreshold, int nextPacket,
                                 double promote) {
        if (numebr <= 0) {
            log.error("终端数量不能小于0！");
            return false;
        }
        if (packetNumber < 0) {
            log.error("传输包数量不能小于0！");
            return false;
        }
        if (packetLoss >= 1 || packetLoss < 0) {
            log.error("丢包率无意义，输入范围应为0-1，不能为1");
            return false;
        }
        if (originalPacket > packetNumber) {
            log.error("初始发包数不能大于总发包数！");
            return false;
        }
        if (cacheThreshold <= 0) {
            log.error("缓存阈值不能为0一下！");
            return false;
        }

        return true;
    }

    /**
     * 统计丢包个数
     */
    public int computelossPacket(int[][] delayMPEM) {
        int res = 0;
        int row = delayMPEM.length;
        int col = delayMPEM[0].length;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                if (delayMPEM[j][i] == 1) {
                    res++;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 按照时延顺序进行贪婪得到OCP or SCP
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
     * 以时延为优先进行贪婪匹配OCP，若未匹配成功则返回null
     */
    private int[] findOCPacket(int[][] delayMPEMCache, int[] hashcode) {
        int number = delayMPEMCache.length - 1;
        int m = delayMPEMCache[0].length;

        List<Integer> code = new ArrayList<>();
        int nowCode = 0;
        int fullCode = (int) Math.pow(2, number) - 1;
        for (int i = 0; i < m; i++) {
            code = new ArrayList<>();
            code.add(i);
            nowCode = hashcode[i];

            int index = 0;
            while (nowCode != fullCode && index != m) {
                if (NetworkCodeTools.detection(nowCode, hashcode[index])) {
                    code.add(index);
                    nowCode += hashcode[index];
                }
                index++;
            }
            if (nowCode == fullCode) {
                break;
            }
        }
        if (nowCode == fullCode) {
            return MatrixTools.listToArray(code);
        } else {
            return null;
        }
    }


    /**
     * 矩阵剪枝——将数据包传输成功的列删除 如果全成功则传输一个大小为0矩阵
     */
    private int[][] deleteZero(int[][] delayMPEM) {
        int n = delayMPEM.length;
        int m = 0;
        try {
            m = delayMPEM[0].length;
        } catch (Exception e) {
            return new int[][]{};
        }
        List<Integer> target = new ArrayList<>();

        //寻找非零列
        for (int i = 0; i < m; i++) {
            int code = 0;
            for (int j = 0; j < n - 1; j++) {
                code += delayMPEM[j][i];
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
