package com.wj.manager.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mb.security")
@EnableConfigurationProperties //记得加上这个注解
@Component
public class MBSecurityProperties {
    private String clientId;
    private String clientSecret;
    private String getTokenUrl;
    private String signingKey;
    private int expire = 14400; //token有效时间，默认4小时
    private int refreshExpire = 1800; //refresToken有效时间
    private String[] redirectUris;
    private String[] grantTypes;
    private String[] scopes;
    private String[] anonUri;
    private String keyStorePath;
    private String keyStorePass;
    private String keyPairAlias;
    private String keyPairPass;
    private String refreshTokenKey = "refresh_jwt";


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGetTokenUrl() {
        return getTokenUrl;
    }

    public void setGetTokenUrl(String getTokenUrl) {
        this.getTokenUrl = getTokenUrl;
    }

    public String[] getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String[] redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String[] getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String[] grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public String getKeyPairAlias() {
        return keyPairAlias;
    }

    public void setKeyPairAlias(String keyPairAlias) {
        this.keyPairAlias = keyPairAlias;
    }

    public String getKeyPairPass() {
        return keyPairPass;
    }

    public void setKeyPairPass(String keyPairPass) {
        this.keyPairPass = keyPairPass;
    }

    public int getRefreshExpire() {
        return refreshExpire;
    }

    public void setRefreshExpire(int refreshExpire) {
        this.refreshExpire = refreshExpire;
    }

    public String getRefreshTokenKey() {
        return refreshTokenKey;
    }

    public void setRefreshTokenKey(String refreshTokenKey) {
        this.refreshTokenKey = refreshTokenKey;
    }

    public String[] getAnonUri() {
        return anonUri;
    }

    public void setAnonUri(String[] anonUri) {
        this.anonUri = anonUri;
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }
}
