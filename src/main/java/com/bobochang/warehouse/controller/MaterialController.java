package com.bobochang.warehouse.controller;

import cn.hutool.core.date.DateTime;
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
import java.util.List;
import java.util.Objects;

@RequestMapping("/material")
@RestController
@Transactional
@BusLog(name = "材料管理")
@Slf4j
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    //注入StoreService
    @Autowired
    private StoreService storeService;
//
//    //注入SupplyService
//    @Autowired
//    private SupplyService supplyService;
//
    //注入UnitService
    @Autowired
    private UnitService unitService;
    
    @Autowired
    private ContractService contractService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

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
    @RequestMapping("/material-page-list")
    public Result materialPageList(Page page, Material material) {
        //执行业务
        page = materialService.queryMaterialPage(page, material);
        //响应
        return Result.ok(page);
    }

    /**
     * 根据合同id查找产品，然后根据产品查找配料比后查找材料列表，用于从任务中心跳转直接采购需要的材料
     * @param page
     * @param contractId
     * @return
     */
    @RequestMapping("/material-page-list-contractId")
    public Result materialPageListByContract(Page page, Integer contractId) {
        Contract contract = contractService.findContractById(contractId);
        log.info(contract.getMaterials());
        if (Objects.equals(contract.getMaterials(), "[]")){
            page = materialService.materialPageListByContract(page, contractId);
        }else{
            page = materialService.materialPageListByContractMaterial(page, contractId);
        }
        //执行业务
        //响应
        return Result.ok(page);
    }



//    /**
//     * 将配置文件的file.upload-path属性值注入给控制器的uploadPath属性,
//     * 其为图片上传到项目的目录路径(类路径classes即resources下的static/img/upload);
//     */
//    @Value("${file.upload-path}")
//    private String uploadPath;
//
//    /**
//     * 上传图片的url接口/product/img-upload
//     * <p>
//     * 参数MultipartFile file对象封装了上传的图片;
//     *
//     * @CrossOrigin表示该url接口允许跨域请求;
//     */
//    @CrossOrigin
//    @PostMapping("/img-upload")
//    public Result uploadImg(MultipartFile file) {
//
//        try {
//            //拿到图片上传到的目录(类路径classes下的static/img/upload)的File对象
//            File uploadDirFile = ResourceUtils.getFile(uploadPath);
//            //拿到图片上传到的目录的磁盘路径
//            String uploadDirPath = uploadDirFile.getAbsolutePath();
//            //拿到图片保存到的磁盘路径
//            String fileUploadPath = uploadDirPath + "\\" + file.getOriginalFilename();
//            //保存图片
//            file.transferTo(new File(fileUploadPath));
//            //成功响应
//            return Result.ok("图片上传成功！");
//        } catch (IOException e) {
//            //失败响应
//            return Result.err(Result.CODE_ERR_BUSINESS, "图片上传失败！");
//        }
//    }
//
    /**
     * 添加商品的url接口/product/product-add
     *
     * @RequestBody Product product将添加的商品信息的json串数据封装到参数Product对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/material-add")
    @BusLog(descrip = "添加材料")
    public Result addMaterial(@RequestBody Material material,
                             @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即添加商品的用户id
        int createBy = currentUser.getUserId();
        material.setCreateBy(createBy);
        material.setCreateTime(DateTime.now());

        //执行业务
        Result result = materialService.saveMaterial(material);

        //响应
        return result;
    }

    /**
     * 删除商品的url接口/product/product-delete/{productId}
     */
    @RequestMapping("/material-delete/{materialId}")
    @BusLog(descrip = "删除材料")
    public Result deleteMaterial(@PathVariable Integer materialId) {
        //执行业务
        Result result = materialService.deleteMaterial(materialId);
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
    @RequestMapping("/material-update")
    @BusLog(descrip = "更新材料")
    public Result updateMaterial(@RequestBody Material material,
                                @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即修改商品的用户id
        int updateBy = currentUser.getUserId();
        material.setUpdateBy(updateBy);
        material.setUpdateTime(DateTime.now());
        
        //执行业务
        Result result = materialService.updateMaterial(material);

        //响应
        return result;
    }

    /**
     * 查询所有材料
     * @return
     */
    @RequestMapping("/material-list")
    public Result materialList(){
        List<Material> materialList = materialService.queryAllMaterial();
        return Result.ok(materialList);
    }


//    /**
//     * 导出材料列表信息数据
//     * @param page
//     * @param product
//     * @return
//     */
//    @RequestMapping("/exportTable")
//    public Result exportTable(Page page, Product product) {
//        //分页查询仓库
//        page = productService.queryProductPage(page, product);
//        //拿到当前页数据
//        List<?> resultList = page.getResultList();
//        //响应
//        return Result.ok(resultList);
//    }
}
