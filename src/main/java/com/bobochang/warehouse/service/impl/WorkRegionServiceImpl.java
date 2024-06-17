package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.WorkRegion;
import com.bobochang.warehouse.service.WorkRegionService;
import com.bobochang.warehouse.mapper.WorkRegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author magic'book
* @description 针对表【work_region】的数据库操作Service实现
* @createDate 2023-12-12 10:42:16
*/
@Service
public class WorkRegionServiceImpl extends ServiceImpl<WorkRegionMapper, WorkRegion>
    implements WorkRegionService{
    @Autowired
    private WorkRegionMapper workRegionMapper;

    @Override
    public List<WorkRegion> queryAllWorkRegion() {
        return workRegionMapper.queryAllWorkRegion();
    }
}




