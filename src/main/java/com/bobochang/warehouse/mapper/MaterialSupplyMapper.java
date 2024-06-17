package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.MaterialSupply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【material_supply】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.MaterialSupply
*/
public interface MaterialSupplyMapper extends BaseMapper<MaterialSupply> {

    List<MaterialSupply> queryPage(Page page, MaterialSupply materialSupply);

    int queryCount(MaterialSupply materialSupply);

    int updateMaterialSupplyById(MaterialSupply materialSupply);

    int updateStateById(MaterialSupply materialSupply);

    MaterialSupply selectMaterialSupplyById(Integer id);

    int updateMaterialIdById(MaterialSupply materialSupply);

    MaterialSupply selectByMaterialIdAndSupply(MaterialSupply materialSupply);
}




