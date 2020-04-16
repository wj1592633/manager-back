package com.wj.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.dto.DeptDto;
import com.wj.manager.common.dto.IviewTreeDto;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.thread.LogThreadData;
import com.wj.manager.mapper.SysRoleMapper;
import com.wj.manager.pojo.SysDept;
import com.wj.manager.pojo.SysRole;
import com.wj.manager.pojo.SysRole;
import com.wj.manager.service.SysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<IviewTreeDto> getListTreeByPid(Long pid) {
        //先查根部门
        if(pid.equals(null)){
            pid = 0L ;
        }
        QueryWrapper<SysRole> warpper =new QueryWrapper<>();
        warpper.eq("pid",pid);
        List<Map<String, Object>> maps = baseMapper.selectMaps(warpper);
        List list = new ArrayList<>();
        if(maps != null && maps.size() >0 ){
            for(Map map : maps){
                if(map != null) {
                    //设置值
                    IviewTreeDto treeDto = new IviewTreeDto();
                    Integer id = (Integer) map.get("id");
                    //没设计好，不想动代码了。这里就把Integer和Long互转
                    treeDto.setId(id.longValue());
                    if(pid.equals(0L)){
                        treeDto.setExpand(true);
                    }
                    treeDto.setTitle((String)map.get("name"));

                    if(id != null){
                        //查询子部门，并设置值
                        treeDto.setChildren(getListTreeByPid(id.longValue()));
                    }
                    //完成查询，添加到要返回的list中
                    list.add(treeDto);
                }
            }
            return list;
        }else {
            return null;
        }

    }

    @Override
    public List getRoleList() {

        return null;
    }

    @Override
    public IPage<Map<String, Object>> selectRoleList(QueryParams<SysRole> queryParams) {
        if((queryParams == null) || (queryParams.getPage()== null) || (queryParams.getSize() == null)){
            queryParams = new QueryParams<SysRole>();
        }
        //设置分页
        Page page = new Page();
        page.setCurrent(queryParams.getPage());
        page.setSize(queryParams.getSize());
        //设置查询条件
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();

        if(StringUtils.isNoneBlank(queryParams.getKeyword())){
            wrapper.like("name",queryParams.getKeyword());
        }
        wrapper.orderByAsc("num");
        IPage iPage = baseMapper.selectMapsPage(page, wrapper);

        return iPage;
    }

    @Override
    @Transactional
    public int addRole(SysRole sysRole) {
        if(sysRole == null){
            throw new CustomException(ExceptionEnum.NULL_DATA);
        }
        sysRole.setStatus(1);
        sysRole.setVersion(1);
        if(sysRole.getNum() == null){
            sysRole.setNum(3);
        }
        if(StringUtils.isNoneBlank(sysRole.getTips())){
            sysRole.setTips("member");
        }
        return baseMapper.insert(sysRole);

    }

    @Override
    @Transactional
    public int updataRole(SysRole sysRole) {
        if(sysRole == null || sysRole.getId() == null){
            throw new CustomException(ExceptionEnum.NULL_DATA);
        }
        SysRole role = baseMapper.selectById(sysRole.getId());
        if(role == null || null == role.getId()){
            throw new CustomException(ExceptionEnum.NULL_DATA);
        }
        LogThreadData.instance().setData(role);
        sysRole.setVersion(role.getVersion());
        return baseMapper.updateById(sysRole);
    }

    @Override
    @Transactional
    public int deleteRole(Integer roleId) {
        if(roleId == null){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        //不能删除管理员
        if (roleId.equals(1)){
            throw new CustomException(ExceptionEnum.DONT_DELETE_ADMIN);
        }
        HashSet<Integer> set = new HashSet<>();
        getSubRoleById(roleId,set);
        set.add(roleId);
        return baseMapper.deleteBatchIds(set);
    }

    /**
     * 根据角色id查询其所有子角色
     * @param roleId
     * @param set
     */
    public void getSubRoleById(Integer roleId, Set<Integer> set){

        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",roleId);

        List<SysRole> subRoles = baseMapper.selectList(wrapper);
        if(subRoles != null && subRoles.size() > 0){
            for(SysRole sub : subRoles){
                set.add(sub.getId());
                getSubRoleById(sub.getId(),set);
            }
        }

    }
}
