package com.javaxapi.demo.config.quartz;

import com.javaxapi.demo.schedule.job.ScheduledTaskJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @Author: Linlm
 * @Description: 定时器配置类
 * @Date: Created in 2019/6/27 上午10:44
 */
@Slf4j
@Configuration
public class QuartzConfig {
    //指定了触发规则
    @Bean(name = "firstTrigger")
    public CronTriggerFactoryBean ScheduledTaskTrigger(JobDetail firstJobDetail) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(firstJobDetail);
        trigger.setCronExpression("*/5 * * * * ?");
        trigger.setGroup("first");

        return trigger;
    }


    @Bean(name = "firstJobDetail")
    public MethodInvokingJobDetailFactoryBean ScheduledTaskDetail() {//指定了具体需要执行的类 具体的方法就是我们需要实现的excuteInternal
        ScheduledTaskJob task = new ScheduledTaskJob();
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();

        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(task);
        // 需要执行的方法
        jobDetail.setTargetMethod("sayHello");

//        jobDetail.setGroup("srd");

        return jobDetail;
    }

    //第二个定时任务的触发规则
    @Bean(name = "secondTrigger")
    public SimpleTriggerFactoryBean submitFailDataTaskTrigger(JobDetail secondJobDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(secondJobDetail);
        // 设置任务启动延迟
        trigger.setStartDelay(0);
        // 每5s执行一次
        trigger.setRepeatInterval(5000);

        trigger.setGroup("second");

        return trigger;
    }


    //第二个定时任务
    @Bean(name = "secondJobDetail")
    public MethodInvokingJobDetailFactoryBean SubmitFailedDataTaskDetail() {
        ScheduledTaskJob task = new ScheduledTaskJob();

        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(task);
        // 需要执行的方法
        jobDetail.setTargetMethod("sayBye");

//        jobDetail.setGroup("srd");

        return jobDetail;
    }


    // 配置Scheduler
    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(Trigger firstTrigger, Trigger secondTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        // 注册触发器
        bean.setTriggers(firstTrigger, secondTrigger);
        return bean;
    }
}
