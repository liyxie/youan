package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.BusLog;
import com.bobochang.warehouse.entity.Salary;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.BusLogService;
import com.bobochang.warehouse.mapper.BusLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【bus_log(业务操作日志)】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class BusLogServiceImpl extends ServiceImpl<BusLogMapper, BusLog>
    implements BusLogService{
    @Autowired
    private BusLogMapper busLogMapper;

    @Override
    public Page queryBuslogPage(Page page, com.bobochang.warehouse.entity.BusLog busLog) {
        int count = busLogMapper.selectBuslogCount(busLog);
        List<BusLog> salaryList = busLogMapper.queryBuslogPage(page,busLog);

        page.setResultList(salaryList);
        page.setTotalNum(count);
        return page;
    }

    @Override
    public List<String> getAllBusLogName() {
        return busLogMapper.getAllBusLogName();
    }
}




