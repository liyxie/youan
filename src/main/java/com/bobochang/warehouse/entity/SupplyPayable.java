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
 * @TableName supply_payable
 */
@TableName(value ="supply_payable")
@Data
public class SupplyPayable implements Serializable {
    /**
     * 入库单id
     */
    @TableId
    private Integer inStoreId;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 已付金额
     */
    private BigDecimal moneyPaid;

    /**
     * 应付金额
     */
    private BigDecimal moenyPayable;

    /**
     * 开票日期
     */
    private Date createTime;

    /**
     * 发票类型
     */
    private Integer invoiceType;

    /**
     * 备注
     */
    private String remarks;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}