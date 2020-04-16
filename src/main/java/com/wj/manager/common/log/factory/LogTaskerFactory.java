package com.wj.manager.common.log.factory;

import com.wj.manager.common.log.generator.LogCreater;
import com.wj.manager.common.log.vo.LogDataVo;
import com.wj.manager.common.util.SpringContextHolder;
import com.wj.manager.mapper.SysLoginLogMapper;
import com.wj.manager.mapper.SysOperationLogMapper;
import com.wj.manager.pojo.SysLoginLog;
import com.wj.manager.pojo.SysOperationLog;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.TimerTask;

public class LogTaskerFactory {

    private static LogCreater logCreater = SpringContextHolder.getBean(LogCreater.class);
    private static SysOperationLogMapper operationLogMapper = SpringContextHolder.getBean(SysOperationLogMapper.class);
    private static SysLoginLogMapper loginLogMapper = SpringContextHolder.getBean(SysLoginLogMapper.class);

    public static TimerTask businessLog(LogDataVo logDataVo, String logName, Integer userId,
                                        String className, String method, String sccess, String dict){
       return new TimerTask() {
           @Override
           public void run() {
               SysOperationLog log = logCreater.createOperationLog(logDataVo, logName, userId, className, method, sccess, dict);
               operationLogMapper.insert(log);
           }
       };
    }
    public static TimerTask LoginLog(ProceedingJoinPoint joinPoint,Integer userid,String logName, String success,String ip){
        return new TimerTask() {
            @Override
            public void run() {
                SysLoginLog log = logCreater.createLoginLog(joinPoint,userid,logName, success,ip);
                loginLogMapper.insert(log);
            }
        };
    }

}
