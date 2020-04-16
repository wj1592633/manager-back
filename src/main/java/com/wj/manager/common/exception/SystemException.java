package com.wj.manager.common.exception;

import com.wj.manager.common.Enum.ExceptionEnum;

/**
 * 系统内部异常
 */
public class SystemException extends RuntimeException {
    private String errorCode; //异常code
    private String errorMsg; //异常信息

    public SystemException(String errorCode, String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public SystemException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getErrorMsg());
        this.errorCode = exceptionEnum.getErrorCode();
        this.errorMsg = exceptionEnum.getErrorMsg();
    }

}
