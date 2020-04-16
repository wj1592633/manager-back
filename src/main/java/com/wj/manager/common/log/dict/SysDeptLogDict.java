package com.wj.manager.common.log.dict;

import com.wj.manager.common.log.constance.LogConst;
import org.springframework.stereotype.Component;

@Component(LogConst.SYS_DEPT_DICT)
public class SysDeptLogDict extends AbstractLogDict {

    @Override
    public void initSpecialField() {
        specialField = "pid,status";
    }

    @Override
    public void initDictory() {
        put("deptid", "部门名称");
        put("num", "部门排序");
        put("pid", "上级名称");
        put("simplename", "部门简称");
        put("fullname", "部门全称");
        put("tips", "备注");
    }

    @Override
    public void initSpecialFieldDictory() {
        putSpecialFieldMethodName("pid","getDeptName");
        putSpecialFieldMethodName("status","getSystemStatus");
    }


}
