package com.bobochang.warehouse.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bobochang.warehouse.entity.ContractEginner;
import com.bobochang.warehouse.entity.ContractRatio;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LI
 * @date 2023/12/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EginnerContractDto {
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
    
    private List<ContractEginner> contractEginnerList;
    
    // 自定义的比例
    private List<List<ContractRatio>> ratioLists;
}
