package com.wj.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.dto.DeptDto;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.thread.LogThreadData;
import com.wj.manager.controller.warpper.SysDeptWarpper;
import com.wj.manager.mapper.SysDeptMapper;
import com.wj.manager.pojo.SysDept;
import com.wj.manager.pojo.SysLoginLog;
import com.wj.manager.service.SysDeptService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<DeptDto> getListTreeByPid(Integer pid) {
        //先查根部门
        if(pid.equals(null)){
            pid = 0 ;
        }
        QueryWrapper<SysDept> warpper =new QueryWrapper<>();
        warpper.eq("pid",pid);
        warpper.eq("status", ConstantFactory.SYS_STATE_ENABLE);
        List<Map<String, Object>> maps = baseMapper.selectMaps(warpper);
        List list = new ArrayList<>();
        if(maps != null && maps.size() >0 ){

            for(Map map : maps){
                if(map != null) {
                    //设置值
                    DeptDto deptDto = new DeptDto();
                    Integer id = (Integer) map.get("id");
                    deptDto.setDeptid(id);
                    if(pid.equals(0)){
                        deptDto.setExpand(true);
                    }
                    deptDto.setTitle((String)map.get("simplename"));

                    if(id != null){
                        //查询子部门，并设置值
                        deptDto.setChildren(getListTreeByPid(id));
                    }
                    //完成查询，添加到要返回的list中
                    list.add(deptDto);
                }
            }
            return list;
        }else {
            return null;
        }

    }

    @Override
    public IPage<Map<String, Object>> getListPage(QueryParams<SysDept> queryParams) {
        if((queryParams == null) || (queryParams.getPage()== null) || (queryParams.getSize() == null)){
            queryParams = new QueryParams<SysDept>();
        }
        //设置分页
        Page page = new Page();
        page.setCurrent(queryParams.getPage());
        page.setSize(queryParams.getSize());
        //设置查询条件
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        if(StringUtils.isNoneBlank(queryParams.getKeyword())){
            wrapper.like("fullname","%"+queryParams.getKeyword()+"%");
        }
        wrapper.orderByAsc("pid","num");
        IPage<Map<String, Object>> iPage = baseMapper.selectMapsPage(page, wrapper);
        return iPage;
    }

    @Override
    @Transactional
    public int updateDeptByid(SysDept dept) {
        if(dept == null || null == dept.getId()){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        SysDept sysDept = baseMapper.selectById(dept.getId());
        LogThreadData.instance().setData(sysDept);
        if(sysDept == null || null == sysDept.getId()){
            throw new CustomException(ExceptionEnum.OPERATE_DATA_NOT_EXSIT);
        }
        dept.setVersion(sysDept.getVersion());
      return baseMapper.updateById(dept);
    }



    @Override
    @Transactional
    public int addDept(SysDept dept) {
        if(dept == null || StringUtils.isBlank(dept.getFullname()) || null == dept.getPid()){
            throw new CustomException("999","部门全名或上级部门必须填写");
        }
        Integer pid = dept.getPid();
        SysDept parent = baseMapper.selectById(pid);
        if(parent == null || null == parent.getId()){
            throw new CustomException("998","上级部门不存在");
        }
        String Pids = parent.getPids()+"["+pid+"],";
        dept.setPids(Pids);
        dept.setVersion(1);
        if(null == dept.getNum()){
            dept.setNum(1);
        }
        dept.setStatus(ConstantFactory.SYS_STATE_ENABLE);
        return baseMapper.insert(dept);
    }


}
