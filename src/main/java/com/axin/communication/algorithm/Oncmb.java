package com.axin.communication.algorithm;


import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import com.sun.xml.internal.ws.api.model.MEP;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 多组合分组广播传输——《基于机会式网络编码的高效广播传输算法》
 * oncmb算法要求接收点具有缓存功能，其算法步骤和ncwbr算法一样，解码步骤不同
 *
 * @author Axin
 * @date 2018-11-1
 */
@Component("oncmb")
public class Oncmb implements NetworkCode {

    NetworkCode ncwbr = new Ncwbr();


    @Override
    public int[] getCodePacket(int[][] MPEM) {
        return ncwbr.getCodePacket(MPEM);
    }

}
