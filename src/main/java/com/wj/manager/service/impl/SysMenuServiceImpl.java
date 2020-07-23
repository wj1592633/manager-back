package com.wj.manager.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.constant.EhCacheConst;
import com.wj.manager.common.dto.IviewTreeDto;
import com.wj.manager.common.dto.MenuDto;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.thread.LogThreadData;
import com.wj.manager.controller.warpper.SysMenuWarpper;
import com.wj.manager.mapper.SysMenuMapper;
import com.wj.manager.pojo.SysMenu;
import com.wj.manager.pojo.SysRole;
import com.wj.manager.service.SysMenuService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Wj
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    @Override
    public List<IviewTreeDto> getListTreeByPCode(Collection<String> urlSet, String pcode) {

        if(StringUtils.isBlank(pcode)){
            pcode = "0";
        }
        QueryWrapper<SysMenu> warpper =new QueryWrapper<>();
        warpper.eq("pcode",pcode);
        warpper.eq("status",1);
        List<Map<String, Object>> maps = baseMapper.selectMaps(warpper);
        List list = new ArrayList<>();
        if(maps != null && maps.size() >0 ){
            for(Map map : maps){
                if(map != null) {
                    //设置值
                    IviewTreeDto treeDto = new IviewTreeDto();
                    Long id = (Long) map.get("id");
                    treeDto.setId(id);
                    //treeDto.setIcon((String)map.get("icon"));
                    if(! ((Integer)map.get("ismenu")).equals(1)){
                        //urlSet为空时，单纯查所有数据，和用户无关
                        if(urlSet == null){
                            treeDto.setChecked(false);
                        }else {
                            treeDto.setChecked(urlSet.contains(((String) map.get("url"))));
                        }
                    }
                    if(((Integer)map.get("isopen")).equals(1)){
                        treeDto.setExpand(true);
                    }
                    treeDto.setTitle((String)map.get("name"));
                    String thisPcode= (String)map.get("code");
                    if(StringUtils.isNoneBlank(thisPcode)){
                        //查询子部门，并设置值
                       treeDto.setChildren(getListTreeByPCode(urlSet,thisPcode));
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

    /**
     *  秒(0-59) 分(0-59) 时(0-23) 日(1-31) 月()1-12  星期(0-7或SUN-SAT)
     *  * : 代表任意每一秒或每一分
     *   ,: cron = "0,1,2,3 * * * * MON-SAT" 每到0、1、2、3秒时都执行
     *   -: 区间
     *   cron = "0/3 * * * * MON-SAT" 从0秒启动，每3秒执行一次
     *   cron = "0/3 * * * * ?"
     *   0 0 2 L * ?
     *   0 0 2 ? * 6L 每个月最后的星期6的2点钟执行一次
     *   0 0 2 LW * ? 每月最后一个工作日2点钟执行一次
     *   0 0 2-4 ? * 1#1 每个月第一周2-4点整点执行一次(共3次) 1#1每个月的第一个，出现在周的位置
     */
 /*  @Scheduled(cron = "0 * * * * MON-SAT")
    public void tesScheduled(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test ======== test =======test");
    }*/

    @Override
    public List<String> getMenuUrlByRoleId(Integer roleId) {
        return baseMapper.getResUrlsByRoleId(roleId);
    }


    @Override
    public List getMenuListByPcode(String pcode) throws InvocationTargetException, IllegalAccessException {
        //先查询除所有最高级的菜单
        if(StringUtils.isBlank(pcode)){
            pcode = "0";
        }

        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("pcode",pcode)
                .eq("ismenu",1)
                .eq("status",1)
                .orderByAsc("num");
        List<Map<String, Object>> pMenus = baseMapper.selectMaps(wrapper);

        ArrayList<Object> list = new ArrayList<>();
        if(pMenus != null && pMenus.size() > 0){
            //封装下菜单
            List<Map<String, Object>> wPMenus =(List<Map<String, Object>>) new SysMenuWarpper(pMenus).warpper();
          //  MenuDto menuDto1 = new MenuDto();
           // menuDto1.setSubMenus(wPMenus);
            for(Map subMenu : wPMenus){
                MenuDto menuDto = new MenuDto();
                BeanUtils.copyProperties(menuDto, subMenu);
                //根据当前的菜单查询其子菜单
                if(StringUtils.isNoneBlank((String)subMenu.get("code"))){
                    //递归把查询到的子菜单设置到当前菜单中
                    menuDto.setSubMenus( getMenuListByPcode((String) subMenu.get("code")));
                }
                list.add(menuDto);
            }
            return list;
        }else {
            return list;
        }
    }

    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = EhCacheConst.CACHE_CONSTANT,key = "'getAllMenuList'")
    public int changeStatusById(Integer id, Integer status) {
        if(null == id || null == status ){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        //查不到数据时
        SysMenu menu = baseMapper.selectById(id);
        if(menu == null || null == menu.getId()){
            throw new CustomException(ExceptionEnum.OPERATE_DATA_NOT_EXSIT);
        }
        menu.setStatus(status);
        LogThreadData.instance().setData(menu);
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pcode",menu.getCode());
        List<SysMenu> sysMenus = baseMapper.selectList(queryWrapper);
        if(null != sysMenus && sysMenus.size() > 0) {
            for (SysMenu subMenu : sysMenus) {
                subMenu.setStatus(status);
                baseMapper.updateById(subMenu);
            }
        }
        return baseMapper.updateById(menu);
    }

    @Override
    @Cacheable(value = EhCacheConst.CACHE_CONSTANT,key = "'getAllMenuList'")
    public List<Map<String, Object>> getAllMenuList() {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("ismenu",1);
        wrapper.orderByAsc("pcode","num");
        return baseMapper.selectMaps(wrapper);
    }
}
