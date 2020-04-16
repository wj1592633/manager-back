package com.wj.manager.common.log.vo;

import java.util.Map;

public class LogDataVo<T> {
    private Object value; //增删的数据都存这个Map。修改时，修改后的数据存
    private T oldValue; //存放修改前的数据

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public T getOldValue() {
        return oldValue;
    }

    public void setOldValue(T oldValue) {
        this.oldValue = oldValue;
    }
}
