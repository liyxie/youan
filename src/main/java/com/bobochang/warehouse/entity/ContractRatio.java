package com.bobochang.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName contract_ratio
 */
@TableName(value ="contract_ratio")
@Data
public class ContractRatio implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 合同id
     */
    private Integer contractId;

    /**
     * 产品id
     */
    private Integer productId;

    /**
     * 原材料id
     */
    private Integer materialId;

    /**
     * 材料占比
     */
    private Double newRatio;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}