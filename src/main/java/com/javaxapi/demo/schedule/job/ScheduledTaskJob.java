package com.javaxapi.demo.schedule.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * @Author: Linlm
 * @Description: 定时任务类
 * @Date: Created in 2019/6/27 上午10:46
 */
@Slf4j
@Configuration
@Component

// 标注启动定时任务
@EnableScheduling
public class ScheduledTaskJob {
    public void sayHello(){
        System.out.println("Hello world, i'm the king of the world!!! \n");
    }

    public void sayBye(){
        System.out.println("Bye Bye, i'm going away!!! \n");
    }
}