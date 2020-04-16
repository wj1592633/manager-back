package com.wj.manager.controller.warpper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.constant.ConstantFactory;

import java.util.List;
import java.util.Map;

public class SysRoleWarpper extends BaseWarpper {

    public SysRoleWarpper(IPage<Map<String,Object>>  page){
        super(page);
    }
    public SysRoleWarpper(List<Map<String,Object>> multi){
        super(multi);
    }
    public SysRoleWarpper(Map<String,Object>  single){
        super(single);
    }
    @Override
    protected void wrapTheMap(Map<String, Object> entity) {
        entity.put("statusName", ConstantFactory.instance().getDictNameByCodeAndNum(DictCodeEnum.SYS_STATE,(Integer)entity.get("status")));
        entity.put("pName", ConstantFactory.instance().getRoleNameByPid((Integer) entity.get("pid")));
        entity.put("deptName", ConstantFactory.instance().getDeptName((Integer) entity.get("deptid")));
    }
}
