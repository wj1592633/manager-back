package com.wj.manager.security;

import com.wj.manager.common.util.SpringContextHolder;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.constance.TokenExConstance;
import com.wj.manager.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;

import java.util.List;
import java.util.Map;

/**
 * 自定义对jwtToken的校验，主要用来修改密码、注销登陆后，使token校验不通过(可以理解为使token失效)
 */
public class MyJwtClaimsSetVerifier implements JwtClaimsSetVerifier {
    /**
     *
     * @param map 可以说是jwtToken的负载payload
     * @throws InvalidTokenException
     */
    @Override
    public void verify(Map<String, Object> map) throws InvalidTokenException {
        //生成token时，记得一定要在payload（CustomUserAuthenticationConverter）中添加note
        String note = (String)map.get(TokenExConstance.NOTE);
        Integer userid = (Integer)map.get(TokenExConstance.ID);
        if(StringUtils.isBlank(note) || null== userid){
            throw new InvalidTokenException("");
        }
        if(StringUtils.isBlank(note)){
            //其实加了提示信息是无效的，最终都会被转成下面的异常，异常信息如下
            //throw new InvalidTokenException("Cannot convert access token to JSON", var6);
            throw new InvalidTokenException("您的登陆已经失效，请重新登陆");
        }
        SysUserService userService = SpringContextHolder.getBean(SysUserService.class);
        String saltDB = userService.getSaltByUserId(userid);
        if(StringUtils.isNoneBlank(saltDB)){
            note = note.trim();
            //curNote是数据库中的一个字段，用户修改密码的同时也修改这个字段，每次解析token时都取数据库对比下，这样原来的token就通不过校验了
            String  curNote=saltDB.trim();
            if( ! note.equals(curNote)){
                throw new InvalidTokenException("");
            }
        }else {
            throw new InvalidTokenException("");
        }
    }
}
