package com.bobochang.warehouse.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Store;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.entity.SupplyPayable;
import com.bobochang.warehouse.page.Page;

import java.util.List;

public interface SupplyService extends IService<Supply> {

    //查询所有供应商的业务方法
    public List<Supply> queryAllSupply();

    // 分页查询供应商
    public Page querySupplyPage(Page page, Supply supply);

    // 删除供应商的业务方法
    public Result deleteSupply(Integer supplyId);

    //更新供应商信息的业务方法
    public Result updateSupply(Supply supply);

    //添加供应商的业务方法
    public Result saveSupply(Supply supply);

    //校验供应商编码是否存在
    public Result checkSupplyNum(String supplyNum);

    List<Supply> selectSupplyByMaterialId(String materialId);

    Supply selectOneByUserId(int userId);
}
