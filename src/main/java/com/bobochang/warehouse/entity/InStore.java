package com.bobochang.warehouse.entity;

import cn.hutool.core.date.DateTime;
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
 * 入库单
 * @TableName in_store
 */
@TableName(value ="in_store")
@Data
public class InStore implements Serializable {
    /**
     * 入库单id
     */
    @TableId(type = IdType.AUTO)
    private Integer insId;

    /**
     * 仓库id
     */
    private Integer storeId;
    private String storeName;

    /**
     * 原材料id
     */
    private Integer materialId;
    private String materialName;

    /**
     * 入库数量/公司数量
     */
    private BigDecimal inNum;

    /**
     * 对方数量
     */
    private BigDecimal relativeNum;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 运费单价
     */
    private BigDecimal freight;

    /**
     * 入库车牌号
     */
    private String carNumber;

    /**
     * 所属合同id
     */
    private Integer contractId;
    private String contractName;

    /**
     * 供应商id
     */
    private Integer supplyId;
    private String supplyName;
    
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
    private String isIn;
    
    private String startTime;
    private String endTime;
    
    private BigDecimal priceSum;

    private String unitName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}