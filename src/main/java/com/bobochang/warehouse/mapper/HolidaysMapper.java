package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.Holidays;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @author HuihuaLi
* @description 针对表【holidays】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.Holidays
*/
public interface HolidaysMapper extends BaseMapper<Holidays> {
    // 判断今天是否是特殊节假日
    public Integer searchTodayIsHolidays();
    public ArrayList<String> searchHolidaysInRange(HashMap param);
}




