package com.bobochang.warehouse.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Store;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.entity.SupplyPayable;
import com.bobochang.warehouse.mapper.SupplyMapper;
import com.bobochang.warehouse.mapper.SupplyPayableMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.SupplyPayableService;
import com.bobochang.warehouse.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

//指定缓存的名称即键的前缀,一般是@CacheConfig标注的类的全类名
@CacheConfig(cacheNames = "com.bobochang.warehouse.service.impl.SupplyServiceImpl")
@Service
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply>
        implements SupplyService {

    //注入SupplyMapper
    @Autowired
    private SupplyMapper supplyMapper;

    /*
      查询所有供应商的业务方法
     */
    //对查询到的所有供应商进行缓存,缓存到redis的键为all:supply
    @Cacheable(key = "'all:supply'")
    @Override
    public List<Supply> queryAllSupply() {
        //查询所有供应商
        return supplyMapper.findAllSupply();
    }

    /**
     * 分页查询供应商信息
     * @param page
     * @param supply
     * @return
     */
    @Override
    public Page querySupplyPage(Page page, Supply supply) {
        //查询供应商总行数
        int storeCount = supplyMapper.selectSupplyCount(supply);

        //分页查询供应商
        List<Supply> storeList = supplyMapper.selectSupplyPage(page, supply);

        //将查到的总行数和当前页数据封装到Page对象
        page.setTotalNum(storeCount);
        page.setResultList(storeList);

        return page;
    }

    /**
     * 删除
     * @param supplyId
     * @return
     */
    @Override
    public Result deleteSupply(Integer supplyId) {
        //根据仓库id删除仓库
        int i = supplyMapper.deleteSupplyById(supplyId);
        if(i>0){
            return Result.ok("供应商删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "供应商删除失败！");
    }

    /**
     * 修改
     * @param supply
     * @return
     */
    @Override
    public Result updateSupply(Supply supply) {
        //根据供应商id修改供应商
        int i = supplyMapper.updateSupplyById(supply);
        if(i>0){
            return Result.ok("供应商修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "供应商修改失败！");
    }

    /**
     * 增加供应商
     * @param supply
     * @return
     */
    @Override
    public Result saveSupply(Supply supply) {
        //添加供应商
        int i = supplyMapper.insertSupply(supply);
        if(i>0){
            return Result.ok("供应商添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "供应商添加失败！");    }

    /**
     * 校验供应商编号
     * @param supplyNum
     * @return
     */
    @Override
    public Result checkSupplyNum(String supplyNum) {
        Supply supply = supplyMapper.selectSupplyByNum(supplyNum);
        return Result.ok(supply==null);
    }

    @Override
    public List<Supply> selectSupplyByMaterialId(String materialId) {
        
        return supplyMapper.selectSupplyByMaterialId(Integer.valueOf(materialId));
    }

    @Override
    public Supply selectOneByUserId(int userId) {
        return supplyMapper.selectOneByUserId(userId);
    }
}
