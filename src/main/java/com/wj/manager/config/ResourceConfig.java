package com.wj.manager.config;

import com.wj.manager.common.properties.MBSecurityProperties;
import com.wj.manager.common.util.SpringContextHolder;
import com.wj.manager.security.RefrshAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 资源服务器，EnableResourceServer相当添加了一个filter
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "restservice";

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    MBSecurityProperties properties;

    @Bean
    public OAuth2AuthenticationEntryPoint refrshAuthenticationEntryPoint(){
        RefrshAuthenticationEntryPoint entryPoint = new RefrshAuthenticationEntryPoint();
        entryPoint.setProperties(SpringContextHolder.getBean(MBSecurityProperties.class));
        return entryPoint;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .authenticationEntryPoint(refrshAuthenticationEntryPoint())
                .resourceId(RESOURCE_ID).tokenStore(tokenStore);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                //下边的路径放行
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui",
                        "/swagger-resources","/swagger-resources/configuration/security",
                        "/swagger-ui.html","/webjars/**","/course/coursepic/list/**","/course/courseview/**").permitAll()
                .antMatchers(properties.getAnonUri()).permitAll()
                .antMatchers("/oauth/token","/oauth/check_token","/oauth/**").permitAll()
                .anyRequest().authenticated();
                //.anyRequest().permitAll();
    }

}