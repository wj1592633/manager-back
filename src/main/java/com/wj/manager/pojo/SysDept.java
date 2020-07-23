package com.wj.manager.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author Wj
 */
@ApiModel("部门模型")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(name = "id",value = "部门id",dataType = "integer")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 排序
     */
    @ApiModelProperty(name = "num",value = "排序",dataType = "integer")
    private Integer num;
    /**
     * 父部门id
     */
    @ApiModelProperty(name = "pid",value = "父部门id",dataType = "integer")
    private Integer pid;
    /**
     * 父级ids
     */
    @ApiModelProperty(hidden = true)
    private String pids;
    /**
     * 简称
     */
    @ApiModelProperty(name = "simplename",value = "部门简称",dataType = "string")
    private String simplename;
    /**
     * 全称
     */
    @ApiModelProperty(name = "fullname",value = "部门全称",dataType = "string")
    private String fullname;
    /**
     * 提示
     */
    private String tips;
    /**
     * 版本（乐观锁保留字段）
     */
    @Version
    @ApiModelProperty(hidden = true)
    private Integer version;

    @ApiModelProperty(hidden = true)
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getSimplename() {
        return simplename;
    }

    public void setSimplename(String simplename) {
        this.simplename = simplename;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SysDept{" +
        ", id=" + id +
        ", num=" + num +
        ", pid=" + pid +
        ", pids=" + pids +
        ", simplename=" + simplename +
        ", fullname=" + fullname +
        ", tips=" + tips +
        ", version=" + version +
        "}";
    }
}
