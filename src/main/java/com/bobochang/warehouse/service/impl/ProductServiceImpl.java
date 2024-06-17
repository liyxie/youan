package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.Product;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.mapper.ProductMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【product(商品表)】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    //分页查询商品的业务方法
    @Override
    public Page queryProductPage(Page page, Product product) {

        //查询商品总行数
        int productCount = productMapper.selectProductCount(product);

        //分页查询商品
        List<Product> productList = productMapper.selectProductPage(page, product);

        //将查询到的总行数和当前页数据组装到Page对象
        page.setTotalNum(productCount);
        page.setResultList(productList);

        return page;
    }

    /*
      将配置文件的file.access-path属性值注入给service的accessPath属性,
     * 其为上传的图片保存到数据库中的访问地址的目录路径/img/upload/;
     */
    @Value("${file.access-path}")
    private String accessPath;

    //添加商品的业务方法
    @Override
    public Result saveProduct(Product product) {
        //添加商品
        int i = productMapper.insertProduct(product);

        if(i>0){
            return Result.ok("添加商品成功！");
        }

        return Result.err(Result.CODE_ERR_BUSINESS, "添加商品失败！");
    }
    
    //删除商品的业务方法
    @Override
    public Result deleteProduct(Integer productId) {
        //根据商品id删除商品
        int i = productMapper.deleteById(productId);
        if(i>0){
            return Result.ok("商品删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "商品删除失败！");
    }

    //修改商品的业务方法
    @Override
    public Result updateProduct(Product product) {
        //根据商品id修改商品信息
        int i = productMapper.updateProductById(product);
        if(i>0){
            return Result.ok("商品修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"商品修改失败！");
    }

    @Override
    public List<Product> queryAllProduct() {
        return productMapper.queryAllProduct();
    }
}




