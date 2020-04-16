package com.wj.manager.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 报销表
 * </p>
 *
 * @author Wj
 * @since 2019-02-18
 */
public class SysExpense implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 报销金额
     */
    private BigDecimal money;
    /**
     * 描述
     */
    private String desc;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 状态: 1.待提交  2:待审核   3.审核通过 4:驳回
     */
    private Integer state;
    /**
     * 用户id
     */
    private Integer userid;
    /**
     * 流程定义id
     */
    @TableField("processId")
    private String processId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    public String toString() {
        return "SysExpense{" +
        ", id=" + id +
        ", money=" + money +
        ", desc=" + desc +
        ", createtime=" + createtime +
        ", state=" + state +
        ", userid=" + userid +
        ", processId=" + processId +
        "}";
    }
}
