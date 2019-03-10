package com.axin.communication.algorithm;

import com.axin.communication.BaseTest;
import com.axin.communication.domain.TaskResult;
import com.axin.communication.tools.common.MatrixTools;
import com.axin.communication.tools.common.NetworkCodeTools;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class HcdiNcBscTest extends BaseTest {
    @Test
    public void name() {
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(4, 7, 0.2);
        MatrixTools.printMatrix(delayMPEM);
//        int[][] deleteZero= HcdiNcBsc.deleteZero(delayMPEM);
//        MatrixTools.printMatrix(deleteZero);
    }

    @Test
    public void testMatrix() {
        int[][] matrix = new int[][]{};
        int num;
        try {
            num = matrix[0].length;
        }catch (Exception e){
            num = 0;
        }


        System.out.println("判断成功！");
        System.out.println(num);
    }

    @Test
    public void findTTLPacket() {
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(4, 7, 0.2);
        MatrixTools.printMatrix(delayMPEM);
//        int[] hashcode = HcdiNcBsc.computeHashcode(delayMPEM);
//        System.out.println(Arrays.toString(hashcode));
//
//        int[] codePacket = HcdiNcBsc.findTTLPacket(delayMPEM, hashcode);
//
//        System.out.println(Arrays.toString(codePacket));
    }


    @Test
    public void OCPPacketTest() {
        int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(5, 7, 0.4);
        delayMPEM = deleteZero(delayMPEM);
        int[] hashcode = computeHashcode(delayMPEM);
        MatrixTools.printMatrix(delayMPEM);

        int[] codePacket = findOCPacket(delayMPEM, hashcode);

        log.info(Arrays.toString(codePacket));

    }

    @Test
    public void hcdiNcBsc() {
        NcdiNcBhc hcdiNcBsc = new NcdiNcBhc();
        TaskResult result = hcdiNcBsc.getBandWithAndDelay(5, 100, 0.4, 10, 10, 20, 5, 1);
        log.info(result.toString());
    }

    private int[] findOCPacket(int[][] delayMPEMCache, int[] hashcode) {
        int number = delayMPEMCache.length - 1;
        int m = delayMPEMCache[0].length;

        List<Integer> code = new ArrayList<>();
        int nowCode = 0;
        int fullCode = (int) Math.pow(2, number) - 1;
        for (int i = 0; i < m; i++) {
            code = new ArrayList<>();
            code.add(i);
            nowCode = hashcode[i];

            int index = 0;
            while (nowCode != fullCode && index != m) {
                if (NetworkCodeTools.detection(nowCode, hashcode[index])) {
                    code.add(index);
                    nowCode += hashcode[index];
                }
                index++;
            }
            if (nowCode == fullCode) {
                break;
            }
        }
        if (nowCode == fullCode) {
            return MatrixTools.listToArray(code);
        } else {
            return null;
        }
    }

    private int[] computeHashcode(int[][] delayMPEM) {
        int n = delayMPEM.length - 1;
        int m = delayMPEM[0].length;
        int[] hashcode = new int[m];
        //计算每列的hash值
        for (int i = 0; i < m; i++) {
            int code = 0;
            int carry = 1;
            for (int j = 0; j < n; j++) {
                code += delayMPEM[j][i] * carry;
                carry *= 2;
            }
            hashcode[i] = code;
        }
        return hashcode;
    }

    private int[][] deleteZero(int[][] delayMPEM) {
        int n = delayMPEM.length-1;
        int m = delayMPEM[0].length;
        List<Integer> target = new ArrayList<>();

        //寻找非零列
        for (int i = 0; i < m; i++) {
            int code = 0;
            int carry = 1;
            for (int j = 0; j < n; j++) {
                code += delayMPEM[j][i];
                carry *= 2;
            }
            if (code != 0) {
                target.add(i);
            }
        }

        if (target.size() < 1) {
            return new int[][]{};
        } else {
            //将非零列赋值到新矩阵
            int[][] res = new int[n][target.size()];
            int resIndex = 0;
            for (int index : target) {
                for (int i = 0; i < n; i++) {
                    res[i][resIndex] = delayMPEM[i][index];
                }
                resIndex++;
            }
            return res;
        }
    }
}