package com.wj.manager.security;

import com.wj.manager.security.constance.TokenExConstance;
import com.wj.manager.security.vo.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;


    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        LinkedHashMap response = new LinkedHashMap();
        String name = authentication.getName();
        //这个必须写，而且必须为user_name.不写的话，会导致，SecurityContextHolder.getContext.getAuthentication.userAuthentication为null，就是获取不到登陆用户的信息
        //具体见DefaultUserAuthenticationConverter的extractAuthentication()方法
        response.put(TokenExConstance.USER_NAME, name);

        Object principal = authentication.getPrincipal();
        JwtUser userJwt = null;
        if(principal instanceof  JwtUser){
            userJwt = (JwtUser) principal;
        }else{
            //refresh_token默认不去调用userdetailService获取用户信息，这里我们手动去调用，得到 JwtUser
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            userJwt = (JwtUser) userDetails;
        }

        response.put(TokenExConstance.ID, userJwt.getId());
        //必填，key 默认为note。可以修改,但必须同时修改MyJwtClaimsSetVerifier里面key
        response.put(TokenExConstance.NOTE, userJwt.getNote());
        if(userJwt.getDeptid() != null){
            response.put(TokenExConstance.DEPT_ID, userJwt.getDeptid());
            response.put(TokenExConstance.DEPT_SCOPE,userJwt.getDeptScope());
        }
        response.put(TokenExConstance.IS_ADMIN,userJwt.isIfAdmin());

        //做权限控制的话，这个必须写，而且必须为authorities
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(TokenExConstance.AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }

        return response;
    }


}
