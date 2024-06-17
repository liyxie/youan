package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.entity.SupplyPayable;
import com.bobochang.warehouse.mapper.SupplyPayableMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.SupplyPayableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【supply_payable】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class SupplyPayableServiceImpl extends ServiceImpl<SupplyPayableMapper, SupplyPayable>
    implements SupplyPayableService {
    @Autowired
    private SupplyPayableMapper supplyPayableMapper;

    @Override
    public Page querySupplyPage(Page page, Supply supply) {
        //查询仓库总行数
//        int storeCount = supplyPayableMapper.selectSupplyCount(supply);

        //分页查询仓库
//        List<Supply> storeList = supplyPayableMapper.selectSupplyPage(page, supply);

        //将查到的总行数和当前页数据封装到Page对象
//        page.setTotalNum(storeCount);
//        page.setResultList(storeList);

        return null;
    }
}




