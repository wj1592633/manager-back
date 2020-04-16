package com.wj.manager.common.dto;

import com.wj.manager.pojo.SysMenu;

import java.util.List;

public class MenuDto extends SysMenu{

    private String statusName;

    private List subMenus;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public List getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List subMenus) {
        this.subMenus = subMenus;
    }
}
