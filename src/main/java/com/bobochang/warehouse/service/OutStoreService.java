package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.OutStore;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.page.Page;

/**
* @author HuihuaLi
* @description 针对表【out_store(出库单)】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface OutStoreService extends IService<OutStore> {
    //添加出库单的业务方法
    public Result saveOutStore(OutStore outStore);

    //分页查询出库单的业务方法
    public Page outStorePage(Page page, OutStore outStore);

    int updateOutStoreById(OutStore outStore);

    //确定出库的业务方法
    Result confirmOutStore(OutStore outStore);

    Page outStoreSummaryPage(Page page, OutStore outStore);
}
