package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.BusLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.entity.Salary;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【bus_log(业务操作日志)】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.BusLog
*/
public interface BusLogMapper extends BaseMapper<BusLog> {

    int selectBuslogCount(BusLog busLog);

    List<BusLog> queryBuslogPage(Page page, BusLog busLog);

    List<String> getAllBusLogName();
}




