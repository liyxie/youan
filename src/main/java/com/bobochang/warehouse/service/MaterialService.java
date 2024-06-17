package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.InStore;
import com.bobochang.warehouse.entity.Material;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Product;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【material(商品表)】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface MaterialService extends IService<Material> {

    public Page queryMaterialPage(Page page, Material material);

    Result saveMaterial(Material material);
    
    Result updateMaterial(Material material);
    
    Result deleteMaterial(Integer materialId);

    List<Material> queryAllMaterial();
    
    int addInventById(InStore inStore);
    
    int reduceById(Integer materialId,double materialNum);

    Material selectMaterialByName(String materialName);

    Page materialPageListByContract(Page page, Integer contractId);

    Page materialPageListByContractMaterial(Page page, Integer contractId);

    List<Material> materialListByContractMaterial(Integer contractId);
}
