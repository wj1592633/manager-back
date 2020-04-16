package com.wj.manager.common.aop.quartz;

import com.wj.manager.Swagger2;
import com.wj.manager.pojo.SysUser;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

//Job默认每次都会创建新的对象
@DisallowConcurrentExecution //上一次没执行完，不会开启新的一次,直到上次任务执行完
public class JobTest implements Job {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
       /* JobDetail jobDetail = jobExecutionContext.getJobDetail();
        //获取spring容器
        ApplicationContext context = (ApplicationContext)jobDetail.getJobDataMap().get("context");
        Swagger2 bean = context.getBean(Swagger2.class);
        logger.warn("线程::++"+Thread.currentThread().getName());
        logger.warn("描述::++"+jobDetail.getDescription()); //描述das描述
        logger.warn("job类名::++::++"+jobDetail.getJobClass().getName()); //com.wj.manager.common.aop.quartz.JobTest
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        logger.warn("map存的名字:::"+(String)jobDataMap.get("name")); //zhangsan
        logger.warn("密码：++"+((SysUser)jobDataMap.get("user")).getPassword());//111
        logger.warn("Message++:::::==="+jobDetail.getJobDataMap().getString("msg")); //null*/
        System.out.println("1111111111111dasdadad");
        logger.warn("bbbbbb"+Thread.currentThread().getName());
    }
}
