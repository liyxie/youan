package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.ProductMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.page.Page;

import java.util.List;
import java.util.Map;

/**
* @author HuihuaLi
* @description 针对表【product_material】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.ProductMaterial
*/
public interface ProductMaterialMapper extends BaseMapper<ProductMaterial> {
    //查询商品总行数的方法
    public int selectProductMaterialCount(ProductMaterial productMaterial);

    //分页查询商品的方法
    public List<ProductMaterial> selectProductMaterialPage(Page page, ProductMaterial productMaterial);

    //添加商品的方法
    public int insertProductMaterial(ProductMaterial productMaterial);

    int updateProductMaterialById(ProductMaterial productMaterial);

    List<ProductMaterial> selectRatioById(String productId);
}




