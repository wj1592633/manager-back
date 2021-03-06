package com.wj.manager.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author Wj
 */
@ApiModel( description = "系统用户对象")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号
     */
    @ApiModelProperty(name = "account",value = "账号")
    @NotBlank(message = "账号不能为空!")
    private String account;
    /**
     * 密码
     */
    //@ApiModelProperty(name = "password",value = "密码",required = true)
    @ApiModelProperty(name = "password",value = "密码")
    //@NotBlank(message = "密码不能为空!")
    private String password;
    /**
     * md5密码盐
     */
    @ApiModelProperty(hidden = true)
    private String salt;
    /**
     * 名字
     */
    private String name;
    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "birthday",value = "生日",dataType = "string",example = "2019-3-22 10:11:22")
    private Date birthday;
    /**
     * 性别（1：男 2：女）
     */
    private Integer sex;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 电话
     */
    private String phone;
    /**
     * 角色id
     */
    private String roleid;
    /**
     * 部门id
     */
    private Integer deptid;
    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
    @ApiModelProperty(hidden = true)
    private Integer status;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createtime",value = "创建时间",dataType = "date",example = "2019-3-22 10:11:22")
    private Date createtime;
    /**
     * 保留字段
     */
    @Version
    @ApiModelProperty(hidden = true)
    private Integer version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SysUser{" +
        ", id=" + id +
        ", avatar=" + avatar +
        ", account=" + account +
        ", password=" + password +
        ", salt=" + salt +
        ", name=" + name +
        ", birthday=" + birthday +
        ", sex=" + sex +
        ", email=" + email +
        ", phone=" + phone +
        ", roleid=" + roleid +
        ", deptid=" + deptid +
        ", status=" + status +
        ", createtime=" + createtime +
        ", version=" + version +
        "}";
    }
}
