package com.bobochang.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 用户表
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
     * 
     */
    private String userCode;

    /**
     * 
     */
    private String userName;

    /**
     * 
     */
    private String userPwd;

    private String realName;

    private String bank;

    private String bankCard;

    private BigDecimal salary;

    private String workType;

    /**
     * 1 超级管理员 、 2  管理员 、 3 普通用户
     */
    private String userType;

    /**
     * 0 未审核 、1 已审核
     */
    private String userState;

    /**
     * 0 正常、 1 已删除
     */
    private String isDelete;

    /**
     * 
     */
    private Integer createBy;

    /**
     * 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 
     */
    private Integer updateBy;

    /**
     * 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private Integer workRegion;
    private int checkinSum;
}