package com.wj.manager.controller.warpper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.constant.ConstantFactory;

import java.util.List;
import java.util.Map;

public class SysMenuWarpper extends BaseWarpper {
    public SysMenuWarpper(IPage<Map<String,Object>> page){
        super(page);
    }
    public SysMenuWarpper(List<Map<String,Object>> multi){
        super(multi);
    }
    public SysMenuWarpper(Map<String,Object>  single){
        super(single);
    }
    @Override
    protected void wrapTheMap(Map<String, Object> entity) {
        entity.put("parent", ConstantFactory.instance().getMenuNameByCode((String)entity.get("pcode")));
        entity.put("statusName", ConstantFactory.instance().getDictNameByCodeAndNum(DictCodeEnum.SYS_STATE,(Integer)entity.get("status")));
    }
}
