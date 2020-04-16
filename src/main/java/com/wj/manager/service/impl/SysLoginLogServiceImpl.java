package com.wj.manager.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.mapper.SysLoginLogMapper;
import com.wj.manager.pojo.SysLoginLog;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.SecurityKit;
import com.wj.manager.service.SysLoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 登录记录 服务实现类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public IPage<Map<String, Object>> selectLogsPage(QueryParams<SysLoginLog> queryParams) {
        if((queryParams == null) || (queryParams.getPage()== null) || (queryParams.getSize() == null)){
            queryParams = new QueryParams<SysLoginLog>();
        }
        //设置分页
        Page page = new Page();
        page.setCurrent(queryParams.getPage());
        page.setSize(queryParams.getSize());
        //设置查询条件
        QueryWrapper<SysLoginLog> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createtime");
        if(StringUtils.isNoneBlank(queryParams.getKeyword())){
            wrapper.like("logname",queryParams.getKeyword());
        }
        IPage<Map<String, Object>> iPage = baseMapper.selectMapsPage(page, wrapper);
        return iPage;
    }
}
