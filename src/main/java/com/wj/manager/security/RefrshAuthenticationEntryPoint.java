package com.wj.manager.security;

import com.alibaba.fastjson.JSON;
import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.common.properties.MBSecurityProperties;
import com.wj.manager.security.vo.AuthToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RefrshAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {
    private WebResponseExceptionTranslator<?> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    MBSecurityProperties properties;


    public MBSecurityProperties getProperties() {
        return properties;
    }

    public void setProperties(MBSecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        try {
            ResponseEntity<?> result = this.exceptionTranslator.translate(authException);
            String refreshJwt = request.getHeader(properties.getRefreshTokenKey());
            //accessToken才需要带Bearer，刷新token不需要。但是前端传来的刷新token带了，现在切割(刷新token貌似前端没必要带上Bearer)
            refreshJwt = StringUtils.substringAfter(refreshJwt, "Bearer ");
            HttpStatus statusCode = result.getStatusCode();
            HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
            System.out.println(result.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
            System.out.println(result.getStatusCode() == HttpStatus.UNAUTHORIZED);

            if (result.getStatusCode() == HttpStatus.UNAUTHORIZED && StringUtils.isNoneBlank(refreshJwt)) {
                MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
                body.add("grant_type", "refresh_token");
                body.add("refresh_token",refreshJwt);
                //scope不该这么写的，应该从原来的accessToken中取
                body.add("scope",properties.getScopes()[0]);

                //头部带的参数
                LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
                //base64编码client_id和client_secret，oauth2必须带上这2个，认证服务器才能知道时哪个应用在请求
                String basic = getBasic(properties.getClientId(), properties.getClientSecret());
                header.add("Authorization",basic);

                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, header);
                //发送远程请求
                ResponseEntity<Map> exchange = restTemplate.exchange(properties.getGetTokenUrl(), HttpMethod.POST, entity, Map.class);
                Map jwtMap = exchange.getBody();

                if(jwtMap.get("error") != null){
                    returnTips(response,ResponseResult.login("登陆已经失效，请重新登陆"));
                    //如果是网页,跳转到登陆页面
                    //response.sendRedirect("login");
                }else{
                    //如果刷新成功
                    Map<String,String> map = new HashMap<>();
                    map.put("accessToken",(String)jwtMap.get("access_token"));
                    map.put("refreshToken",(String)jwtMap.get("refresh_token"));
                    map.put("jti",(String)jwtMap.get("jti"));
                    map.put("url",request.getRequestURL().toString());

                    returnTips(response,ResponseResult.saveAndForward(map));
                }

            }else {
                returnTips(response,ResponseResult.login("登陆已经失效，请重新登陆"));
                }
        } catch (Exception e){
            e.printStackTrace();
            returnTips(response,ResponseResult.login("登陆已经失效，请重新登陆"));
        }
    }

    /*private void reflectAddHeader(HttpServletRequest request,String key,String value) throws Exception{
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        System.out.println("request实现类="+requestClass.getName());
        Field[] declaredFields = requestClass.getDeclaredFields();

        Field request1 = requestClass.getDeclaredField("request");

        request1.setAccessible(true);
        Object o = request1.get(request);
        Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
        coyoteRequest.setAccessible(true);
        Object o1 = coyoteRequest.get(o);
        System.out.println("coyoteRequest实现类="+o1.getClass().getName());
        Field headers = o1.getClass().getDeclaredField("headers");
        headers.setAccessible(true);
        MimeHeaders o2 = (MimeHeaders)headers.get(o1);
        o2.addValue(key).setString("Bearer "+value);
    }*/

    private String getBasic(String clientId,String clientSecret){
        String value = clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic "+new String(encode);
    }

    private void returnTips(HttpServletResponse response,ResponseResult result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String msg = JSON.toJSONString(result);
        ServletOutputStream stream = response.getOutputStream();
        stream.write(msg.getBytes());
        stream.flush();
    }

}
