package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.Workday;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @author HuihuaLi
* @description 针对表【workday】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.Workday
*/
public interface WorkdayMapper extends BaseMapper<Workday> {
    // 判断今天是否是特殊工作日
    public Integer searchTodayIsWorkday();
    public ArrayList<String> searchWorkdayInRange(HashMap param);
}




