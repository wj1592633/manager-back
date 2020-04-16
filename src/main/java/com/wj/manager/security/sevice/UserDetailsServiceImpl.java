package com.wj.manager.security.sevice;

import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.pojo.SysDept;
import com.wj.manager.pojo.SysRole;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.constance.TokenExConstance;
import com.wj.manager.security.vo.JwtUser;
import com.wj.manager.service.SysDeptService;
import com.wj.manager.service.SysMenuService;
import com.wj.manager.service.SysRoleService;
import com.wj.manager.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    SysUserService userService;

    @Autowired
    SysRoleService roleService;
    @Autowired
    SysMenuService menuService;

    @Autowired
    PasswordEncoder passwordEncoder;

      /* @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    SysDeptService deptService;*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //想使用静态数据，可以把下面一行的注释放开，其余的注释,还有CustomUserAuthenticationConverter也要注释
        //return new JwtUser(username,passwordEncoder.encode("111111"),AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN,ROLE_USER,menu"));
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        List<SysUser> users = userService.getUserListByAccount(username);
        if(users == null || users.size() < 1){
            return null;
        }
        SysUser sysUser = users.get(0);
        return sysUser2JwtUser(sysUser);
    }

    private final static String ROLE_IDS = "roleIds";
    private final static String ROLE_NAMES = "roleNames";
    private final static String IF_ADMIN = "ifAdmin";

    private JwtUser sysUser2JwtUser(SysUser sysUser){
        if(sysUser == null){
            return null;
        }
        String roleid = sysUser.getRoleid();
        String permissions = null;
        Set<String> menus = null;
        Map<Object, Object> map = null;
        if(roleid != null) {
             map = getRoleIdsAndNames(roleid);
            List<Integer> roleIds = (List<Integer>) map.get(ROLE_IDS);
            //查询出用户的所有权限
            if (roleIds != null && roleIds.size() > 0) {
                menus = new HashSet<>();
                for (Integer roleId : roleIds) {
                    List<String> menu = menuService.getMenuUrlByRoleId(roleId);
                    menus.addAll(menu);
                }
                permissions = StringUtils.join(menus.toArray(), ",");
            }
        }
        Integer status = sysUser.getStatus();

        JwtUser jwtUser = new JwtUser(sysUser.getAccount(),sysUser.getPassword(),status ==1 ,
                true ,true, status != 2,
                AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

        jwtUser.setAccount(sysUser.getAccount());
        jwtUser.setId(sysUser.getId());
        jwtUser.setNote(sysUser.getSalt());

        if(sysUser.getDeptid() != null){
            jwtUser.setDeptid(sysUser.getDeptid());
            jwtUser.setDeptScope(ConstantFactory.instance().getSubDeptId(sysUser.getDeptid()));
        }
        if(map != null && map.get(IF_ADMIN) != null){
            jwtUser.setIfAdmin((boolean)map.get(IF_ADMIN));
        }
        return jwtUser;

       /*
        jwtUser.setAvatar(sysUser.getAvatar());
        jwtUser.setName(sysUser.getName());
        SysDept dept = deptService.getById(jwtUser.getDeptid());
        if(dept != null && StringUtils.isNoneBlank(dept.getFullname())){
            jwtUser.setDeptName(dept.getFullname());
        }
        List<String> roleNames = (List<String>)map.get("roleNames");
        jwtUser.setRoleIds(roleIds);
        jwtUser.setRoleNames(roleNames);
        */

    }

    /**
     * 获取角色id和名称
     * @param roleIds
     * @return
     */
    private Map<Object, Object> getRoleIdsAndNames(String roleIds){
        List<Integer> roleIdsList = new ArrayList<Integer>();
        List<String> roleNamesList = new ArrayList<String>();
        boolean isAdmin = false;
        if(StringUtils.isNoneBlank(roleIds)){
            String[] ids = roleIds.split(ConstantFactory.instance().ROLE_SEPERATOR);
            if(ids != null && ids.length >0){
                for (String id : ids){
                    int i = Integer.parseInt(id);
                    roleIdsList.add(i);
                    SysRole role = roleService.getById(i);
                    if (role != null && StringUtils.isNoneBlank(role.getName())) {
                        if (ConstantFactory.instance().ADMIN_NAME.equals(role.getTips())) {
                            isAdmin = true;
                        }
                        roleNamesList.add(role.getName());
                    }
                }
                HashMap<Object, Object> map = new HashMap<>();
                map.put(ROLE_IDS,roleIdsList);
                map.put(ROLE_NAMES,roleNamesList);
                map.put(IF_ADMIN,isAdmin);
                return map;
            }
        }
        return null;
    }

    /**
     * 获取所有的权限，用逗号隔开
     * @param roleIds
     * @return
     *//*
    private String getPermissionsString(List<Integer> roleIds){
        if (roleIds != null && roleIds.size() > 0 ){
            Set<String> menus = new HashSet<>();
            roleIds.forEach((roleId)->{
                List<String> menu = menuService.getMenuUrlByRoleId(roleId);
                menus.addAll(menu);
            });
           return StringUtils.join(menus.toArray(),",");
        }
        return null;
    }*/

    //取出身份，如果身份为空说明没有认证
    //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret

      /*  if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username,clientSecret,AuthorityUtils.commaSeparatedStringToAuthorityList("list"));
            }
        }*/

}
