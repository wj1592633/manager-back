package com.wj.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @EnableAsync 开启异步功能，在需要异步的方法上@Async
 * @EnableScheduling 开启任务调度器,在需要异步的方法上@Scheduled(cron = "0 * * * * MON-SAT") SysMenuServiceImpl的tesScheduled
 */

@SpringBootApplication(exclude ={MultipartAutoConfiguration.class})
@MapperScan("com.wj.manager.mapper")
@ComponentScan("com.wj.manager")
@EnableCaching
public class ManagerBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagerBootApplication.class, args);
	}


}

