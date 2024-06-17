package com.bobochang.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName salary
 */
@TableName(value ="salary")
@Data
public class Salary implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private BigDecimal otherSalary;

    /**
     * 
     */
    private BigDecimal overtimeSalary;

    /**
     * 
     */
    private BigDecimal withhold;

    /**
     * 
     */
    private BigDecimal netSalary;

    private BigDecimal payableSalary;


    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private Date createTime;

    private int monthDays;

    private int checkinDays;
    
    private String realName;

    private String bank;

    private String bankCard;

    private BigDecimal salary;

    private String workType;

    private Integer workRegion;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}