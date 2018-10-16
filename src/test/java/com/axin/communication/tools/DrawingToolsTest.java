package com.axin.communication.tools;

import org.jfree.chart.plot.PlotOrientation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootTest
public class DrawingToolsTest {
    @Test
    public void testTools() {
        int sum = 10;
        Map<Double, Double> map1 = new HashMap<>();
        double[] restX = RandomTools.getRadomDouble(1, 10, 1, 10);
        double[] restY = RandomTools.getRadomDouble(1, 10, 1, 10);
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
                "这是y轴", PlotOrientation.VERTICAL, dataSet, types);

        Scanner in = new Scanner(System.in);
        in.hasNext();
    }
}