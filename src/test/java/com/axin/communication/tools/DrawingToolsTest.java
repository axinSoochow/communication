package com.axin.communication.tools;

import com.axin.communication.tools.common.RandomTools;
import com.axin.communication.tools.draw.DrawingTools;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootTest
public class DrawingToolsTest {
    @Test
    public void drawLineChart() {
        int sum = 20;
        Map<Double, Double> map1 = new HashMap<>();
        double[] restX = RandomTools.getRadomDouble(1, 20, 1, sum);
        double[] restY = RandomTools.getRadomDouble(1, 20, 1, sum);
        for (int i = 0; i < sum; i++) {
            map1.put(restX[i], restY[i]);
        }

        Map<Double, Double> map2 = new HashMap<>();
        for (int i = 0; i < sum; i++) {
            map2.put(restY[i], restX[i]);
        }

        Map<Double, Double>[] dataSet = new Map[]{map1, map2};
        String[] types = new String[]{"A", "B"};

        DrawingTools.drawLineChart("axin", "测试", "这是X轴",
                "这是y轴", dataSet, types);

        Scanner in = new Scanner(System.in);
        in.hasNext();
    }
}