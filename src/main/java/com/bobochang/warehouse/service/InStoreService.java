package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.InStore;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.page.Page;

/**
* @author HuihuaLi
* @description 针对表【in_store(入库单)】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface InStoreService extends IService<InStore> {
    public Page queryInStorePage(Page page, InStore inStore);

    //添加入库单的业务方法
    public Result saveInStore(InStore inStore, Integer buyId);

    Result updateInstore(InStore inStore);

    //确定入库的业务方法
    public Result confirmInStore(InStore inStore);

    Page inStoreSummaryPage(Page page, InStore inStore);
}
