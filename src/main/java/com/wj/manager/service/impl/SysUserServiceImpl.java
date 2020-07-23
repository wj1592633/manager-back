package com.wj.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.constant.EhCacheConst;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.exception.SystemException;
import com.wj.manager.common.thread.LogThreadData;
import com.wj.manager.common.util.ToolUtil;
import com.wj.manager.mapper.SysUserMapper;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.SecurityKit;
import com.wj.manager.service.SysUserService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author Wj
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public IPage<Map<String,Object>> selectUserList(QueryParams<SysUser> queryParams) {
        if((queryParams == null) || (queryParams.getPage()== null) || (queryParams.getSize() == null)){
            queryParams = new QueryParams<SysUser>();
        }
       //设置分页
        Page page = new Page();
        page.setCurrent(queryParams.getPage());
        page.setSize(queryParams.getSize());
        //设置查询条件
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("1",1);
        SysUser user = queryParams.getEntity();

        if(! SecurityKit.isAdmin()){
            wrapper.in("DEPTID",SecurityKit.getDeptDataScope());
        }

        if(StringUtils.isNoneBlank(queryParams.getKeyword())){
            wrapper.like("name",queryParams.getKeyword());
        }

        if((user != null)){
            if((user.getDeptid() != null)){
                wrapper.eq("DEPTID",user.getDeptid());
            }
            if(StringUtils.isNoneBlank(user.getName())){
                wrapper.like("NAME","%"+user.getName()+"%");
            }
        }

        wrapper.last(" AND ( status = 1 or status = 2 ) order by id desc");
        IPage<Map<String, Object>> iPage = baseMapper.selectMapsPage(page, wrapper);
        return iPage;
    }

    @Override
    public List<SysUser> getUserListByAccount(String account) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("account",account);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean isExistUserByAccount(String account) {
        List<SysUser> users = this.getUserListByAccount(account);
        if(users != null && users.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map addUser(SysUser sysUser) {
        //账号已经存在时，结束程序
        if (isExistUserByAccount(sysUser.getAccount())){
            throw new CustomException(ExceptionEnum.USER_EXIST);
        }
        ToolUtil.warpperRegisterUser(sysUser);
        String password = sysUser.getPassword();
        if(StringUtils.isBlank(password)){
            throw new CustomException(ExceptionEnum.ACC_PWD_NO_NULL);
        }
        sysUser.setPassword(passwordEncoder.encode(password));
        int row = baseMapper.insert(sysUser);
        //出错回滚
        if(row != 1){
            throw new SystemException(ExceptionEnum.DB_OPERATE_ERROR);
        }
        return ToolUtil.bean2Map(sysUser);
    }

    private SysUser getUserByid(Integer userid){
        if(userid == null){
            throw new CustomException(ExceptionEnum.USER_NO_EXSIT);
        }
        SysUser byId = baseMapper.selectById(userid);
        if(byId == null || null == byId.getId()){
            throw new CustomException(ExceptionEnum.USER_NO_EXSIT);
        }
        return byId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updataUser(SysUser sysUser) {
        //不要通过这个方法修改密码

        //数据没填写，直接结束
        if(sysUser == null || sysUser.getId() == null){
            throw new CustomException(ExceptionEnum.NOT_EXIST);
        }
        //Integer version = baseMapper.getVersionByUserId(sysUser.getId());
        SysUser byId = getUserByid(sysUser.getId());
        //放入Treadlocal中，用于日志
        LogThreadData.instance().setData(byId);

        Integer version = byId.getVersion();
        if(null == version){
            throw new CustomException(ExceptionEnum.USER_NO_EXSIT);
        }
        sysUser.setVersion(version);
        //自己修改自己信息
        if(SecurityKit.getUserId().equals(sysUser.getId())){
            baseMapper.updateById(sysUser);
            return ToolUtil.bean2Map(sysUser);
        //修改别人的信息
        }else {
            if (SecurityKit.canUpdateOtherUser(sysUser.getId())){
                baseMapper.updateById(sysUser);
                return ToolUtil.bean2Map(sysUser);
            }
        }
        throw new CustomException(ExceptionEnum.NO_PERMISSION);
    }

    @Override
    @Cacheable(value = EhCacheConst.CACHE_CONSTANT,key = "'userSalt'+ #userid")
    public String getSaltByUserId(Integer userid) {
        if(userid == null){
           throw new CustomException(ExceptionEnum.NULL_DATA);
        }
        return baseMapper.getSaltByUserId(userid);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = EhCacheConst.CACHE_CONSTANT,key = "'userSalt'+ #userId")
    public String resetPassword(Integer userId, String password) {
        //数据没填写，直接结束
        if(StringUtils.isBlank(password) || null == userId){
            throw new CustomException(ExceptionEnum.USER_NO_EXSIT);
        }
        Integer version = baseMapper.getVersionByUserId(userId);
        if(null == version){
            throw new CustomException(ExceptionEnum.USER_NO_EXSIT);
        }
        String newSlat = ToolUtil.generatorRandomSlat();
        String encodePwd = passwordEncoder.encode(password);

        baseMapper.resetPasswordById(userId, newSlat, encodePwd, version);
        return password;
        //自己修改自己密码
     /* if(SecurityKit.getUserId().equals(userId)){
            baseMapper.updatePasswordById(userId, newSlat, encodePwd, version);
            return password;
        }else {
          if (SecurityKit.canUpdateOtherUser(userId)){
              baseMapper.updatePasswordById(userId, newSlat, encodePwd, version);
              return password;
          }
      }
       throw new CustomException(ExceptionEnum.NO_PERMISSION);
      */

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = EhCacheConst.CACHE_CONSTANT,key = "'userSalt'+ #userid")
    public Integer updatePassword(Integer userid, String oldPwd, String newPwd) {
        if(StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)){
            throw new CustomException(ExceptionEnum.ACC_PWD_NO_NULL);
        }

        if(null != userid){
            SysUser user = baseMapper.selectById(userid);
            if(null != user && user.getId() != null){
                boolean b = passwordEncoder.matches(oldPwd, user.getPassword());
                boolean b1 = passwordEncoder.matches( user.getPassword(),oldPwd);
                if(! passwordEncoder.matches(oldPwd,user.getPassword())){
                    throw new CustomException(ExceptionEnum.PWD_NOT_RIGHT);
                }
                String newSlat = ToolUtil.generatorRandomSlat();
                return baseMapper.resetPasswordById(userid,newSlat,passwordEncoder.encode(newPwd), user.getVersion());
            }
            throw new CustomException(ExceptionEnum.USER_NO_EXSIT);
        }else {
            throw new CustomException(ExceptionEnum.BAD_NET);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updataUserStatus(Integer userId, Integer status) {
        if(null == userId || null == status){
            throw new CustomException(ExceptionEnum.NO_WORK);
        }
        SysUser byid = getUserByid(userId);
        LogThreadData.instance().setData(byid);
        Integer version = byid.getVersion();
        String tips = ConstantFactory.instance().getRoleTipsByUserId(userId);
        if(ConstantFactory.instance().ADMIN_NAME.equals(tips)){
            throw new CustomException(ExceptionEnum.DONT_OPERATE_ADMIN);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);

        sysUser.setVersion(version);
        sysUser.setStatus(status);
        int i = baseMapper.updateById(sysUser);
        return i;
    }

    @Override
    public String uploadAvatar(MultipartFile avatar) throws IOException {
        Map map = SecurityKit.getFullAuthentication();
        if(avatar == null){
            return null;
        }
        String oldName = avatar.getOriginalFilename();
        //文件后缀classpath:static/img +
        //如果要限制文件的类型，比如.png,.jpg  可以用文件后缀加判断，在这就暂时不判断了
        String fileType = oldName.substring(oldName.lastIndexOf("."));
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String subPath = "/" + SecurityContextHolder.getContext().getAuthentication().getName() ;
        // 图片上传根路径是E:/manager/upload，访问时用nginx
        //如果想把图片存不同的服务器，可以ftp传输文件。或者自己搭个文件系统
        File fileDir = new File(ConstantFactory.instance().USER_AVATAR_DIR+ "/img" +subPath);
        if(!fileDir.exists()) {
            FileUtils.forceMkdir(fileDir);
        }
        String fileName = ToolUtil.genarateUserAvatarName();
        if(StringUtils.isBlank(fileName)){
            fileName = UUID.randomUUID().toString();
        }
        File file = new File(fileDir, fileName + fileType);
        //上传
        avatar.transferTo(file);

        return "/img" + subPath + "/" + fileName + fileType;

    }

    @Override
    public SysUser getSysUser(SysUser user) {
        //根据id查
        if(user != null && user.getId() != null ){
            SysUser sysUser = baseMapper.selectById(user.getId());
            if(sysUser == null || sysUser.getId() == null){
                throw new CustomException(ExceptionEnum.NOT_EXIST);
            }
            sysUser.setPassword("");
            return sysUser;
        }
        //根据account查
        if(user != null && user.getAccount() != null ){
            List<SysUser> users = this.getUserListByAccount(user.getAccount());
            if(users != null && users.size() >0 && users.get(0).getId() != null){
                SysUser sysUser = users.get(0);
                sysUser.setPassword("");
                return sysUser;
            }
            throw new CustomException(ExceptionEnum.NOT_EXIST);
        }
        //查询当前登陆用户信息
        SysUser me = baseMapper.selectById(SecurityKit.getUserId());
        if(me == null || me.getId() == null){
            throw new CustomException(ExceptionEnum.NOT_EXIST);
        }
        me.setPassword("");
        return me;
    }
}
