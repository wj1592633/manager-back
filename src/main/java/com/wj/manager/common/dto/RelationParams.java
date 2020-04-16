package com.wj.manager.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("权限分配的接收参数")
public class RelationParams {
    @ApiModelProperty(name="roleId", value = "角色id")
    private Integer roleId;

    @ApiModelProperty(name="menuIds", value = "权限id，long类型的数组",dataType ="Long" )
    private Long[] menuIds;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Long[] getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds) {
        this.menuIds = menuIds;
    }
}
