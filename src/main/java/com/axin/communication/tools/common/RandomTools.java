package com.axin.communication.tools.common;

import java.math.BigDecimal;

/**
 * @author Axin
 */
public class RandomTools {
    /**
     * @param start
     * @param end
     * @param retain 保留几位小数
     * @param sum    生成多少个数
     * @return
     */
    public static double[] getRadomDouble(int start, int end, int retain, int sum) {
        double[] res = new double[sum];

        for (int i = 0; i < sum; i++) {
            BigDecimal db = new BigDecimal(Math.random() * (end - start) + start);
            //四舍五入
            res[i] = db.setScale(retain, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return res;
    }
}
