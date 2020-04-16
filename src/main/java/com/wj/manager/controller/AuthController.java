package com.wj.manager.controller;

import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.pojo.SysLoginLog;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.SecurityKit;
import com.wj.manager.security.sevice.AuthService;
import com.wj.manager.security.vo.AuthToken;
import com.wj.manager.service.SysLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api("登陆登出Controller")
@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @ApiOperation("登陆")
    @PostMapping("/auth/login")
    public ResponseResult login(@ModelAttribute SysUser loginUser,HttpServletRequest request){
        if(loginUser == null || StringUtils.isBlank(loginUser.getAccount())|| StringUtils.isBlank(loginUser.getPassword())){
            throw new CustomException(ExceptionEnum.ACC_PWD_NO_NULL);
        }
        //获取令牌
        AuthToken token = null;
        token = authService.login(loginUser.getAccount(), loginUser.getPassword());

        if(token != null && StringUtils.isNoneBlank(token.getJwtToken()) && StringUtils.isNoneBlank(token.getRefreshToken())){
            return ResponseResult.success(token);
        }
        throw new CustomException(ExceptionEnum.PWD_OR_ACC_ERROR);
    }

    @ApiOperation("登出")
    @GetMapping("/auth/logout")
    public ResponseResult logout(HttpServletRequest request){
        Integer userId = SecurityKit.getUserId();
        Integer row = authService.logout(userId);
        if(null == row || row.intValue() == 0){
            throw new CustomException(ExceptionEnum.PWD_OR_ACC_ERROR);
        }
       return ResponseResult.success("退出登陆成功");
    }

    @ApiOperation("解除锁定")
    @PostMapping("/auth/unlock")
    public ResponseResult unlock(String password){
        if (StringUtils.isBlank(password)){
            throw new CustomException(ExceptionEnum.PWD_NOT_RIGHT);
        }
        Integer userId = SecurityKit.getUserId();
        boolean flag = authService.unlock(userId,password);
        if(flag){
            return ResponseResult.success();
        }
        throw new CustomException(ExceptionEnum.PWD_NOT_RIGHT);
    }

}
