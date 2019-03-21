package com.axin.communication.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResult {

    /**
     * 重传次数
     */
    private int reNumber;
    /**
     * 平均传输时延
     */
    private double delay;

    /**
     * 平均信令损耗
     */
    private double signalLoss;

    /**
     * 缓存溢出次数
     */
    private int cacheOverflow;

}
