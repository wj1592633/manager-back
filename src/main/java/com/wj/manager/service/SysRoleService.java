package com.wj.manager.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.manager.common.dto.IviewTreeDto;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.pojo.SysRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
public interface SysRoleService extends IService<SysRole> {

   public List<IviewTreeDto> getListTreeByPid(Long pid);

    public  List getRoleList();

    public IPage<Map<String,Object>> selectRoleList(QueryParams<SysRole> queryParams);

    public int addRole(SysRole sysRole);
    public int updataRole(SysRole sysRole);
    public int deleteRole(Integer roleId);
}
