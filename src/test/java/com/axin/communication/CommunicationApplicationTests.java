package com.axin.communication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunicationApplicationTests {
    protected long startTime;
    @Before
    public void init() {
        System.out.println("开始测试");
        startTime = System.currentTimeMillis();
    }

    @After
    public void destroy() {
        System.out.println("测试结束");
        System.out.println("耗时："+(System.currentTimeMillis()-startTime)+"ms");
    }
}
