package com.wj.manager.security.vo;

public class AuthToken {
    //jwt令牌(用户携带这个)
    private String jwtToken;
    //刷新令牌
    private String refreshToken;
    //最短得令牌
    private String tokenKey;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
