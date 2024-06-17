package com.bobochang.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LI
 * @date 2023/11/7
 * 出库汇总实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OutSummaryDto {
    private String workRegion;
    
    private String productName;
    
    private double outNum;
    
    private BigDecimal salePrice;
    
    private BigDecimal money;
    
    private Date earliestCreateTime;

    private Date latestCreateTime;
    
    private Integer total;
    
    private BigDecimal totalAmount;
    
    private String unitName;
}
