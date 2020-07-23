package com.wj.manager.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.pojo.SysLoginLog;

import java.util.Map;

/**
 * <p>
 * 登录记录 服务类
 * </p>
 *
 * @author Wj
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    public IPage<Map<String,Object>> selectLogsPage(QueryParams<SysLoginLog> queryParams);

}
