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
 * 出库单
 * @TableName out_store
 */
@TableName(value ="out_store")
@Data
public class OutStore implements Serializable {
    /**
     * 出库单id
     */
    @TableId(type = IdType.AUTO)
    private Integer outsId;

    /**
     * 产品id
     */
    private Integer productId;
    private String productName; // 产品名称
    private BigDecimal salePrice; // 销售价格

    /**
     * 仓库id
     */
    private Integer storeId;
    private String storeName;

    /**
     * 出库数量
     */
    private BigDecimal outNum;

    /**
     * 工区名称
     */
    private String workRegion;

    /**
     * 客户
     */
    private String custom;

    /**
     * 所属合同id
     */
    private Integer contractId;
    private String contractName;

    /**
     * 出库车牌
     */
    private String carNumber;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建人
     */
    private Integer createBy;
    private String userCode;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 0 否 1 是
     */
    private String isOut;
    
    private String startTime;
    private String endTime;

    // 金额
    private BigDecimal salePriceSum;
    
    private String unitName;
    
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}