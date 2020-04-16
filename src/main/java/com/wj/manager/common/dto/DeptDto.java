package com.wj.manager.common.dto;

import com.wj.manager.pojo.SysDept;

import java.util.List;

/**
 * 可以使用IviewTreeDto代替
 */
public class DeptDto {
    private Integer deptid;
    private  String title;
    private  boolean expand = false;
    private List children;

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }
}
