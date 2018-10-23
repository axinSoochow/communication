package com.axin.communication.algorithm;

import com.axin.communication.domain.CodeMap;
import com.axin.communication.tools.common.MatrixTools;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 基于汉明重量网络编码的无线广播重传
 * Hamming Weight Network Encoding Retransmission
 * @author Axin
 * @date 18-10-19
 */
@Component("hwner")
public class Hwner implements NetworkCode {

    @Override
    public int[] getCodePacket(int[][] MPEM) {
        if (MatrixTools.isZeroMatrix(MPEM)) {
            return new int[0];
        }
        //初始化
        List<CodeMap> map = new ArrayList<>();
        Set<CodeMap> codeResult = new HashSet<>();

        int number = MPEM.length;
        computeHamming(MPEM, map);

        //取出第一个
        CodeMap first = map.get(0);
        codeResult.add(first);
        int fullCode = number;
        int nowCode = first.getCode();
        //记录初始的列
        int[] nowCodePacket = new int[number];
        int index = 1;

        //初始化
        for (int i = 0; i < MPEM.length; i++) {
            nowCodePacket[i] = MPEM[i][first.getIndex()];
        }

        while (nowCode != fullCode && index != map.size()) {
            CodeMap curCode = map.get(index);
            if (nowCode + curCode.getCode() <= fullCode
                    && detection(MPEM, curCode.getIndex(), nowCodePacket)) {
                codeResult.add(curCode);
                update(MPEM, curCode.getIndex(), nowCodePacket);
            }
            index++;
        }
        int[] codePacket = transform(codeResult);
        return codePacket;
    }

    /**
     * 更新
     * @param MPEM
     * @param index
     * @param nowCodePacket
     */
    private void update(int[][] MPEM, int index, int[] nowCodePacket) {
        for (int i = 0; i < MPEM.length; i++) {
            if (MPEM[i][index]==1) {
                nowCodePacket[i] = 1;
            }
        }
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
     * 是否不不冲突
     *
     * @param MPEM
     * @param index
     * @return
     */
    private boolean detection(int[][] MPEM, int index, int[] nowCodePacket) {
        //越界判断
        boolean isOut = index < 0 || index >= MPEM[0].length;
        if (isOut) {
            return false;
        }

        for (int i = 0; i < MPEM.length; i++) {
            if (nowCodePacket[i] + MPEM[i][index] == 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算数据包丢失矩阵每列的汉明距离
     *
     * @param MPEM
     * @param map
     */
    private void computeHamming(int[][] MPEM, List<CodeMap> map) {
        int index = 0;
        for (int i = 0; i < MPEM[0].length; i++) {
            int hamming = 0;
            for (int j = 0; j < MPEM.length; j++) {
                hamming += MPEM[j][i];
            }
            if (hamming != 0) {
                map.add(new CodeMap(hamming, index));
            }
            index++;
        }
        Collections.sort(map, new HammingComparator());
    }

    private static class HammingComparator implements Comparator<CodeMap> {
        @Override
        public int compare(CodeMap o1, CodeMap o2) {
            return o2.getCode() - o1.getCode();
        }
    }
}
