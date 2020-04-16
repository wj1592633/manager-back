package com.wj.manager.common.exception;

import com.wj.manager.common.Enum.ExceptionEnum;

/**
 * 自定义异常类
 */
public class CustomException extends RuntimeException {
    private String errorCode; //异常code
    private String errorMsg; //异常信息

    public CustomException(String errorCode, String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public CustomException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getErrorMsg());
        this.errorCode = exceptionEnum.getErrorCode();
        this.errorMsg = exceptionEnum.getErrorMsg();
    }

}
