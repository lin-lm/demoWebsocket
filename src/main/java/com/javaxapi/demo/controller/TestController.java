package com.javaxapi.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Linlm
 * @Description:
 * @Date: Created in 2019/7/8 下午4:55
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Value("${env.is.dev}")
    private boolean isdev;

    @RequestMapping("/testLog")
    public String testLog(){
        log.debug("debug");
        log.info("info");
        log.info("isdev: " + isdev);
        log.error("warn");
        log.info("error");

        return "success";
    }
}
