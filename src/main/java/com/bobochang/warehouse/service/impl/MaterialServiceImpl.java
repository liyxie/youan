package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.InStore;
import com.bobochang.warehouse.entity.Material;
import com.bobochang.warehouse.entity.Product;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.mapper.MaterialMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【material(商品表)】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material>
    implements MaterialService {
    @Autowired
    private MaterialMapper materialMapper;
    @Override
    public Page queryMaterialPage(Page page, Material material) {

        //查询商品总行数
        int materialCount = materialMapper.selectMaterialCount(material);

        //分页查询商品
        List<Material> materialList = materialMapper.selectMaterialPage(page, material);

        //将查询到的总行数和当前页数据组装到Page对象
        page.setTotalNum(materialCount);
        page.setResultList(materialList);

        return page;
    }

    @Override
    public Result saveMaterial(Material material) {
        //添加商品
        int i = materialMapper.insert(material);

        if(i>0){
            return Result.ok("添加材料成功！");
        }

        return Result.err(Result.CODE_ERR_BUSINESS, "添加材料失败！");
    }

    @Override
    public Result updateMaterial(Material material) {
        int i = materialMapper.updateMaterialById(material);

        if(i>0){
            return Result.ok("修改材料成功！");
        }

        return Result.err(Result.CODE_ERR_BUSINESS, "修改材料失败！");
    }

    @Override
    public Result deleteMaterial(Integer materialId) {
        int i = materialMapper.deleteById(materialId);

        if(i>0){
            return Result.ok("删除材料成功！");
        }

        return Result.err(Result.CODE_ERR_BUSINESS, "删除材料失败！");
    }

    @Override
    public List<Material> queryAllMaterial() {
        return materialMapper.queryAllMaterial();
    }
    
    @Override
    public int addInventById(InStore inStore) {
        int result = materialMapper.addInventById(inStore);
        if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int reduceById(Integer materialId, double materialNum) {
        int result = materialMapper.reduceById(materialId, materialNum);
        if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Material selectMaterialByName(String materialName) {
        return materialMapper.selectMaterialByName(materialName);
    }

    @Override
    public Page materialPageListByContract(Page page, Integer contractId) {
        //查询商品总行数
        int materialCount = materialMapper.selectMaterialCountByProduct(contractId);

        //分页查询商品
        List<Material> materialList = materialMapper.selectMaterialPageByProduct(page, contractId);

        //将查询到的总行数和当前页数据组装到Page对象
        page.setTotalNum(materialCount);
        page.setResultList(materialList);

        return page;
    }

    @Override
    public Page materialPageListByContractMaterial(Page page, Integer contractId) {
        //查询商品总行数

        //分页查询商品
        List<Material> materialList = materialMapper.selectMaterialPageByContract(page, contractId);

        //将查询到的总行数和当前页数据组装到Page对象
        page.setResultList(materialList);

        return page;    
    }

    @Override
    public List<Material> materialListByContractMaterial(Integer contractId) {
        List<Material> materialList = materialMapper.selectMaterialByContract(contractId);
        return materialList;
    }
}




