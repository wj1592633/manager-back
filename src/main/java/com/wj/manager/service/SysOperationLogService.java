package com.wj.manager.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.pojo.SysOperationLog;

import java.util.Map;

/**
 * <p>
 * 操作日志 服务类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
public interface SysOperationLogService extends IService<SysOperationLog> {

   public IPage<Map<String,Object>> selectLogsPage(QueryParams<SysOperationLog> queryParams);
}
