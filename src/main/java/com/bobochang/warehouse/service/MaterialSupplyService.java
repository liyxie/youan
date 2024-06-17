package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.MaterialSupply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.page.Page;

import java.math.BigDecimal;

/**
* @author HuihuaLi
* @description 针对表【material_supply】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface MaterialSupplyService extends IService<MaterialSupply> {
    BigDecimal selectPrice(MaterialSupply materialSupply);

    Page queryPage(Page page, MaterialSupply materialSupply, String userCode);

    Result saveMaterial(MaterialSupply material);

    Result updateMaterialSupplyById(MaterialSupply materialSupply);

    int updateState(MaterialSupply materialSupply);

    MaterialSupply selectById(Integer id);

    int updateMaterialIdById(MaterialSupply materialSupply);
}
