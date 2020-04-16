package com.wj.manager.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.manager.pojo.SysRole;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户id获取角色tips
     * @param userId 用户id
     * @return tips信息
     */
    public String getRoleTipsByUserId(Integer userId);
}
