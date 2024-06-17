package com.bobochang.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author LI
 * @date 2023/11/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseReasonDto {
    /**
     * 合同id
     */
    private Integer contractId;

    /**
     * 采购id
     */
    private Integer buyId;

    /**
     * 驳回理由
     */
    private String reason;

    /**
     * 0 否 1 是
     */
    private String isIn;

}
