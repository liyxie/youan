package com.bobochang.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName product_material
 */
@TableName(value ="product_material")
@Data
public class ProductMaterial implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 产品id
     */
    private Integer productId;

    /**
     * 材料id
     */
    private Integer materialId;

    /**
     * 材料占比
     */
    private Double ratio;

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

    private String userName;
    private String productName;
    private String materialName;
    private double materialNum;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}