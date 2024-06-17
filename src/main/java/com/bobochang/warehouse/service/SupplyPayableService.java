package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.entity.SupplyPayable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.page.Page;

/**
* @author HuihuaLi
* @description 针对表【supply_payable】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface SupplyPayableService extends IService<SupplyPayable> {

    Page querySupplyPage(Page page, Supply supply);
}
