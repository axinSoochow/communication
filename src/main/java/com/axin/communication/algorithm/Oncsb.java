package com.axin.communication.algorithm;

import com.axin.communication.domain.CodeMap;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 基于ONC的单组合分组广播传输
 * ONCSB
 * -卢翼2012通信学报
 * @author Axin
 * @date 18-10-20
 */
@Component("oncsb")
public class Oncsb implements NetworkCode {

    @Override
    public int[] getCodePacket(int[][] MPEM) {
        if (MatrixTools.isZeroMatrix(MPEM)) {
            return new int[0];
        }
        //map存放每列计算好的hash值
        List<CodeMap> map = new ArrayList<>();
        Set<CodeMap> codeResult = new HashSet<>();

        int number = MPEM.length;
        //计算每列的hash值
        computeHashcode(MPEM, map);

        //取出第一个
        CodeMap first = map.get(0);
        codeResult.add(first);
        int fullCode = (int) Math.pow(2, number) - 1;
        int nowCode = first.getCode();
        int index = 1;

        while (nowCode != fullCode && index != map.size()) {
            CodeMap curCode = map.get(index);
            if (NetworkCodeTools.detection(curCode.getCode(), nowCode)) {
                codeResult.add(curCode);
                nowCode += curCode.getCode();
            }
            index++;
        }
        int[] codePacket = transform(codeResult);
        return codePacket;
    }

    /**
     * 计算每列的hash值放入数据结构中
     *
     * @param MPEM
     * @param map
     */
    private void computeHashcode(int[][] MPEM, List<CodeMap> map) {
        int index = 0;
        //计算每列的hash值
        for (int i = 0; i < MPEM[0].length; i++) {
            int code = 0;
            int carry = 1;
            for (int j = 0; j < MPEM.length; j++) {
                code += MPEM[j][i] * carry;
                carry *= 2;
            }
            //将hash值非零的列加入codeMap中
            if (code != 0) {
                map.add(new CodeMap(code, index));
            }
            index++;
        }
        Collections.sort(map, new AxinComparator());
    }

    /**
     * codeMap结构提取编码包所在MPEM下标
     *
     * @param code
     * @return
     */
    private int[] transform(Set<CodeMap> code) {
        int[] codePacket = new int[code.size()];
        int index = 0;
        for (CodeMap data : code) {
            codePacket[index++] = data.getIndex();
        }
        return codePacket;
    }



    /**
     * codeMap的code从大到小排序
     */
    private static class AxinComparator implements Comparator<CodeMap> {
        @Override
        public int compare(CodeMap o1, CodeMap o2) {
            return o2.getCode() - o1.getCode();
        }
    }
}

