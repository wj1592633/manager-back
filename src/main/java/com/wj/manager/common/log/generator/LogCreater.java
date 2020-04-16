package com.wj.manager.common.log.generator;

import com.wj.manager.common.log.vo.LogDataVo;
import com.wj.manager.pojo.SysLoginLog;
import com.wj.manager.pojo.SysOperationLog;
import org.aspectj.lang.ProceedingJoinPoint;

public interface LogCreater {
   public SysOperationLog createOperationLog(LogDataVo logDataVo,String logName,Integer userId,String className,String method, String sccess,String dict);

   public SysLoginLog createLoginLog(ProceedingJoinPoint joinPoint,Integer userid,String logName, String success,String ip);

}
