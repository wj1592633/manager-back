package com.wj.manager.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.pojo.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 根据条件分页查询
     * @param queryParams 查询条件
     * @return ResponseResult查询结果
     */
    public IPage<Map<String,Object>>  selectUserList(QueryParams<SysUser> queryParams);

    /**
     * 根据账号查询用户
     * @param account 账号
     * @return 用户对象
     */
    public List<SysUser> getUserListByAccount(String account);

    /**
     * 根据账号查询用户是否存在
     * @param account 账号
     * @return true:已经存在 ,false:不存在
     */
    public boolean isExistUserByAccount(String account);

    /**
     * 添加系统用户
     * @param sysUser 系统用户
     * @return 返回添加的系统用户信息
     */
    public Map addUser(SysUser sysUser);

    /**
     * 修改用户信息
     * @param sysUser 用户信息
     * @return 修改后的用户信息
     */
    public Map updataUser(SysUser sysUser);

    /**
     * 根据用户id获取盐
     * @param userid
     * @return
     */
    public String getSaltByUserId(Integer userid);

    /**
     * 重置密码
     * @param userId 要修改密码的用户
     * @param password 要修改后的密码
     * @return
     */
    public String resetPassword(Integer userId, String password);

    /**
     * 修改密码
     * @param oldPwd 原来的密码
     * @param newPwd 要修改后的密码
     * @return
     */
    public Integer updatePassword(Integer userid, String oldPwd, String newPwd);

    /**
     * 更新用户状态
     * @param userId 要更新的用户的id
     * @param status 状态码，1、2、3
     * @return 数据库影响记录数
     */
    public Integer updataUserStatus(Integer userId, Integer status);

    /**
     * 处理用户头像上传
     * @param avatar 用户上传的文件
     * @return 文件的路径
     */
    public String uploadAvatar(MultipartFile avatar) throws IOException;

    /**
     * 查询单个用户。如果user、user的id和账号都为空，就查询当前用户，id不为空就根据id查
     * 账号不为空就根据账号查询
     * @param user
     * @return 用户具体信息
     */
    public SysUser getSysUser(SysUser user);
}
