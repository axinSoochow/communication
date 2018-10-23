package com.axin.communication.tools.algorithm;

import com.axin.communication.domain.codeMap;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 基于ONC的单组合分组广播传输
 * ONCSB
 *
 * @author Axin
 * @date
 */
@Component("oncsb")
public class Oncsb implements NetworkCode {

    @Override
    public int[] getCodePacket(int[][] MPEM) {
        if (MatrixTools.isZeroMatrix(MPEM)) {
            return new int[0];
        }
        //map存放每列计算好的hash值
        List<codeMap> map = new ArrayList<>();
        Set<codeMap> codeResult = new HashSet<>();

        int number = MPEM.length;
        //计算每列的hash值
        computeHashcode(MPEM, map);

        //取出第一个
        codeMap first = map.get(0);
        codeResult.add(first);
        int fullCode = (int) Math.pow(2, number) - 1;
        int nowCode = first.getCode();
        int index = 0;

        //如果存在有一列全1
        if (nowCode == fullCode) {
            return new int[]{first.getIndex()};
        }
        while (nowCode != fullCode && index != map.size()) {
            codeMap curCode = map.get(index);
            if (detection(curCode.getCode(), nowCode)) {
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
    private void computeHashcode(int[][] MPEM, List<codeMap> map) {
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
                map.add(new codeMap(code, index));
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
    private int[] transform(Set<codeMap> code) {
        int[] codePacket = new int[code.size()];
        int index = 0;
        for (codeMap data : code) {
            codePacket[index++] = data.getIndex();
        }
        return codePacket;
    }

    /**
     * 检测两个编码是否满足网络编码解码条件
     *
     * @param code1
     * @param code2
     * @return
     */
    private boolean detection(int code1, int code2) {
        if ((code1 ^ code2) == code1 + code2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * codeMap的code从大到小排序
     */
    private static class AxinComparator implements Comparator<codeMap> {
        @Override
        public int compare(codeMap o1, codeMap o2) {
            return o2.getCode() - o1.getCode();
        }
    }

//    public static void main(String[] args) {
//        int a = 8;
//        int b =9;
//        System.out.println("a异或b:"+(a^b));
//        System.out.println("a同或b:"+~(a^b));
//    }
}

