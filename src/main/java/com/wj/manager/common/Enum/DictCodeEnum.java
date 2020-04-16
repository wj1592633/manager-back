package com.wj.manager.common.Enum;

public enum DictCodeEnum {
    SEX("sys_sex"),//性别
    SYS_STATE("sys_state"),//系统状态
    ACCOUNT_STATE("account_state");//用户/账号 状态
    private String code;

    DictCodeEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
