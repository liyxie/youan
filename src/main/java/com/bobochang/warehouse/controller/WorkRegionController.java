package com.bobochang.warehouse.controller;

import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Unit;
import com.bobochang.warehouse.entity.WorkRegion;
import com.bobochang.warehouse.service.WorkRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LI
 * @date 2023/12/12
 */
@RequestMapping("/workregion")
@RestController
public class WorkRegionController {
    @Autowired
    private WorkRegionService workRegionService;
    /**
     * 查询所有工区
     * @return
     */
    @RequestMapping("/workregion-list")
    public Result unitList() {
        //执行业务
        List<WorkRegion> workRegions = workRegionService.queryAllWorkRegion();
        //响应
        return Result.ok(workRegions);
    }

}
