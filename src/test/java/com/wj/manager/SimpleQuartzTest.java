package com.wj.manager;

import com.wj.manager.common.aop.quartz.JobTest;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

public class SimpleQuartzTest {

    @Test
    public void testSimpleQuartz() throws SchedulerException, IOException {

        JobBuilder jobBuilder = JobBuilder.newJob(JobTest.class);
        //jobDetail
        JobDetail jobDetail = jobBuilder.withIdentity("xxxx")
                .withDescription("description111")
                .usingJobData("msg", "aaaaaaa")
                .build();

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        //触发器
        SimpleTrigger trigger = triggerBuilder.withIdentity("yyy", "group1")
                .startNow()//马上执行
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(5))
                .build();
        //调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //绑定jobDetail和trigger
        scheduler.scheduleJob(jobDetail,trigger);
        //开始
        scheduler.start();
        System.out.println("aaaaaaa"+Thread.currentThread().getName());
        System.in.read();
    }
}
