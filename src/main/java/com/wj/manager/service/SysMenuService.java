package com.wj.manager.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.manager.common.dto.IviewTreeDto;
import com.wj.manager.pojo.SysMenu;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 查询所有的menu数据给IviewTree
     * @param urlSet 用户的权限url集合。urlSet可以为空，单纯查所有数据，和用户无关
     * @param pcode
     * @return
     */
    public List<IviewTreeDto> getListTreeByPCode(Collection<String> urlSet, String pcode);

    /**
     * 根据角色id获取其所有的权限url
     * @param roleId
     * @return
     */
    public List<String> getMenuUrlByRoleId(Integer roleId);

    /**
     * 获取所有的菜单
     * @return
     */
    public List getMenuListByPcode(String pcode) throws InvocationTargetException, IllegalAccessException;

    /**
     * 根据id修改菜单是否显示
     * @param id
     * @param status
     * @return
     */
    public int changeStatusById(Integer id, Integer status);

    /**
     * 查询所有的菜单，给菜单管理界面用
     * @return
     */
    public List<Map<String,Object>> getAllMenuList();
}
