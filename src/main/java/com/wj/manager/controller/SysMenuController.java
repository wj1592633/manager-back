package com.wj.manager.controller;


import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.Enum.LogTypeEnum;
import com.wj.manager.common.anotation.BusinessLog;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.constant.EhCacheConst;
import com.wj.manager.common.dto.IviewTreeDto;
import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.log.constance.LogConst;
import com.wj.manager.controller.warpper.SysMenuWarpper;
import com.wj.manager.pojo.SysMenu;
import com.wj.manager.service.SysMenuService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜表 前端控制器
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
@RestController
@RequestMapping("/menu")
@Api("系统菜单数据")
public class SysMenuController {

    @Autowired
    SysMenuService sysMenuService;


    @ApiOperation("查询菜单,用于后台界面左侧")
    @GetMapping("/list")
    @Cacheable(value = EhCacheConst.CACHE_CONSTANT,key = "'getMenuList()'")
    public ResponseResult getMenuList() throws Exception{
        List list = sysMenuService.getMenuListByPcode(null);
        if (list != null){
            return ResponseResult.success(list);
        }
       throw new CustomException(ExceptionEnum.UNKNOWN_EXCEPTION);
    }

    @ApiOperation("查询所有数据，给iviewTree用")
    @GetMapping("/treelist")
    @Cacheable(value = EhCacheConst.CACHE_CONSTANT,key = "'getTreeData()'")
    public ResponseResult getTreeData(@ApiParam  @RequestParam Integer roleid){
        if(null == roleid){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        List<String> urlList = sysMenuService.getMenuUrlByRoleId(roleid);
        List<IviewTreeDto> list = sysMenuService.getListTreeByPCode(urlList,"0");
        if (list != null){
            return ResponseResult.success(list);
        }
       return ResponseResult.fail();
    }

    @ApiOperation("设置menu是否显示,status为1显示，为2不现实。同时也会设置其子菜单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "菜单id",dataType = "integer",paramType = "path"),
            @ApiImplicitParam(name = "status",value = "status为1显示，为2不显示",dataType = "integer",paramType = "path")
    })
    @GetMapping("/show/{id}/{status}")
    @Caching(evict = {
            @CacheEvict(value = EhCacheConst.CACHE_CONSTANT,key = "'getMenuList()'"), //清除缓存
            @CacheEvict(value = EhCacheConst.CACHE_CONSTANT,key = "'getTreeData()'")
    })
    @PreAuthorize("hasAuthority('/menu/edit')")
    @BusinessLog(value="修改菜单状态",isWarpper = false,type = LogTypeEnum.UPDATE,dict = LogConst.SYS_MUENU_DICT)
    public ResponseResult isShowMenu(@PathVariable Integer id, @PathVariable Integer status){
        if(null == id || null == status ){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        int row = sysMenuService.changeStatusById(id,status);
        String msg = ConstantFactory.instance().getMenuStatusName(status);
        if (row == 1){
            return ResponseResult.success(msg+"成功");
        }
        return ResponseResult.fail(msg+"失败");
    }
    @GetMapping("/all")
    @ApiOperation("查询所有的菜单，给菜单管理界面用")
    @PreAuthorize("hasAuthority('/menu')")
    public ResponseResult getAllMenuList(){
        List<Map<String,Object>> menus = sysMenuService.getAllMenuList();
        if (null != menus && menus.size() > 0){
            return ResponseResult.success(new SysMenuWarpper(menus).warpper());
        }
        return null;
    }

}

