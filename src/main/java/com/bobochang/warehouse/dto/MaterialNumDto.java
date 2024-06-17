package com.bobochang.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author LI
 * @date 2023/11/8
 * 查询合同某个材料所需要的量
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MaterialNumDto {
    private Integer contractId;
    private Integer materialId;
    private double materialNum;
}
