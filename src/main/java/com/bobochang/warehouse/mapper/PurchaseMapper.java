package com.bobochang.warehouse.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bobochang.warehouse.entity.Purchase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【buy_list(采购单)】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.BuyList
*/
public interface PurchaseMapper extends BaseMapper<Purchase> {
    //查询采购单总行数的方法
    public int selectPurchaseCount(Purchase purchase);

    //分页查询采购单的方法
    public List<Purchase> selectPurchasePage(@Param("page") Page page, @Param("purchase") Purchase purchase);

    //根据id修改采购单的方法
    int updatePurchaseById(Purchase purchase);
//
//    //根据id删除采购单的方法
//    public int deletePurchaseById(Integer buyId);

    //根据id将采购单状态改为已入库的方法
    public int updateIsInById(Purchase purchase);

    int insertPurchase(Purchase purchase);

    List<Purchase> selectPurchaseByContractId(Integer contractId);

    List<Purchase> selectListByContractId(int ContractId);
}




