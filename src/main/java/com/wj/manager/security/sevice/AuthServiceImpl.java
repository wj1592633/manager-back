package com.wj.manager.security.sevice;

import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.constant.EhCacheConst;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.properties.MBSecurityProperties;
import com.wj.manager.common.util.ToolUtil;
import com.wj.manager.mapper.SysUserMapper;
import com.wj.manager.security.SecurityKit;
import com.wj.manager.security.vo.AuthToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    MBSecurityProperties properties;
    @Autowired
    SysUserMapper userMapper;
    @Autowired
    PasswordEncoder encoder;

    @Override
    public AuthToken login(String username, String password) {
        try {
            AuthToken authToken = applyToken(username, password);
            return authToken;
        }catch (Exception e){
            //密码错误，有可能是自己账号但密码错。也可能是误输入别人账号，密码对不上，提示账号错误
            if(e.getMessage().indexOf("Bad credentials") != -1){
                throw new CustomException(ExceptionEnum.PWD_OR_ACC_ERROR);
            }else if(e.getMessage().indexOf("server authentication") != -1){
                //查不到账号
                throw new CustomException(ExceptionEnum.ERROR_ACCOUNT);
            }
            throw e;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = EhCacheConst.CACHE_CONSTANT,key = "'userSalt'+ #userid")
    public Integer logout(Integer userid) {
        Integer version = userMapper.getVersionByUserId(userid);
        if(null == version){
            throw new CustomException(ExceptionEnum.USER_NO_EXSIT);
        }
        String slat = ToolUtil.generatorRandomSlat();
        Integer row = userMapper.updateSaltByUserId(userid, slat, version);
        return row;
    }

    @Override
    public boolean unlock(Integer userId, String password) {
        if (StringUtils.isBlank(password)){
            throw new CustomException(ExceptionEnum.PWD_NOT_RIGHT);
        }
        if(null == userId){
            throw new CustomException(ExceptionEnum.NULL_DATA);
        }
        String passwordDB = userMapper.getPasswordByUserId(userId);
        if(StringUtils.isBlank(passwordDB)){
            throw new CustomException(ExceptionEnum.USER_NO_EXSIT);
        }
        return encoder.matches(password,passwordDB);
    }


    /**
     *密码方式申请令牌
     */
    private AuthToken applyToken(String username, String password){
        //restTemplate用于发送远程请求
        RestTemplate restTemplate = new RestTemplate();
        //SpringSecurity认证错误时，错误码为401或400，restTemplate发现错误就不执行，拿不回结果，
        //所以得设置错误处理setErrorHandler
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != 400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });

        //头部带的参数
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        //base64编码client_id和client_secret，oauth2必须带上这2个，认证服务器才能知道时哪个应用在请求
        String basic = getBasic(properties.getClientId(), properties.getClientSecret());
        header.add("Authorization",basic);

        //body带的参数
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        //参数固定这么写
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);
        //scope,可以根据需求变动
        body.add("scope",properties.getScopes()[0]);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, header);
        //发送远程请求
        ResponseEntity<Map> exchange = restTemplate.exchange(properties.getGetTokenUrl(), HttpMethod.POST, entity, Map.class);
        Map jwtMap = exchange.getBody();
        //jwtMap认证错误时，access_token等都为空，但会有error_description
        if(jwtMap == null ||
                jwtMap.get("access_token") == null ||
                jwtMap.get("refresh_token") == null ||
                jwtMap.get("jti") == null) {
            //解析spring security返回的错误信息
            if (jwtMap != null && jwtMap.get("error_description") != null) {
                String error_description = (String) jwtMap.get("error_description");
                if (StringUtils.isNoneBlank(error_description)) {
                    if (error_description.indexOf("disabled") != -1) {
                        throw new CustomException(ExceptionEnum.USER_INVALID);
                    }else if(error_description.indexOf("locked") != -1){
                        throw new CustomException(ExceptionEnum.USER_FREEZE);
                    }
                }

            }
        }
        AuthToken authToken = new AuthToken();
        authToken.setJwtToken((String)jwtMap.get("access_token"));
        authToken.setRefreshToken((String)jwtMap.get("refresh_token"));
        authToken.setTokenKey((String) jwtMap.get("jti"));
        return authToken;
    }

    /**
     * 访问/oauth/token时，头部要带上base64编码后的clientId和clientSecret
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String getBasic(String clientId,String clientSecret){
        String value = clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic "+new String(encode);
    }
}
