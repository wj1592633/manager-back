package com.wj.manager.common.Enum;

public enum  LogTypeEnum {

    UPDATE("修改"),
    OTHER("其他");

    private String type;

    LogTypeEnum(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
