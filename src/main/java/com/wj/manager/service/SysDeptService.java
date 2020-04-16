package com.wj.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.manager.common.dto.DeptDto;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.pojo.SysDept;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
public interface SysDeptService extends IService<SysDept> {
    /**
     * 根据pid获取部门信息
     * @param pid
     * @return
     */
    public List<DeptDto> getListTreeByPid(Integer pid);


    public IPage<Map<String,Object>> getListPage(QueryParams<SysDept> queryParams);

    public int updateDeptByid(SysDept dept);


    public int addDept(SysDept dept);
}
