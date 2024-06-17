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
 * 合同表
 * @TableName contract
 */
@TableName(value ="contract")
@Data
public class Contract implements Serializable {
    /**
     * 合同id
     */
    @TableId(type = IdType.AUTO)
    private Integer contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 所选的材料
     */
    private String materials;
    
    /**
     * 工期开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startTime;

    /**
     * 工期结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endTime;
    
    /**
     * 0未审核、1 待结算 、2 结算中、 3 已结算
     */
    private String contractState;

    /**
     * 相关附件
     */
    private String files;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 工区名称
     */
    private String workRegion;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 其他客户 
     */
    private String otherCustomer;

    /**
     * 工程名称 
     */
    private String projectName;

    /**
     * 负责公司 
     */
    private String company;

    /**
     * 签订地点 
     */
    private String signingAddress;

    /**
     * 签订日期 
     */
    private String signingDate;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 产品id
     */
    private Integer productId;
    private String productName;

    /**
     * 生产数量
     */
    private double productNum;

    /**
     * 是否需要采购
     */
    private String ifPurchase;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}