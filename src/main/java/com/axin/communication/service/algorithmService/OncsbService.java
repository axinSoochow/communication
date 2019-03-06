package com.axin.communication.service.algorithmService;

import com.axin.communication.algorithm.NetworkCode;
import com.axin.communication.service.ComputeTBService;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("oncsbService")
public class OncsbService implements ComputeTBService {

    @Autowired
    @Qualifier("oncsb")
    private NetworkCode oncsb;

    @Value("${networkcode.promote}")
    private double promote;

    @Override
    public double computeTB(int number, int packetNumber, int interval, double packetLoss, int times) {
        double bandWith = 0;
        for (int i = 0; i < times; i++) {
            bandWith += NetworkCodeTools
                .getCommonBandwidth(oncsb, number, packetNumber, interval, packetLoss, promote);
        }
        return NetworkCodeTools.computeDivide(bandWith, times);
    }
}
