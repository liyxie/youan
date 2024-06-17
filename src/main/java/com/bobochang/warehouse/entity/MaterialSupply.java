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
 * @TableName material_supply
 */
@TableName(value ="material_supply")
@Data
public class MaterialSupply implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 材料id
     */
    private Integer materialId;

    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 供应商id
     */
    private Integer supplyId;
    private String supplyName; // 供应商名称

    /**
     * 报价
     */
    private BigDecimal quotation;

    /**
     * 检测结果：0：未通过 1：通过
     */
    private String inspectionResult;

    /**
     * 相关附件
     */
    private String files;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Integer updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}