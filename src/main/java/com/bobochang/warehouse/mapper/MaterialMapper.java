package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.InStore;
import com.bobochang.warehouse.entity.Material;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【material(商品表)】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.Material
*/
public interface MaterialMapper extends BaseMapper<Material> {
    List<Material> selectMaterialPage(Page page, Material material);

    int selectMaterialCount(Material material);

    public int updateMaterialById(Material material);

    List<Material> queryAllMaterial();

    int selectMaterialCountByProduct(Integer contractId);

    List<Material> selectMaterialPageByProduct(Page page, Integer contractId);

    int addInventById(InStore inStore);

    int reduceById(Integer materialId, double materialNum);

    Material selectMaterialByName(String materialName);

    int selectMaterialCountByContract(Integer contractId);

    List<Material> selectMaterialPageByContract(Page page, Integer contractId);

    List<Material> selectMaterialByContract(Integer contractId);
}




