package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.MaterialSupply;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.entity.User;
import com.bobochang.warehouse.mapper.MaterialSupplyMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.MaterialSupplyService;
import com.bobochang.warehouse.service.SupplyService;
import com.bobochang.warehouse.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【material_supply】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
@Slf4j
public class MaterialSupplyServiceImpl extends ServiceImpl<MaterialSupplyMapper, MaterialSupply>
    implements MaterialSupplyService {
    @Autowired
    private MaterialSupplyMapper materialSupplyMapper;
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private SupplyService supplyService;
    @Override
    public BigDecimal selectPrice(MaterialSupply materialSupply){
//        QueryWrapper<MaterialSupply> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("material_id", materialSupply.getMaterialId());
//        queryWrapper.eq("supply_id", materialSupply.getSupplyId());
        
        MaterialSupply targetMaterialSupply = materialSupplyMapper.selectByMaterialIdAndSupply(materialSupply);
        return targetMaterialSupply.getQuotation();
    }

    @Override
    public Page queryPage(Page page, MaterialSupply materialSupply, String userCode) {
        log.info(String.valueOf(materialSupply.getInspectionResult()));
        
        User user = userInfoService.findUserByCode(userCode);
        List<String> assignee = userInfoService.searchRoleCodeById(user.getUserId());

        // 如果是管理员可以看到所有的，如果不是只能看到自己的
        if(!assignee.get(0).equals("supper_manage") && !assignee.get(0).equals("inspect_man")){
            Supply supply = supplyService.selectOneByUserId(user.getUserId());
            materialSupply.setSupplyId(supply.getSupplyId());
        }
        
        // 查询总行数
        int count = materialSupplyMapper.queryCount(materialSupply);
        
        List<MaterialSupply> materialSupplyList = materialSupplyMapper.queryPage(page, materialSupply);
        
        page.setResultList(materialSupplyList);
        page.setTotalNum(count);
        return page;
    }

    @Override
    public Result saveMaterial(MaterialSupply material) {
        int i = materialSupplyMapper.insert(material);
        if(i>0){
            return Result.ok("添加材料成功！");
        }

        return Result.err(Result.CODE_ERR_BUSINESS, "添加材料失败！");
    }

    @Override
    public Result updateMaterialSupplyById(MaterialSupply materialSupply) {
        // 根据合同 id 修改合同昵称
        int i = materialSupplyMapper.updateMaterialSupplyById(materialSupply);
        if (i > 0) {
            return Result.ok("修改成功");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "修改失败");
    }

    @Override
    public int updateState(MaterialSupply materialSupply) {
        return materialSupplyMapper.updateStateById(materialSupply);
    }

    @Override
    public MaterialSupply selectById(Integer id) {
        return materialSupplyMapper.selectMaterialSupplyById(id);
    }

    @Override
    public int updateMaterialIdById(MaterialSupply materialSupply) {
        return materialSupplyMapper.updateMaterialIdById(materialSupply);
    }
}




