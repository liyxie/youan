package com.bobochang.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName inspection
 * 产品检测表
 */
@TableName(value ="inspection")
@Data
public class Inspection implements Serializable {
    /**
     * 检测id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 材料id
     */
    private Integer materialId;

    /**
     * 送检供应商id
     */
    private Integer supplyId;
    
    /**
     * 检测结果：0：未通过 1：通过
     */
    private char inspectionResult;

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