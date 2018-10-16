package com.axin.communication.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AxinController {

    @RequestMapping("/")
    public String hello() {
        return "hello world!";
    }
}
