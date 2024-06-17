package com.bobochang.warehouse.controller;


import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.constants.WarehouseConstants;
import com.bobochang.warehouse.entity.*;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.InStoreService;
import com.bobochang.warehouse.service.MaterialSupplyService;
import com.bobochang.warehouse.service.StoreService;
import com.bobochang.warehouse.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/instore")
@RestController
@BusLog(name = "入库管理")
public class InStoreController {

    //注入StoreService
    @Autowired  
    private StoreService storeService;

    //注入InStoreService
    @Autowired
    private InStoreService inStoreService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

//    @Autowired
//    private ActivitiService activitiService;
    

    /**
     * 查询所有仓库的url接口/instore/store-list
     */
    @RequestMapping("/store-list")
    public Result storeList() {
        //执行业务
        List<Store> storeList = storeService.queryAllStore();
        //响应
        return Result.ok(storeList);
    }

    /**
     * 分页查询入库单的url接口/instore/instore-page-list
     * <p>
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     * 参数InStore对象用于接收请求参数仓库id storeId、商品名称productName、
     * 起止时间startTime和endTime;
     * <p>
     * 返回值Result对象向客户端响应组装了所有分页信息的Page对象;
     */
    @RequestMapping("/instore-page-list")
    public Result inStorePageList(Page page, InStore inStore) {
        //执行业务
        page = inStoreService.queryInStorePage(page, inStore);
        //响应
        return Result.ok(page);
    }
    
    @RequestMapping("/instore-update")
    @BusLog(descrip = "入库单更新")
    public Result updateInStore(@RequestBody InStore inStore){
        return inStoreService.updateInstore(inStore);
    }

    /**
     * 确定入库的url接口/instore/instore-confirm
     *
     * @RequestBody InStore inStore将请求传递的json数据封装到参数InStore对象;
     */
    @RequestMapping("/instore-confirm")
    @Transactional
    @BusLog(descrip = "入库确认")
    public Result confirmInStore(@RequestBody InStore inStore, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        //执行业务
        //响应
        return inStoreService.confirmInStore(inStore);
    }

    @RequestMapping("/instore-summary-page-list")
    public Result outStoreSummaryPageList(Page page, InStore inStore) {
        //执行业务
        page = inStoreService.inStoreSummaryPage(page, inStore);
        //响应
        return Result.ok(page);
    }
}
