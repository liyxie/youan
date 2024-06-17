package com.bobochang.warehouse.controller;


import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.constants.WarehouseConstants;
import com.bobochang.warehouse.dto.ContractReasonDto;
import com.bobochang.warehouse.dto.PurchaseReasonDto;
import com.bobochang.warehouse.entity.*;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.*;
import com.bobochang.warehouse.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/purchase")
@RestController
@BusLog(name="采购管理")
public class PurchaseController {

    //注入PurchaseService
    @Resource
    private PurchaseService purchaseService;

    //注入StoreService
    @Resource
    private StoreService storeService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    //注入InStoreService
    @Resource
    private InStoreService inStoreService;
    
    @Autowired
    private ContractService contractService;
    
    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    private MaterialSupplyService materialSupplyService;
    
    @Autowired
    private ActivitiService activitiService;

//    @Resource
//    private ActivitiService activitiService;

    /**
     * 添加采购单的url接口/purchase/purchase-add
     */
    @RequestMapping("/purchase-add")
    @BusLog(descrip = "添加采购")
    public Result addPurchase(@RequestBody Purchase purchase) {
        System.out.println(purchase.getSupplyId());
        //执行业务
        Result result = purchaseService.savePurchase(purchase);
        //响应
        return result;
    }

    /**
     * 查询所有仓库的url接口/purchase/store-list
     */
    @RequestMapping("/store-list")
    public Result storeList() {
        //执行业务
        List<Store> storeList = storeService.queryAllStore();
        //响应
        return Result.ok(storeList);
    }

    /**
     * 分页查询采购单的url接口/purchase/purchase-page-list
     * <p>
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     * 参数Purchase对象用于接收请求参数仓库id storeId、商品名称productName、
     * 采购人buyUser、是否生成入库单isIn、起止时间startTime和endTime;
     * <p>
     * 返回值Result对象向客户端响应组装了所有分页信息的Page对象;
     */
    @RequestMapping("/purchase-page-list")
    public Result purchasePageList(Page page, Purchase purchase) {
        //执行业务
        page = purchaseService.queryPurchasePage(page, purchase);
        //响应
        return Result.ok(page);
    }

    /**
     * 修改采购单的url接口/purchase/purchase-update
     *
     * @RequestBody Purchase purchase将请求传递的json数据封装到参数Purchase对象;
     */
    @RequestMapping("/purchase-update")
    @BusLog(descrip = "采购更新")
    public Result updatePurchase(@RequestBody Purchase purchase,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        //执行业务
        Result result = purchaseService.updatePurchase(purchase);

        //响应
        return result;
    }

//    /**
//     * 删除采购单的url接口/purchase/purchase-delete/{buyId}
//     *
//     * @PathVariable Integer buyId将路径占位符buyId的值赋值给参数变量buyId;
//     */
//    @RequestMapping("/purchase-delete/{buyId}")
//    public Result deletePurchase(@PathVariable Integer buyId) {
//        //执行业务
//        Result result = purchaseService.deletePurchase(buyId);
//        //响应
//        return result;
//    }

    /**
     * 添加入库单的url接口/purchase/in-warehouse-record-add
     *
     * @RequestBody Purchase purchase将请求传递的json数据封装到参数Purchase对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/in-warehouse-record-add")
    @BusLog(descrip = "生成入库单")
    public Result addInStore(@RequestBody Purchase purchase,
                             @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id -- 创建入库单的用户id
        int createBy = currentUser.getUserId();

        //创建InStore对象封装添加的入库单的信息
        InStore inStore = new InStore();
        inStore.setStoreId(purchase.getStoreId());
        inStore.setMaterialId(purchase.getMaterialId());
        inStore.setInNum(purchase.getFactBuyNum());
        inStore.setContractId(purchase.getContractId());
        inStore.setSupplyId(purchase.getSupplyId());
        inStore.setCreateBy(createBy);
        inStore.setIsIn("0");
        
        MaterialSupply materialSupply = new MaterialSupply();
        materialSupply.setMaterialId(purchase.getMaterialId());
        materialSupply.setSupplyId(purchase.getSupplyId());
        inStore.setPrice(materialSupplyService.selectPrice(materialSupply));
        
        //响应
        return inStoreService.saveInStore(inStore, purchase.getBuyId());
    }

    /**
     * 根据合同查询采购单
     * @param contractId
     * @return
     */
    @GetMapping("/purchase-list/{contractId}")
    public Result selectPurchaseByContractId(@PathVariable String contractId){
        return Result.ok(purchaseService.getPurchaseDetail(Integer.valueOf(contractId)));
    }

    @GetMapping("/purchase-in-list/{contractId}")
    public Result selectInPurchaseByContractId(@PathVariable String contractId){
        return Result.ok(purchaseService.getInPurchaseDetail(Integer.valueOf(contractId)));
    }
    

//    /**
//     * 导出采购列表信息数据
//     * @param page
//     * @param purchase
//     * @return
//     */
//    @RequestMapping("/exportTable")
//    public Result exportTable(Page page, Purchase purchase) {
//        //分页查询仓库
//        page = purchaseService.queryPurchasePage(page, purchase);
//        //拿到当前页数据
//        List<?> resultList = page.getResultList();
//        //响应
//        return Result.ok(resultList);
//    }

    /**
     * 审核采购单，如果同意
     * @param token
     * @param purchaseReasonDto 包含合同id,采购单id
     * @return
     */
    @PostMapping("/purchase-agree")
    @BusLog(descrip = "审核采购")
    public Result contractAgree(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                                @RequestBody PurchaseReasonDto purchaseReasonDto){
        String userCode = tokenUtils.getCurrentUser(token).getUserCode();
        purchaseReasonDto.setIsIn("2");
        purchaseService.updatePurchaseStateByContractId(purchaseReasonDto);
        Flow flow = new Flow();
        flow.setContractId(purchaseReasonDto.getContractId());
        return activitiService.completeTask(userCode, flow);
    }

    /**
     * 重新递交采购单进行审核
     * @param token
     * @param purchaseReasonDto 包括合同id
     * @return
     */
    @PostMapping("/purchase-again")
    @BusLog(descrip = "重新递交审核")
    public Result contractAgain(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                                @RequestBody PurchaseReasonDto purchaseReasonDto) throws Exception {
        String userCode = tokenUtils.getCurrentUser(token).getUserCode();
        purchaseReasonDto.setIsIn("0");
        purchaseService.updatePurchaseStateByContractId(purchaseReasonDto);
        return activitiService.skipPurchaseTask(userCode, purchaseReasonDto, "sid-04");
    }

    /**
     * 驳回合同
     * @param token
     * @param purchaseReasonDto 包括驳回原因
     * @return
     * @throws Exception
     */
    @PostMapping("/purchase-reject")
    @BusLog(descrip = "驳回审核")
    public Result contractReject(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                                 @RequestBody PurchaseReasonDto purchaseReasonDto) throws Exception {
        String userCode = tokenUtils.getCurrentUser(token).getUserCode();
        purchaseReasonDto.setIsIn("1");
        purchaseService.updatePurchaseStateByContractId(purchaseReasonDto);
        return activitiService.skipPurchaseTask(userCode, purchaseReasonDto, "sid-05");
    }
    
}
