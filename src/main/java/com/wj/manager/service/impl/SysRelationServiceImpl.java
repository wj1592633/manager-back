package com.wj.manager.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.mapper.SysRelationMapper;
import com.wj.manager.pojo.SysMenu;
import com.wj.manager.pojo.SysRelation;
import com.wj.manager.pojo.SysRole;
import com.wj.manager.service.SysRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author Wj
 */
@Service
public class SysRelationServiceImpl extends ServiceImpl<SysRelationMapper, SysRelation> implements SysRelationService {

    @Override
    @Transactional(isolation=Isolation.DEFAULT,propagation = Propagation.REQUIRED,readOnly = false)
    public int updataRelations(Integer roleId, Long[] menuIds) {

        if(roleId == null){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        ConstantFactory instance = ConstantFactory.instance();
        SysRole role = instance.getRoleByRoleId(roleId);
        //角色不存在，不让添加
        if(role == null){
            throw new CustomException(ExceptionEnum.ROLE_NOT_EXIST);
        }
        int i = 0;
        //先删除了所有的权限
        i = deleteRelations(roleId);

        if(menuIds != null && menuIds.length >0){
            //把新的权限添加进来
            for (Long menu : menuIds){
                SysMenu menu1 = instance.getMenuById(menu);
                if(menu1 == null){
                    throw new CustomException(ExceptionEnum.MENU_NOT_EXIST);
                }
                SysRelation relation = new SysRelation();
                relation.setRoleid(roleId);
                relation.setMenuid(menu);
                i = baseMapper.insert(relation);
                if (i != 1){
                    throw new CustomException(ExceptionEnum.BAD_NET);
                }
            }
        }

        return i;
    }

    @Override
    @Transactional(isolation=Isolation.DEFAULT,propagation = Propagation.REQUIRED,readOnly = false)
    public int deleteRelations(Integer roleId) {
        if(roleId == null){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        QueryWrapper<SysRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("roleid",roleId);
        return baseMapper.delete(wrapper);
    }
}
