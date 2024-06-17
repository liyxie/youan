package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.Flow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author HuihuaLi
* @description 针对表【flow】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface FlowService extends IService<Flow> {
    // 添加工作流业务记录
    public void insertFlow(Flow flow);

    // 更新工作流业务记录
    void updateFlow(Flow flow);

    // 根据流程实例id查询工作流记录
    Flow selectByInstanceId(String instanceId);

    Flow selectByContractId(Integer contractId);

    Flow selectById(Integer flowId);

    int updateReasonByContract(Flow flow);

    void deleteByContractId(Integer contractId);
}
