package com.wj.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.dto.DeptDto;
import com.wj.manager.common.dto.DictAllDto;
import com.wj.manager.common.log.dict.AbstractLogDict;
import com.wj.manager.common.log.dict.SysUserLogDict;
import com.wj.manager.common.log.generator.DeafaultLogCreater;
import com.wj.manager.common.log.generator.LogCreater;
import com.wj.manager.common.log.vo.LogDataVo;
import com.wj.manager.common.properties.ShiroProperties;
import com.wj.manager.common.shiro.service.UserAuthService;
import com.wj.manager.common.shiro.service.impl.UserAuthServiceImpl;
import com.wj.manager.common.util.ToolUtil;
import com.wj.manager.common.util.SpringContextHolder;
import com.wj.manager.controller.warpper.SysUserWarpper;
import com.wj.manager.mapper.SysUserMapper;
import com.wj.manager.pojo.SysDict;
import com.wj.manager.pojo.SysOperationLog;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.service.SysDeptService;
import com.wj.manager.service.SysMenuService;
import com.wj.manager.service.SysUserService;
import com.wj.manager.service.impl.SysRoleServiceImpl;
import com.wj.manager.service.impl.SysUserServiceImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Ignore
public class ManagerBootApplicationTests {


    @Test
    public void dasdad(){
        LogCreater creater = new DeafaultLogCreater();
        LogDataVo logDataVo = new LogDataVo();
        SysUser sysUser = new SysUser();
        sysUser.setName("aaaaaaa");
        sysUser.setPassword("dasadada");
        logDataVo.setValue(sysUser);
        SysOperationLog operationLog = creater.createOperationLog(logDataVo, "测试", 15, "method", "dasafs", "aaa1", "dict");
        System.out.println(operationLog);
    }

    @Test
    public void test212(){
        String num = ConstantFactory.instance().getDictNameByCodeAndNum(DictCodeEnum.SYS_STATE, 3);
        System.out.println(num);
    }

	@Test
    //对ConstantFactory类的字段、方法的测试。结果符合预期的值
	public void testConstanctFactory() throws JsonProcessingException {
        System.out.println("__:_::::_:_");
        ConstantFactory instance = ConstantFactory.instance();
        String name33 =instance.getDictNameByCodeAndNum(DictCodeEnum.SEX,1);//男
        System.out.println(name33);
        name33 =instance.getDictNameByCodeAndNum(DictCodeEnum.ACCOUNT_STATE,3);//已删除
        System.out.println(instance.ADMIN_NAME);
		System.out.println(instance.DEFAULT_PWD);
        List list =instance.getParentDeptIds(25);//0，24
        list =instance.getSubDeptId(24);//
        List<SysDict> inDict = instance.findInDict(50);//男女2条记录
        String menuStatusName = instance.getMenuStatusName(1);//启用
        String statusName = instance.getStatusName(2);//冻结
        DictAllDto allDict = instance.getAllDict();
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(allDict);
            System.out.println(s);
        String sexName = instance.getSexName(1);
        String sexName1 = instance.getDictsByName("性别", 1);//男
        String noticeTitle = instance.getNoticeTitle(8);//你好
        String dictName = instance.getDictName(55);//禁用
        String mgr_freeze = instance.getMenuNameByCode("mgr_freeze");//冻结
        String menuName = instance.getMenuName(105L);//系统管理
        String menuNames = instance.getMenuNames("107,108,109");//增删改
        String deptName = instance.getDeptName(24);//总公司
        String singleRoleTip = instance.getSingleRoleTip(1);//administor超级管理员
        singleRoleTip = instance.getSingleRoleName(1);
        singleRoleTip = instance.getRoleName("1,5");//超管,临时
        singleRoleTip = instance.getUserAccountById(1);//admin
        singleRoleTip = instance.getUserNameById(1);//张三
    }
    @Test
    public void testWapper(){
            SysUserServiceImpl sysUserService = SpringContextHolder.getBean(SysUserServiceImpl.class);
            IPage<Map<String,Object>> iPage = sysUserService.selectUserList(null);
            List<Map<String,Object>> records = iPage.getRecords();
            Object iPage1 = new SysUserWarpper(records).warpper();

            Map<String, Object> stringObjectMap = records.get(0);
            Object iPage2 = new SysUserWarpper(stringObjectMap).warpper();

    }

    @Test
    public void test2() throws Exception {
            System.out.println("ToolUtil.generatorRandomSlat()");
            System.out.println(ToolUtil.generatorRandomSlat());
            System.out.println(ToolUtil.generatorRandomSlat());
            System.out.println(ToolUtil.generatorRandomSlat());
            System.out.println(ToolUtil.generatorRandomSlat());
            Map<String, Object> ma = new HashMap<>();
            ma.put("sex",1);
            ma.put("name","asdasd");
            ma.put("birthday",new Date());
            SysUser sysUser = ToolUtil.map2Bean(ma, SysUser.class);
            System.out.println(sysUser);

    }

    @Test
    public void test3(){
           SysUserMapper mapper = SpringContextHolder.getBean(SysUserMapper.class);
            SysUser sysUser = new SysUser();
            sysUser.setId(56);
            sysUser.setName("b1b");
            sysUser.setAvatar("d11as");
            sysUser.setVersion(1);
            //int i = mapper.updateById(sysUser);
        Integer v = mapper.getVersionByUserId(56);
        System.out.println(v);

    }

    @Test
    public void test5(){
        /*SysDeptService bean = SpringContextHolder.getBean(SysDeptService.class);
        List<DeptDto> listTreeByPid = bean.getListTreeByPid(0);
        System.out.println(listTreeByPid);*/
      /*  System.out.println("**3123*");
        UserAuthService bean = SpringContextHolder.getBean(UserAuthService.class);
        List<String> permissions = bean.findPermissionsByRoleId(1);
        System.out.println(permissions);*/


    }

    @Test
    public void test6(){
        SysRoleServiceImpl bean = SpringContextHolder.getBean(SysRoleServiceImpl.class);
        HashSet<Integer> set = new HashSet<>();
        bean.getSubRoleById(1,set);
        System.out.println(set);
        set.add(1);

    }

    @Test
    public void testSplit() throws InvocationTargetException, IllegalAccessException {
	    String s= ",";
        Set<String> menuUrlsByUserId = ConstantFactory.instance().getMenuUrlsByUserId(45);
        Set<String> menuUrlsByUserId2 = ConstantFactory.instance().getMenuUrlsByUserId(1);
        Set<String> menuUrlsByUserId3 = ConstantFactory.instance().getMenuUrlsByUserId(56);
        Set<String> menuUrlsByUserId4 = ConstantFactory.instance().getMenuUrlsByUserId(57);
        System.out.println(menuUrlsByUserId);
    }

    @Test
    public void test4() throws InvocationTargetException, IllegalAccessException {
        SysMenuService menuService = SpringContextHolder.getBean(SysMenuService.class);
        ArrayList<Object> list = new ArrayList<>();
        List list1 = menuService.getMenuListByPcode(null);
        System.out.println(list1);
    }

    @Test
    public void testadas() throws Exception {
        String sex = ConstantFactory.instance().getSex(1);
        String systemStatus = ConstantFactory.instance().getSystemStatus(1);
        String accountStatus = ConstantFactory.instance().getAccountStatus(4);
        System.out.println(111);
        SysUserLogDict sysUserLogDict = new SysUserLogDict();
        AbstractLogDict abstractLogDict =new SysUserLogDict();
        Object status = abstractLogDict.converSpecialFieldValue("status", 3);
        System.out.println(status);
    }
}

