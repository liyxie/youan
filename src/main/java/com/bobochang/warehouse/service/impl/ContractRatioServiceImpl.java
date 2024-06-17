package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.ContractRatio;
import com.bobochang.warehouse.entity.OutStore;
import com.bobochang.warehouse.service.ContractRatioService;
import com.bobochang.warehouse.mapper.ContractRatioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author magic'book
* @description 针对表【contract_ratio】的数据库操作Service实现
* @createDate 2024-01-05 10:02:00
*/
@Service
public class ContractRatioServiceImpl extends ServiceImpl<ContractRatioMapper, ContractRatio>
    implements ContractRatioService{

    @Autowired
    private ContractRatioMapper contractRatioMapper;
    
    @Override
    public int saveContractRatio(ContractRatio contractRatio) {
        return contractRatioMapper.saveContractRatio(contractRatio);
    }

    @Override
    public List<Integer> selectProductByContractId(int contractId) {
        return contractRatioMapper.selectProductByContractId(contractId);
    }

    @Override
    public List<ContractRatio> selectRatioByOutStore(OutStore outStore) {
        return contractRatioMapper.selectRatioByOutStore(outStore);
    }
}




