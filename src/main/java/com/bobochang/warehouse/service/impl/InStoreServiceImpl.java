package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.dto.InSummaryDto;
import com.bobochang.warehouse.dto.OutSummaryDto;
import com.bobochang.warehouse.entity.InStore;
import com.bobochang.warehouse.entity.Purchase;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.mapper.PurchaseMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.InStoreService;
import com.bobochang.warehouse.mapper.InStoreMapper;
import com.bobochang.warehouse.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【in_store(入库单)】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class InStoreServiceImpl extends ServiceImpl<InStoreMapper, InStore>
    implements InStoreService{
    @Autowired
    private InStoreMapper inStoreMapper;
    
    @Autowired
    private PurchaseMapper purchaseMapper;
    
    @Autowired
    private MaterialService materialService;
    
    @Override
    public Page queryInStorePage(Page page, InStore inStore) {

        //查询入库单总行数
        int inStoreCount = inStoreMapper.selectInStoreCount(inStore);

        //分页查询入库单
        List<InStore> inStoreList = inStoreMapper.selectInStorePage(page, inStore);

        //将查询到的总行数和当前页数据封装到Page对象
        page.setTotalNum(inStoreCount);
        page.setResultList(inStoreList);

        return page;
    }

    @Transactional//事务处理
    @Override
    public Result saveInStore(InStore inStore, Integer buyId) {
        //添加入库单
        int i = inStoreMapper.insertInStore(inStore);
        if(i>0){
            //根据id将采购单状态改为已入库
            Purchase purchase = new Purchase();
            purchase.setBuyId(buyId);
            purchase.setIsIn("3");
            int j = purchaseMapper.updateIsInById(purchase);
            if(j>0){
                return Result.ok("入库单添加成功！");
            }
            return Result.err(Result.CODE_ERR_BUSINESS, "入库单添加失败！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "入库单添加失败！");
    }

    @Override
    public Result updateInstore(InStore inStore) {
        int i = inStoreMapper.updateInstoreById(inStore);
        if (i>0){
            return Result.ok("修改入库单据成功");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "修改失败");
    }

    //确定入库的业务方法
    @Transactional//事务处理
    @Override
    public Result confirmInStore(InStore inStore) {

        //根据id将入库单状态改为已入库
        int i = inStoreMapper.updateIsInById(inStore.getInsId());
        if(i>0){
            //根据商品id增加商品库存
            if(materialService.addInventById(inStore)>0){
                return Result.ok("入库成功！");
            }
            return Result.err(Result.CODE_ERR_BUSINESS, "入库失败！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "入库失败！");
    }

    @Override
    public Page inStoreSummaryPage(Page page, InStore inStore) {
        int inStoreCount = inStoreMapper.selectInStoreSummaryCount(inStore);

        List<InSummaryDto> inSummaryDtos = inStoreMapper.selectInStoreSummaryPage(page, inStore);
        for (InSummaryDto inSummaryDto : inSummaryDtos){
            BigDecimal sum = inStoreMapper.selectInStoreSummaryMoneyBySupply(page, inSummaryDto);
            inSummaryDto.setTotalAmount(sum);
        }
        page.setResultList(inSummaryDtos);
        page.setTotalNum(inStoreCount);
        return page;
    }
}




