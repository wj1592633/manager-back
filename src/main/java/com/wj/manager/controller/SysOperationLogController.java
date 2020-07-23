package com.wj.manager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.pojo.SysOperationLog;
import com.wj.manager.service.SysOperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 操作日志 前端控制器
 * </p>
 *
 * @author Wj
 */
@Api("操作日志模块")
@RestController
@RequestMapping("/operation/log")
public class SysOperationLogController {
    @Autowired
    SysOperationLogService logService;

    @GetMapping("/query")
    @ApiOperation(value = "分页、倒序查询操作日志")
    @PreAuthorize("hasAuthority('/log/list')")
    public ResponseResult getLoginLogs(@ModelAttribute QueryParams<SysOperationLog> queryParams){
        IPage<Map<String, Object>> iPage = logService.selectLogsPage(queryParams);
        return ResponseResult.success(iPage);
    }
}

