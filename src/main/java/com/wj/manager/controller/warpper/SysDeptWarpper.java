package com.wj.manager.controller.warpper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.constant.ConstantFactory;

import java.util.List;
import java.util.Map;

public class SysDeptWarpper extends BaseWarpper {

    public SysDeptWarpper(IPage<Map<String,Object>>  page){
        super(page);
    }
    public SysDeptWarpper(List<Map<String,Object>> multi){
        super(multi);
    }
    public SysDeptWarpper(Map<String,Object>  single){
        super(single);
    }
    @Override
    protected void wrapTheMap(Map<String, Object> entity) {
        Integer pid = (Integer) entity.get("pid");

        if (pid == null || pid.equals(0)) {
            entity.put("pName", "--");
        } else {
            entity.put("pName", ConstantFactory.instance().getDeptName(pid));
        }
        entity.put("statusName", ConstantFactory.instance().getDictNameByCodeAndNum(DictCodeEnum.SYS_STATE,(Integer)entity.get("status")));
    }
}
