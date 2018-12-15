package com.axin.communication.service.algorithmService;

import com.axin.communication.algorithm.NetworkCode;
import com.axin.communication.service.ComputeTBService;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("hencService")
public class Hcdi_hencService implements ComputeTBService {

    @Autowired
    @Qualifier("hcdi-henc")
    private NetworkCode hcdihenc;

    @Override
    public double computeTB(int number, int packetNumber, int interval, double packetLoss, int times) {
        double bandWith = 0;
        for (int i = 0; i < times; i++) {
            //采用次方法计算则传输时间间隔为总传输包数量大小一致
            bandWith += NetworkCodeTools.getCommonBandwidth(hcdihenc, number, packetNumber, packetNumber, packetLoss, 1);
        }
        return NetworkCodeTools.computeDivide(bandWith, times);
    }
}
