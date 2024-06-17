package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.ContractRatio;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.entity.OutStore;

import java.util.List;

/**
* @author magic'book
* @description 针对表【contract_ratio】的数据库操作Mapper
* @createDate 2024-01-05 10:02:00
* @Entity com.bobochang.warehouse.entity.ContractRatio
*/
public interface ContractRatioMapper extends BaseMapper<ContractRatio> {

    int saveContractRatio(ContractRatio contractRatio);

    List<Integer> selectProductByContractId(int contractId);

    List<ContractRatio> selectRatioByOutStore(OutStore outStore);
}




