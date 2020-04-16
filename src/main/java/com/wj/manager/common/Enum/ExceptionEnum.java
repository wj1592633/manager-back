package com.wj.manager.common.Enum;


/**
 * 异常消息枚举
 */
public enum ExceptionEnum {
    /** 未知异常 */
    UNKNOWN_EXCEPTION("1000","未知异常"),
    //五权限异常
    NO_PERMISSION("1001","无权操作"),

    //查询的数据不存在
    NOT_EXIST("1002","查无此记录"),
    //用户存在
    USER_EXIST("1003","账号已经被注册"),
    //数据库操作失败
    DB_OPERATE_ERROR("1004","数据库操作失败"),
    //
    NO_IMG("1005","文件上传失败"),

    DONT_OPERATE_ADMIN("1006","不能更改管理员"),
    DONT_DELETE_ADMIN("1009","不能删除管理员"),

    NO_WORK("1007","无效的操作"),

    NULL_DATA("1008","无效的信息"),

    BAD_NET("1009","操作失败，稍后重试!"),

    ROLE_NOT_EXIST("1010","无效的角色"),
    MENU_NOT_EXIST("1011","无效的权限"),

    ERROR_ACCOUNT("1012","账号不存在"),
    ACC_PWD_NO_NULL("1013","账号或密码不能为空"),
    USER_NO_EXSIT("1014","用户不存在"),
    PWD_NOT_RIGHT("1015","密码错误"),
    PWD_OR_ACC_ERROR("1017","账号或密码错误"),
    OPERATE_DATA_NOT_EXSIT("1016","操作的数据不存在"),
    FORBIDEN_AREA("1018","该系统功能已被禁用"),

    LOG_CONTEXT_NULL("1019","日志内容不能为空"),
    USER_INVALID("1020","无效的用户"),
    USER_FREEZE("1021","用户已被冻结"),
    LOG_OUT_FAIL("1022","退出失败"),
    NOT_UPDATE_BOSS_PWD("1023","不能修改boss用户密码"),
    ;


    private String errorCode;
    private String errorMsg;

    ExceptionEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
