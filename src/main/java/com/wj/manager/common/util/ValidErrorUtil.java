package com.wj.manager.common.util;

import com.wj.manager.common.dto.ResponseResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;


public class ValidErrorUtil {
    /**
     * hibernate表单后台验证处理
     * @param bindingResult 表单校验结果
     * @return 从bindingResult提取的所有错误信息组成的字符串，格式：密码不能为空!账号不能为空!
     */
    public static String handleValidError(BindingResult bindingResult){
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError error:allErrors)
        {
            sb.append(error.getDefaultMessage());
        }
        return sb.toString();
    }
}
