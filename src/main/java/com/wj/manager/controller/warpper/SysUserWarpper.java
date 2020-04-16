package com.wj.manager.controller.warpper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.pojo.SysUser;

import java.util.List;
import java.util.Map;

public class SysUserWarpper extends BaseWarpper {

    public SysUserWarpper(IPage<Map<String,Object>>  page){
        super(page);
    }
    public SysUserWarpper(List<Map<String,Object>> multi){
        super(multi);
    }
    public SysUserWarpper(Map<String,Object>  single){
        super(single);
    }
    @Override
    protected void wrapTheMap(Map<String, Object> entity) {
        entity.put("password","");
        entity.put("_checked",false);
        entity.put("sexName", ConstantFactory.instance().getDictNameByCodeAndNum(DictCodeEnum.SEX,(Integer) entity.get("sex")));
        entity.put("roleName", ConstantFactory.instance().getRoleName((String) entity.get("roleid")));
        entity.put("deptName", ConstantFactory.instance().getDeptName((Integer) entity.get("deptid")));
        entity.put("statusName", ConstantFactory.instance().getDictNameByCodeAndNum(DictCodeEnum.ACCOUNT_STATE,(Integer)entity.get("status")));
    }
}
