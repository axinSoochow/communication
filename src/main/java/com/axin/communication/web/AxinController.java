package com.axin.communication.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Slf4j
public class AxinController {

    private final static Logger log = LoggerFactory.getLogger(AxinController.class);

    @RequestMapping("/hello")
    public String hello() {
        log.info("测试 测试！");
        return "hello world!";
    }
}
