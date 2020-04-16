package com.wj.manager.security;

import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.util.SpringContextHolder;
import com.wj.manager.mapper.SysUserMapper;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.constance.TokenExConstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@DependsOn("springContextHolder")
public class SecurityKit {

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    public static SecurityKit instance(){
       return SpringContextHolder.getBean(SecurityKit.class);
    }

    /**
     * 认证信息，有账号，权限，是否认证等信息
     * @return
     */
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 认证详情，有ip，token等信息
     * @return
     */
    public static OAuth2AuthenticationDetails getAuthenticationDetails(){
        return (OAuth2AuthenticationDetails)getAuthentication().getDetails();
    }

    /**
     * 获取token的完整信息。spring oauth2 默认不会把我们CustomUserAuthenticationConverter中附加的
     * 全部信息存到Authentication
     * @return
     */
    public static Map getFullAuthentication(){
        try {
            String token = getAuthenticationDetails().getTokenValue();
            Jwt jwt = JwtHelper.decode(token);
            String claimsStr = jwt.getClaims();
            Map<String, Object> claims = JsonParserFactory.create().parseMap(claimsStr);
            return claims;
        } catch (Exception var6) {
            throw new InvalidTokenException("Cannot convert access token to JSON", var6);
        }
    }
    /**
     * 获取当前用户account
     * @return
     */
    public static String getCurAccount(){
        return getAuthentication().getName();
    }
    /**
     * 获取当前用户的部门数据范围的集合
     */
    public static List<Integer> getDeptDataScope() {
       return  (List<Integer>)(getFullAuthentication().get(TokenExConstance.DEPT_SCOPE));
    }

    /**
     * 是否为管理员
     * @return
     */
    public static boolean isAdmin(){
        return  (boolean)(getFullAuthentication().get(TokenExConstance.IS_ADMIN));
    }

    public static Integer getUserId(){
        return  (Integer)(getFullAuthentication().get(TokenExConstance.ID));
    }

    /**
     * 是否有权操作别的用户信息
     * @param otherUserId
     * @return
     */
    public static boolean canUpdateOtherUser(Integer otherUserId) {
        if (otherUserId == null) {
            throw new CustomException(ExceptionEnum.NO_PERMISSION);
        }
        SysUser otherUser = SpringContextHolder.getBean(SysUserMapper.class).selectById(otherUserId);
        //用户不存在，不能修改
        if (null == otherUser || null == otherUser.getId()) {
            throw new CustomException(ExceptionEnum.NO_PERMISSION);
        }

        //判断自己是不是管理员
        if (SecurityKit.isAdmin()) {
            if(null == otherUser.getDeptid()){
                return true;
            }
            List<Integer> MyDataScope = SecurityKit.getDeptDataScope();
            String yourTips = ConstantFactory.instance().getRoleTipsByUserId(otherUserId);
            String myTips = ConstantFactory.instance().getRoleTipsByUserId(SecurityKit.getUserId());
            //如果对方也是管理员，也不能修改
            if (ConstantFactory.instance().ADMIN_NAME.equals(yourTips) || myTips.equals(yourTips)) {
                throw new CustomException(ExceptionEnum.DONT_OPERATE_ADMIN);
            }
            //在自己的数据范围内，可以修改
            for (Integer deptId : MyDataScope) {
                if (deptId.equals(otherUser.getDeptid())) {
                    return true;
                }
            }
            //不在自己的数据范围内，不可以修改
            throw new CustomException(ExceptionEnum.NO_PERMISSION);

        } else {
            //自己不是管理员，不能修改别人信息
            throw new CustomException(ExceptionEnum.NO_PERMISSION);
        }

    }

}
