package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.Unit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【unit(规格单位表)】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface UnitService extends IService<Unit> {
    List<Unit> queryAllUnit();
}
