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
 * 商品表
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product implements Serializable {
    /**
     * 产品id
     */
    @TableId(type = IdType.AUTO)
    private Integer productId;

    /**
     * 仓库id
     */
    private Integer storeId;
    private String storeName;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品在库数量
     */
    private String productNum;

    /**
     * 单位
     */
    private Integer unitId;
    private String unitName;

    /**
     * 销售单价
     */
    private BigDecimal salePrice;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建者
     */
    private Integer createBy;

    /**
     * 更新者
     */
    private Integer updateBy;
    

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}