package com.axin.communication.service.algorithmService;

import com.axin.communication.algorithm.NetworkCode;
import com.axin.communication.service.ComputeDelayService;
import com.axin.communication.service.ComputeTBService;
import com.axin.communication.tools.common.DelayTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("hencService")
@Slf4j
public class Hcdi_hencService implements ComputeTBService, ComputeDelayService {

    @Autowired
    @Qualifier("hcdi-henc")
    private NetworkCode hcdihenc;

    @Value("${networkcode.promote}")
    private double promote;

    @Override
    public double computeTB(int number, int packetNumber, int interval, double packetLoss, int times) {
        double bandWith = 0;
        for (int i = 0; i < times; i++) {
            //采用次方法计算则传输时间间隔为总传输包数量大小一致
            bandWith += NetworkCodeTools
                .getCommonBandwidth(hcdihenc, number, packetNumber, packetNumber, packetLoss,
                    promote);
        }
        return NetworkCodeTools.computeDivide(bandWith, times);
    }

    @Override
    public double computeDelay(int number, int packetNumber, int interval, double packetLoss, int times) {
        int sum = 0;

        for (int i = 0; i < times; i++) {
            sum += getDelay(number, packetNumber, interval, packetLoss);
        }
        return NetworkCodeTools.computeDivide(sum, times);
    }

    /**
     * 计算传输成功后的时延
     *
     * @param number
     * @param packetNumber
     * @param interval
     * @param packetLoss
     * @return
     */
    private int getDelay(int number, int packetNumber, int interval, double packetLoss) {

        int delay = 0;
        int[][] MPEM;
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(number, packetNumber, packetLoss);
        delayMPEM = DelayTools.deleteZero(delayMPEM);

        while (NetworkCodeTools.getRestPacket(delayMPEM) != 0) {

            MPEM = DelayTools.delayMPEM2MPEM(delayMPEM);
            int[] codePacket = hcdihenc.getCodePacket(MPEM);
            delayMPEM = NetworkCodeTools
                .decodeProcess(delayMPEM, codePacket, packetLoss, promote, true);
            delay += DelayTools.computeDelay(delayMPEM);
            //增加时延
            DelayTools.addDelay(delayMPEM);
            delayMPEM = DelayTools.deleteZero(delayMPEM);
        }
        return delay;
    }

}
