package com.wj.manager.config;

import com.wj.manager.common.properties.MBSecurityProperties;
import com.wj.manager.security.CustomUserAuthenticationConverter;
import com.wj.manager.security.MyJwtClaimsSetVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableWebSecurity
//@Order(-1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsServiceImpl;
    @Autowired
    MBSecurityProperties properties;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl);
    }
  /*@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/oauth/token","/oauth/check_token");

    }*/

   /* @Override
    public void configure(HttpSecurity http) throws Exception {
       http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
              *//*  .authorizeRequests()

                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui",
                        "/swagger-resources","/swagger-resources/configuration/security",
                        "/swagger-ui.html","/webjars/**","/course/coursepic/list/**","/course/courseview/**").permitAll()
                .anyRequest().authenticated();*//*


    }
*/


    @Autowired
    CustomUserAuthenticationConverter customUserAuthenticationConverter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    @Bean
    public JwtClaimsSetVerifier myJwtClaimsSetVerifier(){
        return new MyJwtClaimsSetVerifier();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // JwtAccessTokenConverter中有个AccessTokenConverter类，可以进行相关配置，设置附加信息到jwt中
        ((DefaultAccessTokenConverter)jwtAccessTokenConverter.getAccessTokenConverter()).setUserTokenConverter(customUserAuthenticationConverter);
        jwtAccessTokenConverter.setSigningKey(properties.getSigningKey());
        jwtAccessTokenConverter.setJwtClaimsSetVerifier(myJwtClaimsSetVerifier());
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

}