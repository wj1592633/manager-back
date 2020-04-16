package com.wj.manager.common.aop.quartz;

import com.wj.manager.pojo.SysUser;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.MutableTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
//把//@Configuration放开即可
//@Configuration
//@EnableScheduling
public class JobTestStarter {

    //JobDetailFactoryBean
    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean(){
        JobDetailFactoryBean jobDetailFactoryBean =new JobDetailFactoryBean();
        Map<String,Object> map =new HashMap<>();
        SysUser user = new SysUser();
        user.setId(1);
        user.setPassword("111");
        user.setName("lisi");
        map.put("name","zhangsan");
        map.put("user",user);
        //spring容器
        jobDetailFactoryBean.setApplicationContextJobDataKey("context");
        //
        jobDetailFactoryBean.setJobClass(JobTest.class);
        jobDetailFactoryBean.setDescription("描述das描述");
        jobDetailFactoryBean.setName("jobDetailFactoryBean名字");
        jobDetailFactoryBean.setJobDataAsMap(map);
        //任何时候都不要清理job
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }


    //触发器
    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean(){
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        //设置JobDetail
        triggerFactoryBean.setJobDetail(jobDetailFactoryBean().getObject());
        //表达式
        triggerFactoryBean.setCronExpression("0/3 * * * * ? *");
        return triggerFactoryBean;
    }


    //调度器
    @Bean
    public SchedulerFactoryBean SchedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        //调度器把trigger串起来
        schedulerFactoryBean.setTriggers(cronTriggerFactoryBean().getObject());
        //当由多个任务时，再写多个JobDetailFactoryBean和CronTriggerFactoryBean，然后下面再添加
        //schedulerFactoryBean.setTriggers(cronTriggerFactoryBean2().getObject());
        return schedulerFactoryBean;
    }

}
