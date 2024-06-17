package com.bobochang.warehouse.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.entity.Store;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.entity.SupplyPayable;
import com.bobochang.warehouse.page.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplyMapper extends BaseMapper<Supply> {

    //查询所有供应商的方法
    public List<Supply> findAllSupply();

    public int selectSupplyCount(Supply supply);

    public List<Supply> selectSupplyPage(Page page, Supply supply);

    public int deleteSupplyById(Integer supplyId);

    public int updateSupplyById(Supply supply);

    //添加仓库的方法
    public int insertSupply(Supply supply);

    public Supply selectSupplyByNum(String supplyNum);

    List<Supply> selectSupplyByMaterialId(@Param("materialId") Integer materialId);

    Supply selectOneByUserId(int userId);
}
