package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.Product;
import com.bobochang.warehouse.entity.ProductMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【product_material】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface ProductMaterialService extends IService<ProductMaterial> {
    // 分页查询
    Page queryPage(Page page, ProductMaterial productMaterial);

    // 保存
    Result saveProductMaterial(ProductMaterial productMaterial);

    // 更新
    Result updateRatio(ProductMaterial productMaterial);
    
    // 删除
    Result delete(Integer id);

    // 根据产品id查询配料比
    List<ProductMaterial> selectRatioById(String productId);
}
