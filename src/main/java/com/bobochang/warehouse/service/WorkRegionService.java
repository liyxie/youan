package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.WorkRegion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author magic'book
* @description 针对表【work_region】的数据库操作Service
* @createDate 2023-12-12 10:42:16
*/
public interface WorkRegionService extends IService<WorkRegion> {

    List<WorkRegion> queryAllWorkRegion();
}
