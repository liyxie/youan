package com.bobochang.warehouse.service.impl;


import com.bobochang.warehouse.entity.Statistics;
import com.bobochang.warehouse.mapper.StatisticsMapper;
import com.bobochang.warehouse.service.StatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    //注入StatisticsMapper
    @Resource
    private StatisticsMapper statisticsMapper;

    //统计各个仓库商品库存数量的业务方法
    @Override
    public List<Statistics> statisticsStoreInvent() {
//        return statisticsMapper.statisticsStoreInvent();
        return null;
    }
}
