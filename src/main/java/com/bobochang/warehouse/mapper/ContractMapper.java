package com.bobochang.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.dto.EginnerContractDto;
import com.bobochang.warehouse.dto.MaterialNumDto;
import com.bobochang.warehouse.entity.Contract;
import com.bobochang.warehouse.entity.FaceModel;
import com.bobochang.warehouse.page.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractMapper extends BaseMapper<Contract> {
    // 根据合同 id 查询合同
    public Contract findContractById(Integer contractId);

    // 根据合同名称查询合同
    public Contract findContractByName(String contractName);

    // 查询合同总行数
    public int selectContractCount(Contract contract);

    // 分页查询合同
    public List<Contract> selectContractPage(Page page, Contract contract);

    // 添加合同
    public int insertContract(EginnerContractDto contract);

    // 根据合同 id 修改合同状态
    public int updateContractState(Contract contract);

    // 根据合同 id 修改合同名称
    public int updateContractById(Contract contract);

    List<Contract> selectAllContract();

    int updateContractIfPurchase(Contract contract);

    MaterialNumDto getNeedMaterialNum(MaterialNumDto materialNumDto);
}
