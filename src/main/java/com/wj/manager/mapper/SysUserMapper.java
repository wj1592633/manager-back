package com.wj.manager.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.manager.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据用户id 重置密码
     * @param userId 用户id
     * @param newSlat 盐
     * @param hashPassword 加密后的密码
     * @return
     */
    public Integer resetPasswordById(@Param("userId") Integer userId,@Param("newSlat") String newSlat,
                                      @Param("hashPassword")String hashPassword ,
                                      @Param("version") Integer version);

    public Integer getVersionByUserId(Integer userId);

    @Select("SELECT salt from sys_user where id = #{userid}")
    public String getSaltByUserId(Integer userid);

    public Integer updateSaltByUserId(@Param("userId") Integer userId,@Param("salt") String salt, @Param("version") Integer version);

    public String getPasswordByUserId(Integer userId);

    /**
     * 根据用户id 更新密码
     * @param newSlat 盐
     * @param newPwd 加密后的密码
     * @return
     */
    //public Integer changePassword(@Param("userId")Integer userId,@Param("newPwd") String newPwd,@Param("newSlat") String newSlat, @Param("version") Integer version);

}
