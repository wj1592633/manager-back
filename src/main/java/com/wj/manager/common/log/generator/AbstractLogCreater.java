package com.wj.manager.common.log.generator;

import com.wj.manager.common.log.constance.LogConst;
import com.wj.manager.common.log.vo.LogDataVo;
import com.wj.manager.common.util.ToolUtil;
import com.wj.manager.pojo.SysLoginLog;
import com.wj.manager.pojo.SysOperationLog;
import com.wj.manager.pojo.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public abstract class AbstractLogCreater implements LogCreater{

    @Override
    public SysOperationLog createOperationLog(LogDataVo logDataVo, String logName, Integer userId,
                                              String className, String method, String sccess, String dict) {

        SysOperationLog log = new SysOperationLog();
        //aop那对logName拼装了，现在截取
        log.setLogname(logName.substring(2));
        log.setCreatetime(new Date());
        log.setLogtype(LogConst.LOG_TYPE_BUSINESS);
        log.setUserid(userId);
        log.setClassname(className);
        log.setMethod(method);
        log.setSucceed(sccess);
        log.setMessage(Vo2Msg(logDataVo,logName.substring(0,2),dict));
        return log;
        //[角色名称]->值:超级管理员;;  [性别]->值:null;;  [部门名称]->值:;;  [头像]->值:string;;  [null]->值:string;;  [电话]->值:1115;;  [名字]->值:string;;  [账号]->值:string;;  [电子邮件]->值:string;;
    }

    protected String Vo2Msg(LogDataVo logDataVo,String logType,String dict){
        Object oldValue = logDataVo.getOldValue();
        Object value = logDataVo.getValue();
        Map<String, Object> old = null;
        Map<String, Object> now = null;
        if(!(oldValue instanceof Map && null != oldValue)){
            old = ToolUtil.bean2Map(oldValue);
        }
        if(!(value instanceof Map) && null != value){
            now = ToolUtil.bean2Map(value);
        }else if(value instanceof Map){
            now = (Map)value;
        }
        if(StringUtils.isNoneBlank(logType) && (logType.indexOf(LogConst.TYPE_UPDATE) != -1)){
            return createMsg(old,now,dict);
        }else {
            return createMsg(now,dict);
        }
    }

    protected abstract String createMsg(Map old, Map now, String dict);
    protected abstract String createMsg(Map now, String dict);

    @Override
    public SysLoginLog createLoginLog(ProceedingJoinPoint joinPoint,Integer userid,String logName, String success,String ip) {
        Object[] args = joinPoint.getArgs();
        SysLoginLog log = new SysLoginLog();
        log.setUserid(userid);
        log.setLogname(logName);
        log.setSucceed(success);
        log.setCreatetime(new Date());
        log.setIp(ip);
        if(logName.indexOf("登陆") != -1) {
            SysUser sysUser = (SysUser) args[0];
            log.setIp(ip);
            log.setMessage("账号:" + sysUser.getAccount() + ",密码:" + sysUser.getPassword());
            return log;
        }
        return log;
    }
}
