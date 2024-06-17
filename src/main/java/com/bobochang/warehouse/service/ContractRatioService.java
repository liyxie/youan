package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.ContractRatio;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.OutStore;

import java.util.List;

/**
* @author magic'book
* @description 针对表【contract_ratio】的数据库操作Service
* @createDate 2024-01-05 10:02:00
*/
public interface ContractRatioService extends IService<ContractRatio> {

    int saveContractRatio(ContractRatio contractRatio);

    List<Integer> selectProductByContractId(int contractId);

    List<ContractRatio> selectRatioByOutStore(OutStore outStore);
}
