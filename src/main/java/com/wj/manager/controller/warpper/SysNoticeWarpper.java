package com.wj.manager.controller.warpper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.constant.ConstantFactory;

import java.util.List;
import java.util.Map;

public class SysNoticeWarpper extends BaseWarpper {

    public SysNoticeWarpper(IPage<Map<String,Object>>  page){
        super(page);
    }
    public SysNoticeWarpper(List<Map<String,Object>> multi){
        super(multi);
    }
    public SysNoticeWarpper(Map<String,Object>  single){
        super(single);
    }
    @Override
    protected void wrapTheMap(Map<String, Object> entity) {
        entity.put("createrName", ConstantFactory.instance().getUserNameById((Integer) entity.get("creater")));
        entity.put("isMenuName", ((Integer)entity.get("ismenu"))==1?"是":"否");
    }
}
