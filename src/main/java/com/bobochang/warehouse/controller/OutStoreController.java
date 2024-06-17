package com.bobochang.warehouse.controller;


import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.constants.WarehouseConstants;
import com.bobochang.warehouse.entity.*;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.ActivitiService;
import com.bobochang.warehouse.service.ContractService;
import com.bobochang.warehouse.service.OutStoreService;
import com.bobochang.warehouse.service.StoreService;
import com.bobochang.warehouse.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/outstore")
@RestController
@BusLog(name = "出库管理")
@Slf4j
public class OutStoreController {

    //注入OutStoreService
    @Autowired
    private OutStoreService outStoreService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    //注入StoreService
    @Autowired
    private StoreService storeService;
    
    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private ContractService contractService;
    /**
     * 添加出库单的url接口/outstore/outstore-add
     *
     * @RequestBody OutStore outStore将添加的出库单信息的json数据封装到参数OutStore对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/outstore-add")
    @Transactional
    @BusLog(descrip = "添加出库单")
    public Result addOutStore(@RequestBody OutStore outStore,
                              @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即添加出库单的用户id
        int createBy = currentUser.getUserId();
        outStore.setCreateBy(createBy);

        //执行业务

        //响应
        return outStoreService.saveOutStore(outStore);
    }

    /**
     * 查询所有仓库的url接口/outstore/store-list
     */
    @RequestMapping("/store-list")
    public Result storeList() {
        //执行业务
        List<Store> storeList = storeService.queryAllStore();
        //响应
        return Result.ok(storeList);
    }

    /**
     * 分页查询出库单的url接口/outstore/outstore-page-list
     * <p>
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     * 参数OutStore对象用于接收请求参数仓库id storeId、商品名称productName、
     * 是否出库isOut、起止时间startTime和endTime;
     * <p>
     * 返回值Result对象向客户端响应组装了所有分页信息的Page对象;
     */
    @RequestMapping("/outstore-page-list")
    public Result outStorePageList(Page page, OutStore outStore) {
        //执行业务
        page = outStoreService.outStorePage(page, outStore);
        //响应
        return Result.ok(page);
    }

    /**
     * 出库单更新
     * @param outStore
     * @return
     */
    @PostMapping ("/outstore-update")
    public Result outStorePageList(@RequestBody OutStore outStore) {
        //执行业务
        int i = outStoreService.updateOutStoreById(outStore);
        if(i<1){
            return Result.err(500,"修改失败");
        }
        //响应
        return Result.ok("修改成功");
    }
    
    /**
     * 确定出库的url接口/outstore/outstore-confirm
     */
    @RequestMapping("/outstore-confirm")
    @Transactional
    @BusLog(descrip = "出库单确认")
    public Result confirmOutStore(@RequestBody OutStore outStore, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        //执行业务
        //响应
        return outStoreService.confirmOutStore(outStore);
    }

    /**
     * 分页获取出库汇总
     * @param page
     * @param outStore
     * @return
     */
    @RequestMapping("/outstore-summary-page-list")
    public Result outStoreSummaryPageList(Page page, OutStore outStore) {
        //执行业务
        page = outStoreService.outStoreSummaryPage(page, outStore);
        //响应
        return Result.ok(page);
    }

    /**
     * 完成出库任务
     * @param token 用户令牌 contract 包含合同id
     * @return
     */
    @PostMapping("/complete-task")
    @BusLog(descrip = "完成出库")
    public Result completeTask(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                               @RequestBody Flow flow){
        System.out.println(flow.getContractId());
        String userCode = tokenUtils.getCurrentUser(token).getUserCode();
        
        Contract contract = new Contract();
        contract.setContractId(flow.getContractId());
        contract.setContractState("3"); // 将合同状态修改为结算中
        
        if(contractService.updateContractState(contract) < 1){
            return Result.err(500,"修改合同状态失败");
        }
        
        return activitiService.completeTask(userCode, flow);
    }
}
