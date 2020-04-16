package com.wj.manager.common.dto;

public class ResponseResult {
    public static String USER_EXIST = "用户已经存在";
    public static String OPERATE_ERROR = "网络环境异常，稍后请重试";

    private String state;
    private String failMsg;
    private Object data;

    private ResponseResult(){}

    public static ResponseResult success(Object data){
        ResponseResult result = new ResponseResult();
        result.state = "200";
        result.data = data;
        return result;
    }

    public static ResponseResult success(){
       return ResponseResult.success(null);
    }

    public static ResponseResult fail(String failMsg){
        ResponseResult result = new ResponseResult();
        result.state = "500";
        result.data = null;
        result.failMsg = failMsg;
        return result;
    }
    public static ResponseResult saveAndForward(Object data){
        ResponseResult result = new ResponseResult();
        result.state = "202";
        result.data = data;
        return result;
    }

    public static ResponseResult login(String failMsg){
        ResponseResult result = new ResponseResult();
        result.state = "401";
        result.data = null;
        result.failMsg = "可能您还未登陆或者登陆超时";
        if(failMsg != null){
            result.failMsg = failMsg;
        }
        return result;
    }


    public static ResponseResult login(){
        return login(null);
    }

    public static ResponseResult fail(){
        return ResponseResult.fail(null);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
