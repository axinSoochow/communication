package com.axin.communication.service.algorithmService;

import com.axin.communication.algorithm.NetworkCode;
import com.axin.communication.service.ComputeSignalService;
import com.axin.communication.service.ComputeTBService;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("arqService")
public class ArqService implements ComputeTBService, ComputeSignalService {

    @Autowired
    private NetworkCode arq;


    @Value("${networkcode.promote}")
    private double promote;

    @Override
    public double computeTB(int number, int packetNumber, int interval, double packetLoss, int times) {

        double bandWith = 0;
        for (int i = 0; i < times; i++) {
            bandWith += NetworkCodeTools.getCommonBandwidth(arq, number, packetNumber, interval, packetLoss, promote);
        }
        double res = NetworkCodeTools.computeDivide(bandWith, times);
        return res;
    }

    @Override
    public double computeSignalLoss(int number, int packetNumber, double packetLoss, int times) {
        double signalLoss = 0;
        for (int i = 0; i < times; i++) {
            int[][] MPEM = NetworkCodeTools.creatMPEM(number, packetNumber, packetLoss);
            signalLoss += getARQSignalLoss(MPEM,packetLoss);
        }
        return NetworkCodeTools.computeDivide(signalLoss, times);
    }

    /**
     * 返回系统信令效率
     * @param MPEM
     * @return
     */
    private double getARQSignalLoss(int[][] MPEM,double packetLoss) {
        //终端总信令消耗数
        int signalNumber = computeLossPacketNumber(MPEM,packetLoss);
        //丢失包数
        int lossPacketNumber = computelossPacket(MPEM);
        return NetworkCodeTools.computeDivide(lossPacketNumber,signalNumber);
    }

    /**
     * 计算矩阵所有的丢包数
     * @param MPEM
     * @return
     */
    private int computeLossPacketNumber(int[][] MPEM,double packetLoss) {
        int row = MPEM.length;
        int col = MPEM[0].length;
        int res = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (MPEM[i][j] == 1) {
                    res++;
                }
            }
        }
        res = (int)(res * (1 + packetLoss));
        return res;
    }

    /**
     * 统计丢包个数
     */
    private int computelossPacket(int[][] delayMPEM) {
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
}
