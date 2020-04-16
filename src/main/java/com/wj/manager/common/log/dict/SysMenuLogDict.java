package com.wj.manager.common.log.dict;

import com.wj.manager.common.log.constance.LogConst;
import org.springframework.stereotype.Component;

@Component(LogConst.SYS_MUENU_DICT)
public class SysMenuLogDict extends AbstractLogDict {

    @Override
    public void initSpecialField() {
        specialField = "isopen,status";
    }

    @Override
    public void initDictory() {
        put("menuId", "菜单id");
        put("id", "菜单id");
        put("code", "菜单编号");
        put("pcode", "菜单父编号");
        put("name", "菜单名称");
        put("icon", "菜单图标");
        put("url", "url地址");
        put("num", "菜单排序号");
        put("levels", "菜单层级");
        put("tips", "备注");
        put("status", "菜单状态");
        put("isopen", "是否打开");
        put("", "");
    }

    @Override
    public void initSpecialFieldDictory() {
        putSpecialFieldMethodName("isopen","getIsOpenName");
        putSpecialFieldMethodName("status","getAccountStatus");
    }


}
