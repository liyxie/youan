package com.bobochang.warehouse.controller;

import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.constants.WarehouseConstants;
import com.bobochang.warehouse.entity.*;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.*;
import com.bobochang.warehouse.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/product")
@RestController
@Transactional
@BusLog(name = "商品管理")
@Slf4j
public class ProductController {

    //注入StoreService
    @Autowired
    private StoreService storeService;

    //注入ProductTypeService
    @Autowired
    private ProductTypeService productTypeService;

    //注入SupplyService
    @Autowired
    private SupplyService supplyService;

    //注入UnitService
    @Autowired
    private UnitService unitService;

    //注入ProductService
    @Autowired
    private ProductService productService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;
    
    @Autowired
    private ContractEginnerService contractEginnerService;

    /**
     * 查询所有仓库的url接口/product/store-list
     * <p>
     * 返回值Result对象给客户端响应查询到的List<Store>;
     */
    @RequestMapping("/store-list")
    public Result storeList() {
        //执行业务
        List<Store> storeList = storeService.queryAllStore();
        //响应
        return Result.ok(storeList);
    }

    /**
     * 查询所有单位的url接口/product/unit-list
     * <p>
     * 返回值Result对象给客户端响应查询到的List<Unit>;
     */
    @RequestMapping("/unit-list")
    public Result unitList() {
        //执行业务
        List<Unit> unitList = unitService.queryAllUnit();
        //响应
        return Result.ok(unitList);
    }

    /**
     * 分页查询商品的url接口/product/product-page-list
     * <p>
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     * 参数Product对象用于接收请求参数仓库id storeId、商品名称productName、
     * 品牌名称brandName、分类名称typeName、供应商名称supplyName、产地名称
     * placeName、上下架状态upDownState、是否过期isOverDate;
     * <p>
     * 返回值Result对象向客户端响应组装了所有分页信息的Page对象;
     */
    @RequestMapping("/product-page-list")
    public Result productPageList(Page page, Product product) {
        //执行业务
        page = productService.queryProductPage(page, product);
        //响应
        return Result.ok(page);
    }

    /**
     * 添加商品的url接口/product/product-add
     *
     * @RequestBody Product product将添加的商品信息的json串数据封装到参数Product对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/product-add")
    @BusLog(descrip = "添加产品")
    public Result addProduct(@RequestBody Product product,
                             @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即添加商品的用户id
        int createBy = currentUser.getUserId();
        product.setCreateBy(createBy);

        //执行业务
        Result result = productService.saveProduct(product);

        //响应
        return result;
    }

    /**
     * 删除商品的url接口/product/product-delete/{productId}
     */
    @RequestMapping("/product-delete/{productId}")
    public Result deleteProduct(@PathVariable Integer productId) {
        //执行业务
        Result result = productService.deleteProduct(productId);
        //响应
        return result;
    }

    /**
     * 修改商品的url接口/product/product-update
     *
     * @RequestBody Product product将请求传递的json数据封装到参数Product对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/product-update")
    @BusLog(descrip = "产品更新")
    public Result updateProduct(@RequestBody Product product,
                                @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即修改商品的用户id
        int updateBy = currentUser.getUserId();
        product.setUpdateBy(updateBy);

        //执行业务
        Result result = productService.updateProduct(product);

        //响应
        return result;
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

    
    @RequestMapping("/product-list-id")
    public Result getProductListByContractId(Contract contract) {
        List<ContractEginner> contractEginnerList = contractEginnerService.selectProductById(contract.getContractId());
        List<Product> productList = productService.queryAllProduct();
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Product product : productList){
            for (ContractEginner contractProduct : contractEginnerList){
                if (contractProduct.getProductName().contains(product.getProductName())){
                    Map<String, Object> map = new HashMap<>();
                    map.put("productId", product.getProductId());
                    map.put("productName", product.getProductName());
                    map.put("id",contractProduct.getId());
                    System.out.println(map);
                    resultList.add(map);   
                }
            }
        }
        //响应
        return Result.ok(resultList);  
    }
}
