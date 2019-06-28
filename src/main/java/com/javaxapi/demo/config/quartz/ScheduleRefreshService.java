package com.javaxapi.demo.config.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Author: Linlm
 * @Description: 定时（查数据库），根据查询结果决定是否重新设置定时任务
 * @Date: Created in 2019/6/27 上午10:54
 */

@Slf4j
@Configuration
@EnableScheduling
@Component
public class ScheduleRefreshService {

//    @Autowired
//    private ConfigMapper configMapper;

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    private Set<TriggerKey> getTriggerKeyList(){
        GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.anyGroup();
        try {
           return scheduler.getTriggerKeys(groupMatcher);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @Author: Linlm
     * @Description: 每隔10s查库，并根据查询结果决定是否重新设置定时任务
     * @Date: 2019/6/27 下午7:23
     * @Since:
     */
    @Scheduled(fixedRate = 10000)
    public void scheduleUpdateTrigger() throws SchedulerException {
        Set<TriggerKey> triggerKeySet = this.getTriggerKeyList();
        if(triggerKeySet == null){
            return;
        }

        for (TriggerKey triggerKey : triggerKeySet) {
            try {
                this.refreshTrigger(triggerKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重置定时任务时间
     * @param triggerKey
     * @throws Exception
     */
    private void refreshTrigger(TriggerKey triggerKey) throws Exception{
        Trigger trigger = scheduler.getTrigger(triggerKey);

        if (trigger instanceof CronTrigger){
            this.refreshCronTrigger(trigger);
        }else if (trigger instanceof SimpleTrigger){
            this.refreshSimpleTrigger(trigger);
        }
    }

    /**
     * 重置定时任务时间
     * cron表达式设置时间
     * @param trigger
     * @throws Exception
     */
    private void refreshCronTrigger(Trigger trigger) throws Exception{
        CronTrigger cronTrigger = (CronTrigger) trigger;
        TriggerKey triggerKey = cronTrigger.getKey();

        //当前Trigger使用的
        String currentCron = cronTrigger.getCronExpression();

        //从数据库查询出来的, 可以用group.name当作唯一值
        String triggerKeyName = triggerKey.getName();
        String triggerKeyGroup = triggerKey.getGroup();
//        String searchCron = configMapper.findOne(1).getCron();

        // 测试
        String searchCron = "*/5 * * * * ?";
        if (System.currentTimeMillis() > 1561703930000L) {
            searchCron = "*/10 * * * * ?";
        }

        if (currentCron.equals(searchCron)) {
            // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
        } else {
            //新的表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(searchCron);

            cronTrigger = cronTrigger.getTriggerBuilder().withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, cronTrigger);
        }
    }

    /**
     * 重置定时任务时间
     * 定时器设置时间
     * @param trigger
     * @throws Exception
     */
    private void refreshSimpleTrigger(Trigger trigger) throws Exception{
        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
        TriggerKey triggerKey = simpleTrigger.getKey();

        //当前Trigger使用的
        long repeatInterval = simpleTrigger.getRepeatInterval();

        //从数据库查询出来的, 可以用group.name当作唯一值
        String triggerKeyName = triggerKey.getName();
        String triggerKeyGroup = triggerKey.getGroup();
//        String searchCron = configMapper.findOne(1).getCron();

        // 测试
        long searchRepeatInterval = 5000;
        if (System.currentTimeMillis() > 1561703930000L) {
            searchRepeatInterval = 10000;
        }

        if (repeatInterval == searchRepeatInterval) {
            // 如果当前使用的刷新间隔和从数据库中查询出来的刷新间隔一致，则不刷新任务
        } else {
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(searchRepeatInterval)
                    // 重复次数必须设置，否则默认为0，不执行
                    .withRepeatCount(-1);

            Trigger newTrigger = simpleTrigger.getTriggerBuilder().withSchedule(simpleScheduleBuilder).build();

            scheduler.rescheduleJob(triggerKey, newTrigger);
        }
    }
}
