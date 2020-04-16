package com.wj.manager.common.exception.handler;

import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.common.exception.CustomException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一处理异常
 */
@RestControllerAdvice
public class HandleGlobalException {

   /* @ExceptionHandler({UnauthorizedException.class})
    @ResponseBody
    public ResponseResult handleUnauthorizedException (Exception e){
        System.out.println(e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail("无权访问");
    }*/


    @ExceptionHandler({java.lang.NullPointerException.class})
    @ResponseBody
    public ResponseResult handleNullPointerException(Exception e){
        e.printStackTrace();
        return ResponseResult.fail("请正确填写信息!");
    }

    @ExceptionHandler({CustomException.class})
    public ResponseResult handleCustomException(Exception e){
        System.out.println(e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail(e.getMessage());
    }

   /* @ExceptionHandler({DisabledAccountException.class})
    @ResponseBody
    public ResponseResult handleDisabledAccountException (Exception e){
        System.out.println(e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail("您的账号被禁用了");
    }*/

   /* @ExceptionHandler({LockedAccountException.class})
    @ResponseBody
    public ResponseResult handleLockedAccountException (Exception e){
        System.out.println(e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail("您的账号被冻结了，请联系管理员");
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseResult handleAuthenticationException (Exception e){
        System.out.println(e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail("登陆出错");
    }
*/
   @ExceptionHandler({AccessDeniedException.class})
   public ResponseResult handleAccessDeniedException(Exception e){

       System.out.println(e.getMessage());
       e.printStackTrace();
       return ResponseResult.fail("无权操作");
   }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseResult handleException(Exception e){

        System.out.println(e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail("无权访问 ( ╯□╰ )");
    }


}
