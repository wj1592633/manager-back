package com.wj.manager.common.log.dict;

import com.wj.manager.common.log.constance.LogConst;
import org.springframework.stereotype.Component;

@Component(LogConst.SYS_ROLE_DICT)
public class SysRoleLogDict extends AbstractLogDict {

    @Override
    public void initSpecialField() {
        specialField = "pid,deptid,status";
    }

    @Override
    public void initDictory() {
        put("roleid", "角色编号");
        put("num", "角色排序");
        put("pid", "角色的父级");
        put("name", "角色名称");
        put("deptid", "部门名称");
        put("tips", "备注");
        put("status", "状态");
        put("ids", "资源名称");
    }

    @Override
    public void initSpecialFieldDictory() {
        putSpecialFieldMethodName("pid","getRoleNameByIntId");
        putSpecialFieldMethodName("deptid","getDeptName");
        putSpecialFieldMethodName("status","getSystemStatus");
    }


}
