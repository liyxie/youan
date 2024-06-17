package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.WorkRegion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author magic'book
* @description 针对表【work_region】的数据库操作Mapper
* @createDate 2023-12-12 10:42:16
* @Entity com.bobochang.warehouse.entity.WorkRegion
*/
public interface WorkRegionMapper extends BaseMapper<WorkRegion> {

    List<WorkRegion> queryAllWorkRegion();
}




