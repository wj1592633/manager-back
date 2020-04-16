package com.wj.manager.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author Wj
 * @since 2019-02-18
 */
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 排序
     */
    private Integer num;
    /**
     * 父级字典
     */
    private Integer pid;
    /**
     * 名称
     */
    private String name;
    /**
     * 提示
     */
    private String tips;
    /**
     * 值
     */
    private String code;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SysDict{" +
        ", id=" + id +
        ", num=" + num +
        ", pid=" + pid +
        ", name=" + name +
        ", tips=" + tips +
        ", code=" + code +
        "}";
    }
}
