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
 * 商品表
 * @TableName material
 */
@TableName(value ="material")
@Data
public class Material implements Serializable {
    /**
     * 材料id
     */
    @TableId(type = IdType.AUTO)
    private Integer materialId;

    /**
     * 仓库id
     */
    private Integer storeId;
    private String storeName; // 非表中字段，仓库名称

    /**
     * 材料名称及规格
     */
    private String materialName;

    /**
     * 材料在库数量
     */
    private double materialNum;

    /**
     * 单位
     */
    private Integer unitId;
    private String unitName; // 非表中字段，单位名称
    
    /**
     * 材料介绍
     */
    private String introduce;

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
     * 创建人
     */
    private Integer createBy;
    

    /**
     * 更新人
     */
    private Integer updateBy;
    
    private String needNum;
    
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}