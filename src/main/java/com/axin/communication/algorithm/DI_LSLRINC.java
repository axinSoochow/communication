package com.axin.communication.algorithm;

import com.axin.communication.domain.TaskResult;
import com.axin.communication.tools.common.DelayTools;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Dual Interfaces-Low Signal loss retransmission based on random Linear Network Coding
 * 双接口场景下-基于随机线性网络编码的低信令损耗重传方法
 */
@Service
public class DI_LSLRINC {

    public TaskResult getPerformance(int number, int packetNumber, double packetLoss, int threshold,
                                     int timeToLive) {

        int[][] delayMPEM;
        int[][] nextPacket;
        //重传次数
        int reNumber = 0;
        //丢失包总时延
        int delay = 0;
        //丢包总数
        int lossPacket = 0;
        //信令损耗
        int signalNumber = 0;

        //执行多播重传解码步骤Flag
        boolean decodeFlag = false;

        //1 初始生成MPEM矩阵
        packetNumber--;
        delayMPEM = NetworkCodeTools.creatDelayMPEM(number, 1, packetLoss);
        if (isLossPacket(delayMPEM)) {
            lossPacket++;
        }

        while (true) {
            decodeFlag = false;

            //2.1 找到应编码数是否大于等于阈值threshold
            if (isThreshold(delayMPEM, threshold)) {
                //执行多播编解码步骤
                decodeFlag = true;
            }

            //2.2 TTL是否超时
            delayMPEM = DelayTools.deleteZero(delayMPEM);
            try {
                if (delayMPEM[0].length >= timeToLive) {
                    //执行多播编解码步骤
                    decodeFlag = true;
                }
            } catch (Exception e) {
                decodeFlag = false;
            }

            //3 解码步骤——累计传输时延、重传次数、信令损耗
            if (decodeFlag) {
                Result res = multicastDecoding(delayMPEM, number, packetLoss);
                //累计重传次数
                reNumber += res.getReNumber();
                //累计信令损耗
                signalNumber += res.getSignalNumber();
                //累计时延
                delay += res.getDelay();
                //簇头MPEM矩阵初始化为null矩阵
                delayMPEM = new int[][]{};
            }

            //4.1 数据包传输完毕执行MPEM清理
            if (packetNumber <= 0) {
                //空矩阵检测
                if (MatrixTools.isNullMatrix(delayMPEM)) {
                    break;
                }
                Result res = multicastDecoding(delayMPEM, number, packetLoss);
                //累计重传次数
                reNumber += res.getReNumber();
                //累计信令损耗
                signalNumber += res.getSignalNumber();
                //累计时延
                delay += res.getDelay();
                break;
            }
            //4.2 数据包未传输完毕-生成下一个数据包-增加数据包时延
            DelayTools.addDelay(delayMPEM);
            nextPacket = NetworkCodeTools.creatDelayMPEM(number, 1, packetLoss);
            packetNumber--;
            if (isLossPacket(nextPacket)) {
                lossPacket++;
            }
            delayMPEM = MatrixTools.jointMatrix(delayMPEM, nextPacket);
        }
        // 返回性能参数
//        System.out.println("累计性能参数重传次数" + reNumber + " 累计信令：" + signalNumber + " 累计时延：" + delay);
        double aveDelay = NetworkCodeTools.computeDivide(delay, lossPacket);
        double aveSignalNumber = NetworkCodeTools.computeDivide(lossPacket, signalNumber);
        TaskResult result = new TaskResult();
        result.setReNumber(reNumber);
        result.setDelay(aveDelay);
        result.setSignalLoss(aveSignalNumber);
        return result;
    }

    //多播与接收端解码过程
    private Result multicastDecoding(int[][] delayMPEM, int number, double packetLoss) {
        List<Integer> terminalLossPacket;
        terminalLossPacket = getTerminalLossPacket(delayMPEM);
        List<Integer> reNumberList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            //该接收端需要簇头重传多少次
            int reNumberTemp = 0;
            //该接收端已接收数据包数
            int receive = 0;
            //直到这个接收端收到足够多的包
            while (receive < terminalLossPacket.get(i)) {
                //模拟丢包
                boolean isReceive = Math.random() > packetLoss ? true : false;
                reNumberTemp++;
                if (isReceive) {
                    receive++;
                }
            }
            //该接收端成功恢复丢包-记录该设备需要簇头传输多少次
            reNumberList.add(reNumberTemp);
        }
        //重传次数
        int reNumber = MatrixTools.getMaxFormList(reNumberList);
        //信令损耗
        int signalLoss = number;
        //时延
        int delay = computeDelay(delayMPEM, MatrixTools.getMaxFormList(reNumberList));
        return new Result(reNumber, delay, signalLoss);
    }

    //是否发生丢包
    private boolean isLossPacket(int[][] delayMPEM) {
        for (int i = 0; i < delayMPEM.length - 1; i++) {
            if (delayMPEM[i][0] == 1) {
                return true;
            }
        }
        return false;
    }

    //终端丢包最大数是否达到阈值
    private boolean isThreshold(int[][] delayMPEM, int threshold) {
        if (MatrixTools.isNullMatrix(delayMPEM)) {
            return false;
        }

        List<Integer> terminalLossPacket = getTerminalLossPacket(delayMPEM);
        int max = MatrixTools.getMaxFormList(terminalLossPacket);
        if (max >= threshold) {
            return true;
        }
        return false;
    }

    //得到各个终端的丢包数
    private List<Integer> getTerminalLossPacket(int[][] delayMPEM) {
        List<Integer> res = new ArrayList<>();
        int row = delayMPEM.length - 1;
        int col = delayMPEM[0].length;
        for (int i = 0; i < row; i++) {
            int loss = 0;
            for (int j = 0; j < col; j++) {
                if (delayMPEM[i][j] == 1) {
                    loss++;
                }
            }
            res.add(loss);
        }
        return res;
    }

    //累计丢失包传输时延
    private int computeDelay(int[][] delayMPEM, int reNumber) {
        int delay = 0;
        int row = delayMPEM.length;
        int col = delayMPEM[0].length;
        for (int i = 0; i < col; i++) {
            delay += delayMPEM[row - 1][i];
        }
        delay += reNumber * col;
        return delay;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Result {
        //重传次数
        private int reNumber;
        //时延
        private int delay;
        //信令损耗
        private int signalNumber;
    }
}







