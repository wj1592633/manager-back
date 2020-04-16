package com.wj.manager.common.aop;

import com.wj.manager.common.anotation.BusinessLog;
import com.wj.manager.common.log.LogManager;
import com.wj.manager.common.log.constance.LogConst;
import com.wj.manager.common.log.factory.LogTaskerFactory;
import com.wj.manager.common.log.vo.LogDataVo;
import com.wj.manager.common.thread.LogThreadData;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.SecurityKit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

@Aspect
@Component
@Order(200)
public class LogAop {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut(value = "@annotation(com.wj.manager.common.anotation.BusinessLog)")
    public void pointCut(){}

    //只有@Around才能接收ProceedingJoinPoint，其他的JoinPoint
    @Around("pointCut()")
    public Object businessLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = null;
        String success = LogConst.SUCCESS;
        try {
           result = joinPoint.proceed(args);
        }catch (Exception e){
            //用户操作失败，记录日志，结果为fail
            success = LogConst.FAIL;
            //记录日志要捕获异常，并且不能抛出去。类似redis操作
            try {
                handleBusinessLog(joinPoint,success);
            } catch (Exception e1){
                logger.error("记录日志异常:"+ e1.getMessage());
            }

            throw e;
        }
        //用户操作成功，记录日志，结果位success
        try {
            handleBusinessLog(joinPoint,success);
        } catch (Exception e){
            logger.error("记录日志异常:"+ e.getMessage());
        }
        //方法运行的到的结果:ResponseResult。如果返回静态的，那么被切的方法的返回值都相同
        return result;
    }

    private void handleBusinessLog(ProceedingJoinPoint joinPoint, String success){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = null;
        if( !(signature instanceof MethodSignature)){
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);
        String dict = businessLog.dict();
        String type = businessLog.type().getType();
        String value =type + businessLog.value();

        LogDataVo logDataVo = new LogDataVo();
        Object[] args = joinPoint.getArgs();
        //不是封装类型的实参，比如controller接收的时 (Integer status,Integer userid),组装成map
        if(!businessLog.isWarpper()){
            Map nowMap = new HashMap<>();
            String[] parameterNames = methodSignature.getParameterNames();
            for (int index = 0 ; index < args.length ; index++){
                Object arg = args[index];
                if(!(arg instanceof ServletRequest) && !(arg instanceof ServletResponse)){
                    nowMap.put(parameterNames[index],arg);
                }
            }
            logDataVo.setValue(nowMap);
        }else {
            //封装类型的实参不做处理。controller方法接收实参时，要放在第一位
            logDataVo.setValue(args[0]);
        }
        //修改操作
        if(type.indexOf(LogConst.TYPE_UPDATE) != -1){
            //从线程共享数据中取出封装类型的数据
            logDataVo.setOldValue(LogThreadData.instance().getData());
        }
        String className = methodSignature.getDeclaringTypeName();
        TimerTask task = LogTaskerFactory.businessLog(logDataVo, value, SecurityKit.getUserId(), className, method.getName(), success, dict);
        //记录日志
        LogManager.me().executeLog(task);
        //LogManager.me().executeLog(LogTaskerFactory.businessLog(logDataVo,value, 1,className,method.getName(), success, dict));
        LogThreadData.release();
    }

    @Around("execution(public * com.wj.manager.controller.AuthController.login(..))")
    public Object LoginLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = null;
        String success = LogConst.SUCCESS;
        try {
            result = joinPoint.proceed(args);
        }catch (Exception e){
            //用户操作失败，记录日志，结果为fail
            success = LogConst.FAIL;
            //记录日志要捕获异常，并且不能抛出去。类似redis操作
            try {
                handleLoginLog(joinPoint,null,LogConst.LOG_TYPE_LOGIN,success);
            } catch (Exception e1){
                logger.error("记录日志异常:"+ e1.getMessage());
            }

            throw e;
        }
        //用户操作成功，记录日志，结果位success
        try {
            handleLoginLog(joinPoint,null,LogConst.LOG_TYPE_LOGIN,success);
        } catch (Exception e){
            logger.error("记录日志异常:"+ e.getMessage());
        }
        //方法运行的到的结果:ResponseResult。如果返回静态的，那么被切的方法的返回值都相同
        return result;
    }

    @Around("execution(public * com.wj.manager.controller.AuthController.logout(..))")
    public Object LogOutLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = null;
        String success = LogConst.SUCCESS;
        try {
            result = joinPoint.proceed(args);
        }catch (Exception e){
            //用户操作失败，记录日志，结果为fail
            success = LogConst.FAIL;
            //记录日志要捕获异常，并且不能抛出去。类似redis操作
            try {
                handleLoginLog(joinPoint,SecurityKit.getUserId(),LogConst.LOG_TYPE_LOGOUT,success);
            } catch (Exception e1){
                logger.error("记录日志异常:"+ e1.getMessage());
            }

            throw e;
        }
        //用户操作成功，记录日志，结果位success
        try {
           handleLoginLog(joinPoint,SecurityKit.getUserId(),LogConst.LOG_TYPE_LOGOUT,success);
        } catch (Exception e){
            logger.error("记录日志异常:"+ e.getMessage());
        }
        //方法运行的到的结果:ResponseResult。如果返回静态的，那么被切的方法的返回值都相同
        return result;
    }

    private void handleLoginLog(ProceedingJoinPoint joinPoint,Integer userid,String logName,String success){
        Object[] args = joinPoint.getArgs();
        String ip = null;
        for (Object o : args){
            if(o instanceof ServletRequest){
                ip =  ((ServletRequest) o).getRemoteAddr();
            }
        }
        LogManager.me().executeLog(LogTaskerFactory.LoginLog(joinPoint,userid ,logName,success,ip));
    }

}
