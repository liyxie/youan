package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.Unit;
import com.bobochang.warehouse.mapper.UnitMapper;
import com.bobochang.warehouse.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【unit(规格单位表)】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit>
    implements UnitService {

    @Autowired
    private UnitMapper unitMapper;
    
    @Override
    public List<Unit> queryAllUnit() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        List<Unit> list = unitMapper.selectList(queryWrapper);
        return list;
    }
}




