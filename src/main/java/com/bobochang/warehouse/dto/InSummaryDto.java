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
 * 入库汇总实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InSummaryDto {
    private String supplyName;
    
    private String materialName;
    
    private double inNum;
    
    private BigDecimal price;
    
    private BigDecimal money;
    
    private Date earliestCreateTime;

    private Date latestCreateTime;
    
    private BigDecimal totalAmount;
    
    private Integer total;

    private String unitName;

}
