package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.BusLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【bus_log(业务操作日志)】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface BusLogService extends IService<BusLog> {

    Page queryBuslogPage(Page page, com.bobochang.warehouse.entity.BusLog busLog);

    List<String> getAllBusLogName();
}
