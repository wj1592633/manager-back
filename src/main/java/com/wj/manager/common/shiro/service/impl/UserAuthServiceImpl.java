package com.wj.manager.common.shiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.shiro.service.UserAuthService;
import com.wj.manager.common.util.SpringContextHolder;
import com.wj.manager.mapper.SysMenuMapper;
import com.wj.manager.mapper.SysUserMapper;
import com.wj.manager.pojo.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 该类依赖springContextHolder原因：
 *      本类是为ShiroUserRealm提供服务的，ShiroUserRealm类会被shiro内部调用，
 *      人家的东西最好让shiro自己来管理，不把ShiroUserRealm类扫描进spring中
 *      所以ShiroUserRealm类想持有UserAuthServiceImpl类，可以通过instance()方法来拿到实例
 */

@Service
@Transactional(readOnly = true)
@DependsOn("springContextHolder")
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    SysUserMapper userMapper;
    @Autowired
    SysMenuMapper menuMapper;


    public static UserAuthService instance(){
      return   SpringContextHolder.getBean(UserAuthServiceImpl.class);
    }

    @Override
    public SysUser checkUserByAccount(String userName) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("ACCOUNT",userName)
                .last(" and status <> 3");
        return userMapper.selectOne(wrapper);
    }

    /*@Override
    public ShiroUser sysUser2ShiroUser(SysUser sysUser) {
        ShiroUser shiroUser = new ShiroUser();
        if(sysUser != null ){
            shiroUser.setId(sysUser.getId());
            shiroUser.setAccount(sysUser.getAccount());
            shiroUser.setDeptId(sysUser.getDeptid());
            shiroUser.setName(sysUser.getName());
            shiroUser.setDeptName(ConstantFactory.instance().getDeptName(sysUser.getDeptid()));
            String roleids = sysUser.getRoleid();
            List<Integer> roleidList = new ArrayList<>();
            List<String> roleNameList = new ArrayList<>();
            //角色id在Sys_user表中是'1,5,6'的格式存在的
            if(StringUtils.isNoneBlank(roleids)){
                String[] split = roleids.split(",");
                for(String roleid : split){
                    //这层不为空是必须的，万一 ',5,6' 切割除的数据会有空值
                    if(StringUtils.isNoneBlank(roleid)) {
                        Integer roleId = Integer.parseInt(roleid);
                        roleidList.add(roleId);
                        roleNameList.add(ConstantFactory.instance().getSingleRoleName(roleId));
                    }
                }
            }
            shiroUser.setRoleList(roleidList);
            shiroUser.setRoleNames(roleNameList);
        }
        return shiroUser;
    }*/

    @Override
    public List<String> findPermissionsByRoleId(Integer roleId) {
        return menuMapper.getResUrlsByRoleId(roleId);
    }

    @Override
    public List<Integer> findRolesByAccount(String account) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("account",account);
        SysUser user = userMapper.selectOne(wrapper);
        if(user != null){
            ArrayList<Integer>  RoleIds = new ArrayList<>();
            String roleids = user.getRoleid();
            if(StringUtils.isBlank(roleids)){
                return null;
            }
            String[] rids = roleids.split(",");
            if(rids != null && rids.length>0){
                for (String rid :rids){
                    RoleIds.add(Integer.parseInt(rid));
                }
            }
            return RoleIds;
        }
        return null;
    }
}
