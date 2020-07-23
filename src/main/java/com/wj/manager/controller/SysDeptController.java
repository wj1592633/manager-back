package com.wj.manager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.Enum.LogTypeEnum;
import com.wj.manager.common.anotation.BusinessLog;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.dto.DeptDto;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.log.constance.LogConst;
import com.wj.manager.controller.warpper.SysDeptWarpper;
import com.wj.manager.pojo.SysDept;
import com.wj.manager.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author Wj
 */
@RestController
@RequestMapping("/dept")
@Api("部门数据")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @GetMapping("/list")
    @ApiOperation("获取所有的部门,给iview的tree组建提供数据")
    public ResponseResult getListTree(){
        List<DeptDto> tree = sysDeptService.getListTreeByPid(0);
        if(tree != null && tree.size() > 0){
            return ResponseResult.success(tree);
        }
        return ResponseResult.fail();
    }

    @GetMapping("/list/table")
    @ApiOperation("获取所有的部门,给部门管理的table组建提供数据")
    @PreAuthorize("hasAuthority('/dept')")
    public ResponseResult getListTable(@ModelAttribute QueryParams<SysDept> queryParams){
        IPage<Map<String, Object>> iPage = sysDeptService.getListPage(queryParams);
        if(iPage != null && iPage.getRecords()!=null && iPage.getRecords().size()>0){
           return ResponseResult.success(new SysDeptWarpper(iPage).warpper());
        }
        return ResponseResult.fail();
    }

    @PostMapping("/add")
    @ApiOperation("添加部门")
    @PreAuthorize("hasAuthority('/dept/add')")
    @BusinessLog(value = "添加部门",dict = LogConst.SYS_DEPT_DICT)
    public ResponseResult addDept(@ModelAttribute SysDept dept){
        if(dept == null || StringUtils.isBlank(dept.getFullname()) || null == dept.getPid()){
            throw new CustomException("999","部门全名或上级部门必须填写");
        }

        int row = sysDeptService.addDept(dept);
        if(row == 1){
            return ResponseResult.success("添加部门成功");
        }
        return ResponseResult.fail("添加部门失败");
    }

    @PostMapping("/update")
    @ApiOperation("更改部门，id必须填")
    @PreAuthorize("hasAuthority('/dept/update')")
    @BusinessLog(value = "修改部门",dict = LogConst.SYS_DEPT_DICT,type = LogTypeEnum.UPDATE)
    public ResponseResult updateDept(@RequestBody SysDept dept){
      if(dept == null || null == dept.getId()){
          throw new CustomException(ExceptionEnum.NO_WORK);
      }
     int row = sysDeptService.updateDeptByid(dept);
      if(row == 1){
          return ResponseResult.success("更新部门成功");
      }
        return ResponseResult.fail();
    }

    @GetMapping("/status/{id}/{value}")
    @ApiOperation("更改部门状态，id必须填")
    @PreAuthorize("hasAuthority('/dept/update')")
    @BusinessLog(value = "修改部门",dict = LogConst.SYS_DEPT_DICT,type = LogTypeEnum.UPDATE,isWarpper = false)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "部门id",dataType = "integer",paramType = "path"),
            @ApiImplicitParam(name = "value",value = "状态值，1:启用,2:禁用",dataType = "integer",paramType = "path")
    })
    public ResponseResult updateDept(@PathVariable Integer deptid,@PathVariable Integer status){
        if(deptid == null || null == status){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        if(deptid.intValue() == 24 && status.intValue() == ConstantFactory.SYS_STATE_DISABLE){
            return ResponseResult.fail("无权更改总公司");
        }
        SysDept dept = new SysDept();
        dept.setId(deptid);
        dept.setStatus(status);
        int row = sysDeptService.updateDeptByid(dept);
        if(row == 1){
            return ResponseResult.success(ConstantFactory.instance().getDictNameByCodeAndNum(DictCodeEnum.SYS_STATE,status)+"部门成功");
        }
        return ResponseResult.fail(ConstantFactory.instance().getDictNameByCodeAndNum(DictCodeEnum.SYS_STATE,status)+"部门失败");
    }

}

