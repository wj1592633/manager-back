package com.wj.manager.security.constance;

/**
 * SecurityKit的getFullAuthentication()获得的map的key
 */
public class TokenExConstance {
    public final static String USER_NAME = "user_name"; //当前用户账号
    public final static String ID = "id"; //当前用户id
    public final static String NOTE = "note"; //当前用户标记
    public final static String DEPT_ID = "deptId"; //当前用户部门
    public final static String DEPT_SCOPE = "deptSocpe";//当前用户及子部门，获得的value为List<Integer>
    public final static String AUTHORITIES = "authorities";//当前用户的权限集合,List<String>
    public final static String AUD = "aud"; //client的RESOURCE_ID
    public final static String EXP = "exp"; //token有效时间
    public final static String JTI = "jti"; //token的jti
    public final static String CLIENT_ID = "client_id"; //client_id
    public final static String IS_ADMIN = "ifAdmin";
}
