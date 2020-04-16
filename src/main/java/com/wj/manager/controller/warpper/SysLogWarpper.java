package com.wj.manager.controller.warpper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.constant.ConstantFactory;

import java.util.List;
import java.util.Map;

public class SysLogWarpper extends BaseWarpper {

    public SysLogWarpper(IPage<Map<String,Object>>  page){
        super(page);
    }
    public SysLogWarpper(List<Map<String,Object>> multi){
        super(multi);
    }
    public SysLogWarpper(Map<String,Object>  single){
        super(single);
    }
    @Override
    protected void wrapTheMap(Map<String, Object> entity) {
        if(entity.keySet().contains("userid")){
            entity.put("userName",ConstantFactory.instance().getUserNameById((Integer) entity.get("userid")));
        }
        String message = (String) entity.get("message");

        //如果信息过长,则只截取前100位字符串
        if (message != null && message.length() >= 100) {
            String subMessage = message.substring(0, 100) + "...";
            entity.put("message", subMessage);
        }

        //如果信息中包含分割符号;;;   则分割字符串返给前台
        if (message != null && message.indexOf(";;;") != -1) {
            String[] msgs = message.split(";;;");
            entity.put("regularMessage", msgs);
        } else {
            entity.put("regularMessage", message);
        }

    }
}
