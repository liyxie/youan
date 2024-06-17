package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.dto.OutSummaryDto;
import com.bobochang.warehouse.entity.OutStore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.page.Page;

import java.math.BigDecimal;
import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【out_store(出库单)】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.OutStore
*/
public interface OutStoreMapper extends BaseMapper<OutStore> {
    //添加出库单的方法
    public int insertOutStore(OutStore outStore);

    //查询出库单总行数的方法
    public int outStoreCount(OutStore outStore);

    //分页查询出库单的方法
    public List<OutStore> outStorePage(Page page, OutStore outStore);

    //根据id将出库单状态改为已出库的方法
    public int updateIsOutById(Integer outsId);

    int updateOutStoreById(OutStore outStore);

    List<OutSummaryDto> selectOutStoreSummaryPage(Page page, OutStore outStore);

    BigDecimal selectOutStoreSummaryMoenyByWorkRegion(Page page, OutSummaryDto outSummaryDto);
    
    int selectOutStoreSummaryCount(OutStore outStore);
}




