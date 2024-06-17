package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.Product;
import com.bobochang.warehouse.entity.ProductMaterial;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.mapper.ProductMaterialMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.ProductMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author HuihuaLi
* @description 针对表【product_material】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class ProductMaterialServiceImpl extends ServiceImpl<ProductMaterialMapper, ProductMaterial>
    implements ProductMaterialService {
    @Autowired
    private ProductMaterialMapper productMaterialMapper;
    @Override
    public Page queryPage(Page page, ProductMaterial productMaterial) {

        //查询商品总行数
        int productMaterialCount = productMaterialMapper.selectProductMaterialCount(productMaterial);

        //分页查询商品
        List<ProductMaterial> productMaterialList = productMaterialMapper.selectProductMaterialPage(page, productMaterial);

        //将查询到的总行数和当前页数据组装到Page对象
        page.setTotalNum(productMaterialCount);
        page.setResultList(productMaterialList);

        return page;
    }

    @Override
    public Result saveProductMaterial(ProductMaterial productMaterial) {
        //添加商品
        int i = productMaterialMapper.insertProductMaterial(productMaterial);

        if(i>0){
            return Result.ok("添加成功！");
        }

        return Result.err(Result.CODE_ERR_BUSINESS, "添加失败！");
    }

    @Override
    public Result updateRatio(ProductMaterial productMaterial) {
        //根据商品id修改商品信息
        int i = productMaterialMapper.updateProductMaterialById(productMaterial);
        if(i>0){
            return Result.ok("商品修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"商品修改失败！");
    }

    @Override
    public Result delete(Integer id) {
        int i = productMaterialMapper.deleteById(id);
        if(i>0){
            return Result.ok("删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"删除失败！");
    }

    @Override
    public List<ProductMaterial> selectRatioById(String productId) {
        return productMaterialMapper.selectRatioById(productId);
    }

}




