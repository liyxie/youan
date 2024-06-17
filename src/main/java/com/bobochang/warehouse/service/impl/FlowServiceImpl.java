package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.Flow;
import com.bobochang.warehouse.service.FlowService;
import com.bobochang.warehouse.mapper.FlowMapper;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author HuihuaLi
* @description 针对表【flow】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow>
    implements FlowService{
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FlowMapper flowMapper;

    @Override
    public void insertFlow(Flow flow) {
        flowMapper.insertFlow(flow);
    }

    @Override
    public void updateFlow(Flow flow) {
        flowMapper.updateFlow(flow);
    }

    @Override
    public Flow selectByInstanceId(String instanceId) {
        return flowMapper.selectByInstanceId(instanceId);
    }

    @Override
    public Flow selectByContractId(Integer contractId) {
        QueryWrapper<Flow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_id", contractId);
        return flowMapper.selectList(queryWrapper).get(0);
    }

    @Override
    public Flow selectById(Integer flowId) {
        return flowMapper.selectById(flowId);
    }

    @Override
    public int updateReasonByContract(Flow flow) {
        return flowMapper.updateReasonByContract(flow);
    }

    @Override
    public void deleteByContractId(Integer contractId) {
        QueryWrapper<Flow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_id", contractId);
        flowMapper.delete(queryWrapper);
    }
}




