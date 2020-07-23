package com.wj.manager.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.manager.pojo.SysRelation;

/**
 * <p>
 * 角色和菜单关联表 服务类
 * </p>
 *
 * @author Wj
 */
public interface SysRelationService extends IService<SysRelation> {
    /**
     * 修改角色权限
     * @param roleId 被修改的角色
     * @param menuIds 要修改成的权限所有id
     */
   public int updataRelations(Integer roleId, Long[] menuIds);

    /**
     * 根据roleid删除对应的权限
     * @param roleId
     * @return
     */
   public int deleteRelations(Integer roleId);
}
