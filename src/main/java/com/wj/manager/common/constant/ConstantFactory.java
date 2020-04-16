package com.wj.manager.common.constant;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.protocol.x.Notice;
import com.sun.tools.javac.util.Convert;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.dto.DictAllDto;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.util.SpringContextHolder;
import com.wj.manager.mapper.*;
import com.wj.manager.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@PropertySource("classpath:const.properties")
@ConfigurationProperties(prefix = "const")
@DependsOn("springContextHolder")
public class ConstantFactory {
    public static final Integer SYS_STATE_ENABLE = 1 ;
    public static final Integer SYS_STATE_DISABLE  = 2 ;
    public static final Integer SYS_SEX_MALE = 1;
    public static final Integer SYS_SEX_FEMALE = 2;
    public static final Integer ACCOUNT_STATE_ENABLE = 1;
    public static final Integer ACCOUNT_STATE_FREEZE = 2;
    public static final Integer ACCOUNT_STATE_DELETE = 3;
    public String ADMIN_NAME ; //管理员标识符
    public String ROLE_SEPERATOR = ",";
    public String DEFAULT_PWD;//默认密码
    public String DEFAULT_USER_NAME;//默认用户名
    public Integer USER_UNFREEZE;
    public Integer USER_DISABLE;
    public Integer USER_FREEZE;
    public String USER_AVATAR_DIR; //用户头像存储路径
    private List<String> anonUri;
    public String contextPath = "/manager-boot";
    private SysUserMapper userMapper = SpringContextHolder.getBean(SysUserMapper.class );
    private SysRoleMapper roleMapper = SpringContextHolder.getBean(SysRoleMapper .class );
    private SysDeptMapper deptMapper = SpringContextHolder.getBean(SysDeptMapper.class );
    private SysDictMapper dictMapper = SpringContextHolder.getBean(SysDictMapper.class );
    private SysRelationMapper relationMapper = SpringContextHolder.getBean(SysRelationMapper.class );
    private SysMenuMapper  menuMapper  = SpringContextHolder.getBean(SysMenuMapper .class );
    private SysNoticeMapper noticeMapper  = SpringContextHolder.getBean(SysNoticeMapper .class );

    public static ConstantFactory instance(){
        return SpringContextHolder.getBean(ConstantFactory.class);
    }

    /**
     * 根据用户id获取用户名字
     * @param userId 用户id
     * @return 用户名字
     */
    public String getUserNameById(Integer userId) {
        SysUser user = userMapper.selectById(userId);
        if (user != null) {
            return user.getName();
        } else {
            return "--";
        }
    }

    /**
     * 根据用户id获取角色tips
     * @param userId 用户id
     * @return tips信息
     */
    public String getRoleTipsByUserId(Integer userId){
        return roleMapper.getRoleTipsByUserId(userId);
    }

    /**
     * 根据用户id获取用户账号
     *
     */
    public String getUserAccountById(Integer userId) {
        SysUser user = userMapper.selectById(userId);
        if (user != null) {
            return user.getAccount();
        } else {
            return "--";
        }
    }
    /**
     * 根据用户id获取其菜单，即权限
     * @param userId
     * @return
     */
    public Set<String> getMenuUrlsByUserId(Integer userId){
        if(userId == null){
            return null;
        }
        List<SysRole> roles = getRolesByUserId(userId);
        HashSet<String> urlSet = new HashSet<>();
        if(roles != null && roles.size() > 0 ){
            for(SysRole role : roles){
                List<String> urls = menuMapper.getResUrlsByRoleId(role.getId());
                if(urls != null && urls.size() >0){
                    for(String url : urls){
                        urlSet.add(url);
                    }
                }
            }
            return urlSet;
        }else {
            return null;
        }
    }


    /**
     * 通过角色ids获取角色名称
     */
    //todo
    public String getRoleName(String... ids){
        if(ids.length<0 && ids == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(String id : ids){
            SysRole sysRole = roleMapper.selectById(Integer.parseInt(id));
            if(sysRole != null && StringUtils.isNoneBlank(sysRole.getName())) {
                sb.append(sysRole.getName()).append(",");
            }
        }
       return StringUtils.removeEnd(sb.toString(),",");
    }



    /**
     * 通过角色ids获取角色名称
     */
    //todo
    public String getRoleName(String ids){
        if(StringUtils.isBlank(ids)){
            return "";
        }
        String[] roleIds = ids.split(",");
       return getRoleName(roleIds);
    }

    public String getRoleNameByIds(String ids){
        if(StringUtils.isBlank(ids)){
            return "";
        }
        String[] roleIds = ids.split(",");
        return getRoleName(roleIds);
    }

    public String getRoleNameByIntId(Integer id){
       if(null == id){
           return "";
       }
        SysRole sysRole = roleMapper.selectById(id);
       if (sysRole != null && StringUtils.isNoneBlank(sysRole.getName())){
           return sysRole.getName();
       }
       return "";
    }


    /**
     * 根据用户id获取其角色
     * @param userId
     * @return
     */
    public List<SysRole> getRolesByUserId(Integer userId){
        if(userId == null){
            return null;
        }
        ArrayList<SysRole> list = new ArrayList<>();
        SysUser sysUser = userMapper.selectById(userId);
        String roleid = sysUser.getRoleid();
        if(StringUtils.isNoneBlank(roleid)){
            String[] roleids = roleid.split(",");
            if(roleids !=null && roleids.length > 0){
                for(String id : roleids){
                    SysRole sysRole = roleMapper.selectById(Integer.parseInt(id));
                    if(sysRole != null) {
                        list.add(sysRole);
                    }
                }
            }
            return list;
        }else {
            return null;
        }
    }


    /**
     * 根据id = pid查询角色名称
     * @param pid
     * @return
     */
    public String getRoleNameByPid(Integer pid){
        if(pid == null){
            return null;
        }
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("id",pid);
        SysRole pRole = roleMapper.selectOne(wrapper);
        if(pRole != null){
            return pRole.getName();
        }else {
            return "--";
        }

    }
    public SysRole getRoleByRoleId(Integer roleId){
        if(roleId == null){
            return null;
        }
        return roleMapper.selectById(roleId);
    }

    /**
     * 通过角色id获取角色名称
     */
    //todo
    public String getSingleRoleName(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        SysRole sysRole = roleMapper.selectById(roleId);
        if(sysRole != null && StringUtils.isNoneBlank(sysRole.getName())) {
            return sysRole.getName();
        }
        return "";
    }

    /**
     * 通过角色id获取角色tips
     */
    //todo
    public String getSingleRoleTip(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        SysRole sysRole = roleMapper.selectById(roleId);
        if(sysRole != null && StringUtils.isNoneBlank(sysRole.getName())) {
            return sysRole.getTips();
        }
        return "";
    }

    /**
     * 获取部门名称
     */
    //todo
    public String getDeptName(Integer deptId) {
        if(deptId == null){
            return null;
        }
        SysDept sysDept = deptMapper.selectById(deptId);
        if(sysDept != null && StringUtils.isNoneBlank(sysDept.getFullname())) {
            return sysDept.getFullname();
        }
        return "";
    }

    /**
     * 获取菜单的名称(多个)
     */
    public String getMenuNames(String menuIds) {
        if(StringUtils.isBlank(menuIds)){
            return "";
        }
        String[] menus = menuIds.split(",");
        StringBuilder sb = new StringBuilder();
        for (String menu : menus) {
            SysMenu sysMenu = menuMapper.selectById(Integer.parseInt(menu));
            if (sysMenu != null && StringUtils.isNoneBlank(sysMenu.getName()) ) {
                sb.append(sysMenu.getName()).append(",");
            }
        }
        return StringUtils.removeEnd(sb.toString(),",");
    }

    public String getIsOpenName(Integer value){
        if (null == value){
            return "";
        }else if(value.intValue() == 1){
            return "是";
        }else {
            return "不是";
        }
    }
    /**
     * 获取菜单名称
     */
    public String getMenuName(Long menuId) {
        if(menuId == null){
            return "";
        }
        SysMenu menu = menuMapper.selectById(menuId);
        if (menu == null) {
            return "";
        } else {
            return menu.getName();
        }
    }

    public SysMenu getMenuById(Long menuId){
        if(menuId == null){
            return  null;
        }
        return menuMapper.selectById(menuId);
    }

    /**
     * 获取菜单名称通过编号
     */
    public String getMenuNameByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        } else {
            QueryWrapper<SysMenu> wrapper = new QueryWrapper<SysMenu>();
            wrapper.eq("code",code);
            SysMenu menu = menuMapper.selectOne(wrapper);
            if (menu == null) {
                return "无";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取字典名称
     */
    public String getDictName(Integer dictId) {
        if (dictId == null) {
            return "";
        }
        SysDict dict = dictMapper.selectById(dictId);
        if (dict == null) {
            return "";
        } else {
            return dict.getName();
        }

    }

    /**
     * 获取通知标题
     */
    public String getNoticeTitle(Integer dictId) {
        if (dictId == null) {
            return "";
        } else {
            SysNotice notice = noticeMapper.selectById(dictId);
            if (notice == null) {
                return "";
            } else {
                return notice.getTitle();
            }
        }
    }

    /**
     * 根据字典名称和字典中的值获取对应的名称
     * 可能有问题
     */
    public String getDictsByName(String name, Integer val) {
        QueryWrapper<SysDict> wrapper= new QueryWrapper<>();
        wrapper.eq("NAME",name);
        SysDict dict = dictMapper.selectOne(wrapper);
        if (dict == null) {
            return "";
        } else {
            QueryWrapper<SysDict> wrapper1= new QueryWrapper<>();
            wrapper1 = wrapper1.eq("pid", dict.getId());
            List<SysDict> dicts = dictMapper.selectList(wrapper1);
            for (SysDict item : dicts) {
                if (item.getNum() != null && item.getNum().equals(val)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 获取性别名称
     */
    public String getSexName(Integer sex) {
        return getDictsByName("性别", sex);
    }


    /**
     * 查询除所有的dict数据
     * {dicts:}
     * dictAllDto
     * 结构如下
     * Map<code,Map<num,name>>
     * 可这么取值
     * DictAllDto.getDicts().get("sys_sex").get(1)
     * @return
     */
    //todo
    public DictAllDto getAllDict(){
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.eq("PID",0);
        //获取父级(pid = 0)元素
        List<SysDict> sysDicts = dictMapper.selectList(wrapper);
        Map map = null;
        if(sysDicts != null && sysDicts.size()>0){
            map =new HashMap<>();
            for (SysDict dict : sysDicts){
                QueryWrapper<SysDict> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("PID",dict.getId());
                //查询当前父级元素下的子元素
                List<SysDict> subDicts = dictMapper.selectList(wrapper1);
                if(subDicts != null && subDicts.size()>0) {
                    Map submap = new HashMap<>();
                    for (SysDict subdict : subDicts){
                        submap.put(subdict.getNum(),subdict.getName());
                    }
                    map.put(dict.getCode(),submap);
                }

            }
        }
        DictAllDto dictAllDto = new DictAllDto();
        dictAllDto.setDicts(map);
        return dictAllDto;
    }

    public String getDictNameByCodeAndNum(DictCodeEnum codeEnum,Integer num){
        DictAllDto allDict = getAllDict();
        Map<Integer,String> codeDict = (Map<Integer,String>)allDict.getDicts().get(codeEnum.getCode());
        return (String)codeDict.get(num);
    }

    /**
     * 获取性别
     * @param num
     * @return
     */
    public String getSex(Integer num){
        DictAllDto allDict = getAllDict();
        Map<Integer,String> codeDict = (Map<Integer,String>)allDict.getDicts().get(DictCodeEnum.SEX.getCode());
        return (String)codeDict.get(num);
    }

    /**
     * 获取系统 状态
     * @param num
     * @return
     */
    public String getSystemStatus(Integer num){
        DictAllDto allDict = getAllDict();
        Map<Integer,String> codeDict = (Map<Integer,String>)allDict.getDicts().get(DictCodeEnum.SYS_STATE.getCode());
        return (String)codeDict.get(num);
    }

    /**
     * 获取用户状态
     * @param num
     * @return
     */
    public String getAccountStatus(Integer num){
        DictAllDto allDict = getAllDict();
        Map<Integer,String> codeDict = (Map<Integer,String>)allDict.getDicts().get(DictCodeEnum.ACCOUNT_STATE.getCode());
        return (String)codeDict.get(num);
    }

    /**
     * 获取用户登录状态
     */
    public String getStatusName(Integer status) {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.eq("PID",56)
                .eq("NUM",status);
        SysDict dict = dictMapper.selectOne(wrapper);
        if(dict == null){
            throw new CustomException(ExceptionEnum.NOT_EXIST);
        }
        return dict.getName();
    }

    /**
     * 获取菜单状态
     */
    public String getMenuStatusName(Integer status) {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.eq("PID",53)
                .eq("NUM",status);
        SysDict sysDict = dictMapper.selectOne(wrapper);
        if(sysDict == null){
            throw new CustomException(ExceptionEnum.NOT_EXIST);
        }
        return sysDict.getName();
    }

    /**
     * 查询指定父级字典下的所有 子字典
     * @id 父级id
     */
    public List<SysDict> findInDict(Integer id) {
        if (id == null) {
            return null;
        } else {
            QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
            List<SysDict> dicts = dictMapper.selectList(wrapper.eq("pid", id));
            if (dicts == null || dicts.size() == 0) {
                return null;
            } else {
                return dicts;
            }
        }
    }

    /**
     * 获取子部门id
     */
    public List<Integer> getSubDeptId(Integer deptid) {
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper = wrapper.like("pids", "%[" + deptid + "]%");
        List<SysDept> depts = this.deptMapper.selectList(wrapper);

        ArrayList<Integer> deptids = new ArrayList<>();

        if (depts != null && depts.size() > 0) {
            for (SysDept dept : depts) {
                deptids.add(dept.getId());
            }
        }
        return deptids;
    }

    /**
     * 获取所有父部门id
     */
    public List<Integer> getParentDeptIds(Integer deptid) {
        SysDept dept = deptMapper.selectById(deptid);
        String pids = dept.getPids();
        String[] split = pids.split(",");
        ArrayList<Integer> parentDeptIds = new ArrayList<>();
        for (String s : split) {
            s = StringUtils.removeStart(s,"[");
            s = StringUtils.removeEnd(s,"]");
            parentDeptIds.add(Integer.valueOf(s));
        }
        return parentDeptIds;
    }

    public String getADMIN_NAME() {
        return ADMIN_NAME;
    }

    public void setADMIN_NAME(String ADMIN_NAME) {
        this.ADMIN_NAME = ADMIN_NAME;
    }

    public String getDEFAULT_PWD() {
        return DEFAULT_PWD;
    }

    public void setDEFAULT_PWD(String DEFAULT_PWD) {
        this.DEFAULT_PWD = DEFAULT_PWD;
    }

    public String getDEFAULT_USER_NAME() {
        return DEFAULT_USER_NAME;
    }

    public void setDEFAULT_USER_NAME(String DEFAULT_USER_NAME) {
        this.DEFAULT_USER_NAME = DEFAULT_USER_NAME;
    }

    public Integer getUSER_UNFREEZE() {
        return USER_UNFREEZE;
    }

    public void setUSER_UNFREEZE(Integer USER_UNFREEZE) {
        this.USER_UNFREEZE = USER_UNFREEZE;
    }

    public Integer getUSER_DISABLE() {
        return USER_DISABLE;
    }

    public void setUSER_DISABLE(Integer USER_DISABLE) {
        this.USER_DISABLE = USER_DISABLE;
    }

    public Integer getUSER_FREEZE() {
        return USER_FREEZE;
    }

    public void setUSER_FREEZE(Integer USER_FREEZE) {
        this.USER_FREEZE = USER_FREEZE;
    }

    public String getUSER_AVATAR_DIR() {
        return USER_AVATAR_DIR;
    }

    public void setUSER_AVATAR_DIR(String USER_AVATAR_DIR) {
        this.USER_AVATAR_DIR = USER_AVATAR_DIR;
    }

    public List<String> getAnonUri() {
        return anonUri;
    }

    public void setAnonUri(List<String> anonUri) {
        this.anonUri = anonUri;
    }
}
