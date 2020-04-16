package com.wj.manager.common.aop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.mapper.SysMenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class PermissionAop {

    @Autowired
    SysMenuMapper menuMapper;

    @Pointcut(value = "@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void permissionPointCut(){}

    @Before("permissionPointCut()")
    public void permissionCheck(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = null;
        if( !(signature instanceof MethodSignature)){
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        String value = annotation.value();
        String menuUrl = StringUtils.substringBetween(value, "('", "')");
        Integer menuStatusByUrl = menuMapper.getMenuStatusByUrl(menuUrl);
        if (menuStatusByUrl != null && menuStatusByUrl.intValue() != ConstantFactory.SYS_STATE_ENABLE.intValue()){
            throw new CustomException(ExceptionEnum.FORBIDEN_AREA);
        }
    }
}
