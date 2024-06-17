package com.bobochang.warehouse.controller;

import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Store;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LI
 * @date 2023/10/16
 * 供应商管理
 */
@RestController
@RequestMapping("/supply")
@BusLog(name="供应商管理")
public class SupplyController {
    @Autowired
    private SupplyService supplyService;

    /**
     * 分页查询供应商列表
     * @param page
     * @param supply
     * @return
     */
    @RequestMapping("/supply-page-list")
    public Result storePageList(Page page, Supply supply) {
        System.out.println(supply.getSupplyName());
        //执行业务
        page = supplyService.querySupplyPage(page, supply);
        //响应
        return Result.ok(page);
    }

    /**
     * 删除供应商
     * @param supplyId 供应商id
     * @return
     */
    @RequestMapping("/supply-delete/{supplyId}")
    @BusLog(descrip = "删除供应商")
    public Result deleteStore(@PathVariable Integer supplyId) {
        //执行业务
        Result result = supplyService.deleteSupply(supplyId);
        //响应
        return result;
    }

    /**
     * 修改供应商信息
     * @param supply
     * @return
     */
    @RequestMapping("/supply-update")
    @BusLog(descrip = "更新供应商")
    public Result updateStore(@RequestBody Supply supply) {
        //执行业务
        Result result = supplyService.updateSupply(supply);
        //响应
        return result;
    }

    /**
     * 添加仓库的url接口/supply/supply-add
     *
     * @RequestBody Supply supply将请求传递的json数据封装到参数Supply对象;
     */
    @RequestMapping("/supply-add")
    @BusLog(descrip = "添加供应商")
    public Result addStore(@RequestBody Supply supply) {
        //执行业务
        Result result = supplyService.saveSupply(supply);
        //响应
        return result;
    }

    /**
     * 校验供应商编号是否已存在的url接口/supply/supply-num-check
     */
    @RequestMapping("/supply-num-check")
    public Result checkStoreNum(String supplyNum) {
        //执行业务
        Result result = supplyService.checkSupplyNum(supplyNum);
        //响应
        return result;
    }

    /**
     * 导出供应商信息数据
     * @param page
     * @param supply
     * @return
     */
    @RequestMapping("/exportTable")
    @BusLog(descrip = "导出供应商")
    public Result exportTable(Page page, Supply supply) {
        //分页查询仓库
        page = supplyService.querySupplyPage(page, supply);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);
    }


    /**
     * 查询到能够提供该材料的供应商信息，并且是产品检测合格的供应商
     * @param materialId 材料id
     * @return
     */
    @RequestMapping("/supply-list/{materialId}")
    public Result selectSupplyByMaterialId(@PathVariable String materialId){
        System.out.println(materialId);
        List<Supply> list = supplyService.selectSupplyByMaterialId(materialId);
        return Result.ok(list);
    }

    /**
     * 查询到能够提供该材料的供应商信息，并且是产品检测合格的供应商
     * @return
     */
    @RequestMapping("/supply-list")
    public Result selectAllSupply(){
        List<Supply> list = supplyService.queryAllSupply();
        return Result.ok(list);
    }
}
