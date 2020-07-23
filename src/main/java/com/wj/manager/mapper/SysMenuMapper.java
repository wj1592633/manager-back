package com.wj.manager.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.manager.pojo.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author Wj
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据角色查询该角色的所有权限
     * @param roleId
     * @return
     */
    public List<String> getResUrlsByRoleId(Integer roleId);

    public Integer getMenuStatusByUrl(String url);

}
