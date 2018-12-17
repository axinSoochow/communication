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
     * 计算的总时延
     */
    private int delay;

    /**
     * ttl超时次数
     */
    private int ttlTimes;

    /**
     * 缓存溢出次数
     */
    private int cacheOverflow;

}
