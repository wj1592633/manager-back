package com.wj.manager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.LogTypeEnum;
import com.wj.manager.common.anotation.BusinessLog;
import com.wj.manager.common.dto.DeptDto;
import com.wj.manager.common.dto.IviewTreeDto;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.common.log.constance.LogConst;
import com.wj.manager.controller.warpper.SysRoleWarpper;
import com.wj.manager.controller.warpper.SysUserWarpper;
import com.wj.manager.pojo.SysRole;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
@Api("角色数据")
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/treelist")
    @ApiOperation("获取所有的角色,给iview的tree组建提供数据")
    public ResponseResult getListTree(){
        List<IviewTreeDto> tree = sysRoleService.getListTreeByPid(0L);
        if(tree != null && tree.size() > 0){
            return ResponseResult.success(tree);
        }
        return ResponseResult.fail();
    }

    @GetMapping("/list")
    @ApiOperation("获取所有的角色,给iview的table组建提供数据")
    @PreAuthorize("hasAuthority('/role')")
    public ResponseResult getRoleList(@ApiParam(name = "queryParams",value = "查询参数",required = true) @ModelAttribute QueryParams<SysRole> queryParams){

        IPage<Map<String, Object>> iPage = sysRoleService.selectRoleList(queryParams);
        if(iPage != null && iPage.getRecords() != null && iPage.getRecords().size() > 0){
            Object warpper = new SysRoleWarpper(iPage).warpper();
            return ResponseResult.success(warpper);
        }
        return ResponseResult.fail("没有你想要的数据");

    }

    @PostMapping("/add")
    @ApiOperation("添加角色")
    @PreAuthorize("hasAuthority('/role/add')")
    @BusinessLog(value = "添加角色",dict = LogConst.SYS_ROLE_DICT,type = LogTypeEnum.OTHER,isWarpper = true)
    public ResponseResult addRole(@ModelAttribute SysRole sysRole){
        int i = sysRoleService.addRole(sysRole);
        if(i == 1 ){
            return ResponseResult.success("添加角色成功");
        }
        return ResponseResult.fail();
    }

    @PostMapping("/edit")
    @ApiOperation("修改角色")
    @PreAuthorize("hasAuthority('/role/edit')")
    @BusinessLog(value = "修改角色",dict = LogConst.SYS_ROLE_DICT,type = LogTypeEnum.UPDATE,isWarpper = true)
    public ResponseResult editRole(@ModelAttribute SysRole sysRole){
        int i = sysRoleService.updataRole(sysRole);
        if(i == 1 ){
            return ResponseResult.success("修改角色成功");
        }
        return ResponseResult.fail();
    }

    @PostMapping("/delete")
    @ApiOperation("删除角色,及其其子角色")
    @PreAuthorize("hasAuthority('/role/remove')")
    @BusinessLog(value = "删除角色",dict = LogConst.SYS_ROLE_DICT,type = LogTypeEnum.OTHER,isWarpper = false)
    public ResponseResult deleteRole(Integer roleid){
        int i = sysRoleService.deleteRole(roleid);
        if(i > 0){
            return ResponseResult.success("删除角色成功");
        }
        return ResponseResult.fail();
    }


}

