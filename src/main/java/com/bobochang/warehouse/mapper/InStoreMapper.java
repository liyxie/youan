package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.dto.InSummaryDto;
import com.bobochang.warehouse.dto.OutSummaryDto;
import com.bobochang.warehouse.entity.InStore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.page.Page;

import java.math.BigDecimal;
import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【in_store(入库单)】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.InStore
*/
public interface InStoreMapper extends BaseMapper<InStore> {
    //查询入库单总行数的方法
    public int selectInStoreCount(InStore inStore);

    //分页查询入库单的方法
    public List<InStore> selectInStorePage(Page page, InStore inStore);

    //添加入库单的方法
    public int insertInStore(InStore inStore);

    int updateInstoreById(InStore inStore);

//    根据id将入库单状态改为已入库的方法
    public int updateIsInById(Integer insId);

    List<InSummaryDto> selectInStoreSummaryPage(Page page, InStore inStore);

    BigDecimal selectInStoreSummaryMoneyBySupply(Page page, InSummaryDto inSummaryDto);

    int selectInStoreSummaryCount(InStore inStore);

}




