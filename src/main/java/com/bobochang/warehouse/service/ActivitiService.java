package com.bobochang.warehouse.service;

import com.bobochang.warehouse.dto.ContractReasonDto;
import com.bobochang.warehouse.dto.EginnerContractDto;
import com.bobochang.warehouse.dto.PurchaseReasonDto;
import com.bobochang.warehouse.entity.Contract;
import com.bobochang.warehouse.entity.Flow;
import com.bobochang.warehouse.entity.Result;

import java.util.List;
import java.util.Map;

/**
 * 工作流服务类
 */
public interface ActivitiService {
    // 上传流程定义的xml文件
    public String xmlUpload(String file,String fileName);

    // 查看用户是否有任务
    public Result haveTask(int userId);

    // 开启流程实例
    Result startInstance(EginnerContractDto contract);

    // 完成流程实例中的任务
    public Result completeTask(String userCode, Flow flow);
    
    // 查看用户所有流程实例的任务
    List<Object> searchTask(List<String> roleCodes);
    
    Result skipTask(String userCode, ContractReasonDto contractReasonDto) throws Exception;

    Result skipPurchaseTask(String userCode, PurchaseReasonDto purchaseReasonDto, String taskId) throws Exception;

    Result againInstance(Contract contract);
}
