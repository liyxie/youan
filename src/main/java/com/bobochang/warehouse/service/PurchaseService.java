package com.bobochang.warehouse.service;

import com.bobochang.warehouse.dto.PurchaseReasonDto;
import com.bobochang.warehouse.entity.Purchase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【buy_list(采购单)】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface PurchaseService extends IService<Purchase> {

    Page queryPurchasePage(Page page, Purchase purchase);

    Result updatePurchase(Purchase purchase);

    public Result savePurchase(Purchase purchase);


    List<Purchase> selectPurchaseByContractId(Integer contractId);

    List<Object> getPurchaseDetail(Integer contractId);
    
    int updatePurchaseState(Purchase purchase);

    void updatePurchaseStateByContractId(PurchaseReasonDto purchaseReasonDto);

    List<Object> getInPurchaseDetail(Integer contractId);
}
