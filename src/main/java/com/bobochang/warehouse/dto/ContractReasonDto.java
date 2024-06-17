package com.bobochang.warehouse.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author LI
 * @date 2023/11/1
 * 用于记录合同退回原因
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContractReasonDto {
    /**
     * 合同id
     */
    private Integer contractId;

    /**
     * 是否需要采购
     */
    private String ifPurchase;

    /**
     * 驳回原因
     */
    private String reason;
}
