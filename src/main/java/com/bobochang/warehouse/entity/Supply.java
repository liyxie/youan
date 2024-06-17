package com.bobochang.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 供货商
 * @TableName supply
 */
@TableName(value ="supply")
@Data
public class Supply implements Serializable {
    /**
     * 供应商id
     */
    @TableId(type = IdType.AUTO)
    private Integer supplyId;

    /**
     * 供应商编码
     */
    private String supplyNum;

    /**
     * 供应商名称
     */
    private String supplyName;

    /**
     * 联系人
     */
    private String concat;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否启用：0:可用  1:不可用
     */
    private String isDelete;

    private Integer userId;    
    
    /**
     * 报价
     */
    private BigDecimal quotation;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}