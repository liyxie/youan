package com.bobochang.warehouse.controller;

import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Salary;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.page.PageCheckinDto;
import com.bobochang.warehouse.service.BusLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LI
 * @date 2024/1/22
 */
@RestController
@RequestMapping("/buslog")
@Slf4j
@BusLog(name = "日志管理")
public class BusLogController {
    @Autowired
    private BusLogService busLogService;
    
    @GetMapping("/buslog-list")
    public Result findBusLogList(Page page, com.bobochang.warehouse.entity.BusLog busLog) {
        page = busLogService.queryBuslogPage(page, busLog);
        return Result.ok(page);
    }
    
    @GetMapping("/all-buslog-name")
    public Result getAllBusLogName(){
        List<String> lists = busLogService.getAllBusLogName();
        return Result.ok(lists);
    }
}
