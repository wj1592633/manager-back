package com.wj.manager.common.log.dict;

import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.log.constance.LogConst;
import com.wj.manager.common.util.SpringContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component(LogConst.SYS_USER_DICT)
public class SysUserLogDict extends AbstractLogDict {

    @Override
    public void initSpecialField() {
        specialField = "roleid,deptid,sex,status";
    }

    @Override
    public void initDictory() {
        //这里的英文字段要和SysUser的字段相同
        put("userid","用户id");
        put("avatar","头像");
        put("account","账号");
        put("name","名字");
        put("birthday","生日");
        put("sex","性别");
        put("password","密码");
        put("email","电子邮件");
        put("phone","电话");
        put("status","状态");
        put("roleid","角色名称");
        put("deptid","部门名称");
        put("roleids","角色名称");
    }

    @Override
    public void initSpecialFieldDictory() {
        putSpecialFieldMethodName("roleid","getRoleNameByIds");
        putSpecialFieldMethodName("deptid","getDeptName");
        putSpecialFieldMethodName("sex","getSex");
        putSpecialFieldMethodName("status","getAccountStatus");
    }


}
