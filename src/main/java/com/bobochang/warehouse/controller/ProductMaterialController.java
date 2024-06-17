package com.bobochang.warehouse.controller;

import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.constants.WarehouseConstants;
import com.bobochang.warehouse.entity.*;
import com.bobochang.warehouse.mapper.ProductMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.MaterialService;
import com.bobochang.warehouse.service.ProductMaterialService;
import com.bobochang.warehouse.service.ProductService;
import com.bobochang.warehouse.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LI
 * @date 2023/10/24
 */
@RequestMapping("/product-material")
@RestController
@Transactional
@BusLog(name = "产品与材料比例管理")
@Slf4j
public class ProductMaterialController {
    @Autowired
    private ProductMaterialService productMaterialService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private MaterialService materialService;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private TokenUtils tokenUtils;
    
    @RequestMapping("/page-list")
    public Result pageList(Page page, ProductMaterial productMaterial) {
        //执行业务
        page = productMaterialService.queryPage(page, productMaterial);
        //响应
        return Result.ok(page);
    }

    /**
     * 查询所有产品
     * @return
     */
    @RequestMapping("/product-list")
    public Result productList(){
        List<Product> productList = productService.queryAllProduct();
        return Result.ok(productList);
    }
    
    /**
     * 添加   
     * @param productMaterial
     * @param token
     * @return
     */
    @RequestMapping("/add")
    @BusLog(descrip = "添加产品与材料比例")
    public Result addProduct(@RequestBody ProductMaterial productMaterial,
                             @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即添加商品的用户id
        int createBy = currentUser.getUserId();
        productMaterial.setCreateBy(createBy);

        //执行业务
        Result result = productMaterialService.saveProductMaterial(productMaterial);

        //响应
        return result;
    }

    /**
     * 更新配料比记录
     * @param productMaterial
     * @param token
     * @return
     */
    @RequestMapping("/update")
    @BusLog(descrip = "更新产品与材料比例")
    public Result updateProduct(@RequestBody ProductMaterial productMaterial,
                                @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即修改商品的用户id
        int updateBy = currentUser.getUserId();
        productMaterial.setUpdateBy(updateBy);

        //执行业务
        Result result = productMaterialService.updateRatio(productMaterial);

        //响应
        return result;
    }

    /**
     * 删除配料比记录
     * @param Id
     * @return
     */
    @RequestMapping("/delete/{Id}")
    public Result delete(@PathVariable Integer Id) {
        //执行业务
        //响应
        return productMaterialService.delete(Id);
    }

    /**
     * 根据产品id查询配料比
     * @param productId 产品id
     * @return
     */
    @GetMapping("/ratio/{productId}")
    public Result getRatio(@PathVariable String productId){
        return Result.ok(productMaterialService.selectRatioById(productId));
    }


    @GetMapping("/ratio-name/{productName}")
    public Result getRatioByProductName(@PathVariable String productName){
        System.out.println(productName);
        List<Product> list = productMapper.selectAllProductName();
        System.out.println(list);

        List<ProductMaterial> productMaterialList = new ArrayList<>();
        for (Product substring : list) {
            if (productName.contains(substring.getProductName())) {
                System.out.println("字符串中包含子字符串 '" + substring.getProductName() + "'");
                System.out.println(substring.getProductName());
                productMaterialList = productMaterialService.selectRatioById(substring.getProductId().toString());
                break;
            }
        }
        
        return Result.ok(productMaterialList);
//        return Result.ok(productMaterialService.selectRatioById(productId));
    }
}
