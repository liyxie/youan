package com.bobochang.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName contract_eginner
 */
@TableName(value ="contract_eginner")
@Data
public class ContractEginner implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 合同id
     */
    private Integer contractId;

    /**
     * 项目名称
     */
    private String productName;

    /**
     * 规格
     */
    private String specs;

    /**
     * 单位
     */
    private String unit;

    /**
     * 暂定数量
     */
    private Double quantity;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 总价
     */
    private BigDecimal total;

    /**
     * 备注
     */
    private String remarks;

    private String ifRatio;

    private Integer productId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}