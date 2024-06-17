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
 * @TableName flow
 */
@TableName(value ="flow")
@Data
public class Flow implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 流程实例id
     */
    private String instanceId;

    /**
     * 合同id
     */
    private Integer contractId;

    /**
     * 采购单id
     */
    private Integer purchaseId;

    /**
     * 入库单id
     */
    private Integer inStoreId;

    /**
     * 出库单id
     */
    private Integer outStoreId;

    /**
     * 实例状态：1=进行中，0=已结束
     */
    private Integer state;

    /**
     * 部署时间
     */
    private Date createTime;

    /**
     * 驳回原因
     */
    private String reason;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}