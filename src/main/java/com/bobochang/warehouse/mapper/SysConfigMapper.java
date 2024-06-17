package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【sys_config】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.SysConfig
*/
public interface SysConfigMapper extends BaseMapper<SysConfig> {
    List<SysConfig> selectAllParam();
}




