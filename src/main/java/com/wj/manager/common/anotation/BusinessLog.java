package com.wj.manager.common.anotation;

import com.sun.jdi.LongType;
import com.wj.manager.common.Enum.LogTypeEnum;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BusinessLog {
    String value();
    LogTypeEnum type() default LogTypeEnum.OTHER; //修改或非修改操作
    boolean isWarpper() default true; //接收的参数是否是封装类型参数，封装类型参数要放第一;不是，那参数引用要和其实体类字段一样
    String dict();
}
