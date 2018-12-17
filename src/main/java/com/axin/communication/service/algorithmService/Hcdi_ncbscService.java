package com.axin.communication.service.algorithmService;

import com.axin.communication.algorithm.HcdiNcBsc;
import com.axin.communication.domain.TaskResult;
import com.axin.communication.service.ComputeTBService;
import com.axin.communication.tools.common.NetworkCodeTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ncbscService")
public class Hcdi_ncbscService implements ComputeTBService, InitializingBean {

    /**
     * 初始发包数
     */
    private int originalPacket;

    /**
     * TTL
     */
    private int timeToLive;

    /**
     * 缓存阈值
     */
    private int cacheThreshold;

    /**
     * 下一组发包数
     */
    private int nextPacket;

    @Autowired
    private HcdiNcBsc hcdiNcBsc;


    public Hcdi_ncbscService() {
        hcdiNcBsc = new HcdiNcBsc();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("ncbsc参数：（初始发包数：{}），（TTL：{}），（缓存阈值：{}），（下一组发包数：{}）", originalPacket, timeToLive, cacheThreshold, nextPacket);
    }

    @Override
    public double computeTB(int number, int packetNumber, int interval, double packetLoss, int times) {
        TaskResult result;
        double bandWith = 0;

        for (int i = 0; i < times; i++) {
            if (interval != 0) {
                result = hcdiNcBsc.getBandWithAndDelay(number, packetNumber, packetLoss, originalPacket, timeToLive, interval, nextPacket, 1);
            } else {
                result = hcdiNcBsc.getBandWithAndDelay(number, packetNumber, packetLoss, originalPacket, timeToLive, cacheThreshold, nextPacket, 1);
            }
            bandWith += NetworkCodeTools.computeDivide(result.getReNumber() + packetNumber, packetNumber);
        }
        return NetworkCodeTools.computeDivide(bandWith, times);
    }

    @Value("${networkcode.hcdi.originalPacket}")
    public void setOriginalPacket(int originalPacket) {
        this.originalPacket = originalPacket;
    }

    @Value("${networkcode.hcdi.timeToLive}")
    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Value("${networkcode.hcdi.cacheThreshold}")
    public void setCacheThreshold(int cacheThreshold) {
        this.cacheThreshold = cacheThreshold;
    }

    @Value("${networkcode.hcdi.nextPacket}")
    public void setNextPacket(int nextPacket) {
        this.nextPacket = nextPacket;
    }

}
