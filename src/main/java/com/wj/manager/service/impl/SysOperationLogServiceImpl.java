package com.wj.manager.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.mapper.SysOperationLogMapper;
import com.wj.manager.pojo.SysOperationLog;
import com.wj.manager.pojo.SysOperationLog;
import com.wj.manager.service.SysOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {

    @Autowired
    SysOperationLogMapper logMapper;

    @Override
    public IPage<Map<String, Object>> selectLogsPage(QueryParams<SysOperationLog> queryParams) {
        if((queryParams == null) || (queryParams.getPage()== null) || (queryParams.getSize() == null)){
            queryParams = new QueryParams<SysOperationLog>();
        }
        //设置分页
        Page page = new Page();
        page.setCurrent(queryParams.getPage());
        page.setSize(queryParams.getSize());
        //设置查询条件
        QueryWrapper<SysOperationLog> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createtime");
        IPage<Map<String, Object>> iPage = baseMapper.selectMapsPage(page, wrapper);
        return iPage;
    }
}
