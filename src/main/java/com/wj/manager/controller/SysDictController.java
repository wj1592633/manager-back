package com.wj.manager.controller;


import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.dto.DictAllDto;
import com.wj.manager.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author Wj
 */
@RestController
@RequestMapping("/dict")
@Api("字典")
public class SysDictController {
    @GetMapping("/all")
    @ApiOperation("获取字典")
    public ResponseResult getDict(){
        DictAllDto allDict = ConstantFactory.instance().getAllDict();
        return ResponseResult.success(allDict.getDicts());
    }
}

