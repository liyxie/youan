package com.bobochang.warehouse.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.entity.Store;
import com.bobochang.warehouse.page.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreMapper extends BaseMapper<Store> {

    //查询所有仓库的方法
    public List<Store> findAllStore();

    //查询仓库总行数的方法
    public int selectStoreCount(Store store);

    //分页查询仓库的方法
    public List<Store> selectStorePage(Page page, Store store);

    //根据仓库编号查询仓库的方法
    public Store selectStoreByNum(String storeNum);

    //添加仓库的方法
    public int insertStore(Store store);

    //根据仓库id修改仓库的方法
    public int updateStoreById(Store store);

    //根据仓库id删除仓库的方法
    public int deleteStoreById(Integer storeId);
}
