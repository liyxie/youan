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
 * 采购单
 * @TableName buy_list
 */
@TableName(value ="buy_list")
@Data
public class Purchase implements Serializable {
    /**
     * 采购id
     */
    @TableId(type = IdType.AUTO)
    private Integer buyId;

    /**
     * 材料id
     */
    private Integer materialId;
    private String materialName;

    /**
     * 仓库id
     */
    private Integer storeId;
    private String storeName;


    /**
     * 供应商id
     */
    private Integer supplyId;//采购材料的供应商id
    private String supplyName;
    
    /**
     * 实际购买数量
     */
    private BigDecimal buyNum;

    /**
     * 计划购买数量
     */
    private BigDecimal factBuyNum;

    /**
     * 采购时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date buyTime;

    /**
     * 购买人
     */
    private String buyUser;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 所属合同id
     */
    private Integer contractId;
    private String contractName;

    /**
     * 0 否 1 是
     */
    private String isIn;

    /**
     * 驳回理由
     */
    private String reason;
    
    private String startTime;
    private String endTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}