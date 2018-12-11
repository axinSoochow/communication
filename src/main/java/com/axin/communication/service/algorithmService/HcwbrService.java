package com.axin.communication.service.algorithmService;


import com.axin.communication.algorithm.NetworkCode;
import com.axin.communication.service.ComputeTBService;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.springframework.beans.factory.annotation.Qualifier;

public class HcwbrService implements ComputeTBService {

    @Qualifier("hcwbr")
    private NetworkCode hcwbr;

    @Override
    public double computeTB(int number, int packetNumber, int interval, double packetLoss, int times) {
        double bandWith = 0;
        for (int i = 0; i < times; i++) {
            bandWith += NetworkCodeTools.getCommonBandwidth(hcwbr, number, packetNumber, interval, packetLoss, 1);
        }
        return NetworkCodeTools.computeDivide(bandWith, times);
    }
}
