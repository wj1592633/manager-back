package com.wj.manager.common.shiro.service;

import com.wj.manager.pojo.SysUser;

import java.util.List;
import java.util.Map;

public interface UserAuthService {
    /**
     * 根据用户账号查询用户
     * @param userName 用户账号
     * @return 用户信息
     */
    public SysUser checkUserByAccount(String userName);

    /**
     * 把查询得到的SysUser进行封装，会处理一些数据，比如敏感信息：密码
     * @param sysUser 原生的SysUser实例
     * @return ShiroUser  SysUser处理后的实例
     */
    //改改public ShiroUser sysUser2ShiroUser(SysUser sysUser);

    /**
     *
     * @param roleId
     * @return
     */
    public List<String> findPermissionsByRoleId(Integer roleId);

    /**
     * 根据用户账号查询用户的角色
     * @param account
     * @return
     */
    public List<Integer> findRolesByAccount(String account);

}
