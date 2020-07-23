package com.wj.manager.controller;


import com.wj.manager.common.dto.RelationParams;
import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.service.SysRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * <p>
 * 角色和菜单关联表 前端控制器
 * </p>
 *
 * @author Wj
 */
@Api("权限分配")
@RestController
@RequestMapping("/relation")
public class SysRelationController {
    @Autowired
    SysRelationService sysRelationService;

    @PostMapping("/update")
    @ApiOperation("修改角色的权限")
    @PreAuthorize("hasAuthority('/role/setAuthority')")
    public ResponseResult updataRelations(@ModelAttribute  RelationParams params){
        sysRelationService.updataRelations(params.getRoleId(), params.getMenuIds());
        return ResponseResult.success();
    }
}

