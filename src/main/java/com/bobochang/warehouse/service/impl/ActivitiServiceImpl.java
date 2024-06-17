package com.bobochang.warehouse.service.impl;

import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bobochang.warehouse.dto.ContractReasonDto;
import com.bobochang.warehouse.dto.EginnerContractDto;
import com.bobochang.warehouse.dto.PurchaseReasonDto;
import com.bobochang.warehouse.dto.TaskDTO;
import com.bobochang.warehouse.entity.Contract;
import com.bobochang.warehouse.entity.Flow;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.User;
import com.bobochang.warehouse.service.ActivitiService;
import com.bobochang.warehouse.service.ContractService;
import com.bobochang.warehouse.service.FlowService;
import com.bobochang.warehouse.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LI
 * @date 2023/10/9
 */
@Service
@Slf4j
public class ActivitiServiceImpl implements ActivitiService {
    @Value("${file.xml-upload-path}")
    private String xmlUploadPath;

    @Autowired
    private UserInfoService userService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Value("${warehouse.deploymentId}")
    private String deploymentId;

    /**
     * 上传流程定义的xml文件
     * @param file xml文件
     * @param fileName 文件名称
     * @return
     */
    @Override
    public String xmlUpload(String file,String fileName) {
        if (file.isEmpty()) {
            return "";
        }
        try {
            //拿到图片上传到的目录(类路径classes下的static/img/upload)的File对象
            File uploadDirFile = ResourceUtils.getFile(xmlUploadPath);
            //拿到图片上传到的目录的磁盘路径
            String uploadDirPath = uploadDirFile.getAbsolutePath();
            // 获得文件字节流并写入文件
            byte[] fileBytes = file.getBytes();
            String filePath = uploadDirPath+"\\"+fileName+".bpmn20.xml";
            Files.write(Path.of(filePath), fileBytes);
            return filePath;
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 查看用户是否有任务未完成
     * @param userId 用户id
     * @return
     */
    @Override
    public Result haveTask(int userId) {
       List<String> roleCodes = userService.searchRoleCodeById(userId);
        // 将查询到的任务用系统实例类封装返回，否则会有延迟加载的错误
        List<TaskDTO> taskDTOList = new ArrayList<>();

        // 查询未审核的合同列表，如果有为审核的合同
        Contract contract = new Contract();
        contract.setContractState("0");
        int contractCount = contractService.searchContractCount(contract);
        
        for (String roleCode: roleCodes){
            // 如果是超级管理员则看合同有没有审核
            if (roleCode.equals("supper_manage")){
                if(contractCount == 0){
                    return Result.ok("暂无要处理的合同");
                }else{
                    return Result.ok("有还未审核的合同");
                }
            } else{
                // 其他用户则是查询是否有任务
                TaskQuery query = taskService.createTaskQuery().taskAssignee(roleCode);
                List<Task> taskList = query.list();
                
                for (Task task : taskList) {
                    TaskDTO taskDTO = new TaskDTO();
                    BeanUtils.copyProperties(task, taskDTO);
                    taskDTOList.add(taskDTO);
                }
            }
        }
        return Result.ok(taskDTOList);
    }

    /**
     * 开启流程实例
     * @param contract 包括合同状态以及工作流的类型
     * @return
     */
    @Override
    @Transactional
    public Result startInstance(EginnerContractDto contract) {
        try{
            System.out.println(Integer.valueOf(contract.getIfPurchase()));
            // 判断合同类型
            if (Integer.valueOf(contract.getIfPurchase()) == 3){
                contractService.saveContractEginner(contract);
            }else{
                contractService.saveContract(contract);
            }
            
            // 启动流程实例
            Map<String, Object> variables = new HashMap<>();
            variables.put("produce_man", "produce_man");
            variables.put("out_store", "out_store");
            variables.put("supper_manage", "supper_manage");
            variables.put("purchase_man", "purchase_man");
            variables.put("station_master","station_master");
            variables.put("in_store", "in_store");
            if (Integer.parseInt(contract.getIfPurchase()) == 3){
                variables.put("status", 0);
            }else {
                variables.put("status", Integer.valueOf(contract.getIfPurchase()));
            }
            
            if (Integer.parseInt(contract.getIfPurchase()) == 2){
                variables.put("only", 1);
            }else{
                variables.put("only", 0);
            }
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(deploymentId,variables);

            // 保存实例记录
            Flow flow = new Flow();
            flow.setInstanceId(processInstance.getId());
            flow.setContractId(contract.getContractId());
            if (Integer.parseInt(contract.getIfPurchase()) == 3){
                flow.setState(0);
            }else {
                flow.setState(Integer.valueOf(contract.getIfPurchase()));
            }
            flowService.insertFlow(flow);

            // 完成第一个合同创建的任务
            Task task = taskService.createTaskQuery()
                    .processInstanceId(flow.getInstanceId())
                    .singleResult();
            taskService.complete(task.getId());

            return Result.ok("启动流程成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 完成流程中的任务
     * @param userCode 用户代码
     * @param flow 流程实例
     */
    @Override
    public Result completeTask(String userCode, Flow flow) {
        User user = userService.findUserByCode(userCode);
        List<String> assignees = userService.searchRoleCodeById(user.getUserId());
        
        for(String assignee : assignees){
            System.out.println(assignee);
            String taskId = "";
            String instanceId = flowService.selectByContractId(flow.getContractId()).getInstanceId();


            if(assignee.equals("supper_manage")){
                // 根据角色查看自身未完成的任务并完成任务
                TaskQuery query = taskService.createTaskQuery().processInstanceId(instanceId);

                for (Task task: query.list()){
                    log.info(task.getProcessInstanceId());
                    taskId = task.getId();
                    break;
                }
            }else {
                // 根据角色查看自身未完成的任务并完成任务
                TaskQuery query = taskService.createTaskQuery().taskAssignee(assignee);

                for (Task task: query.list()){
                    if(instanceId.equals(task.getProcessInstanceId())){
                        taskId = task.getId();
                        break;
                    }
                }
            }
            
            if (!Objects.equals(taskId, "")){
                taskService.complete(taskId);

                // 更新工作流记录
                flowService.updateFlow(flow);
                break;
            }
        }
        
        return Result.ok("完成任务");
    }

    /**
     * 查看用户所有的流程任务
     * @param roleCodes 用户权限
     * @return
     */
    @Override
    public List<Object> searchTask(List<String> roleCodes) {
        List<Object> result = new ArrayList<>();

        for (String roleCode:roleCodes){
            List<Map<String, String>> taskNodes = searchAllTaskByDefinitionId();
            result.addAll(runningTask(roleCode,taskNodes));
            result.addAll(historyTask(roleCode,taskNodes));
            if(roleCode.equals("supper_manage")){
                result.addAll(allTask(roleCode,taskNodes));
            }
        }
        return result.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 查询的是轮到用户完成任务的流程实例
     * @param assignee 用户角色信息
     * @param taskNodes 所有任务列表
     * @return list
     */
    private List<Object> runningTask(String assignee,List<Map<String, String>> taskNodes){
        TaskQuery runningQuery = taskService.createTaskQuery().taskAssignee(assignee);
        List<Task> runningTasks = runningQuery.list();

        List<Object> result = new ArrayList<>();
        if(!runningTasks.isEmpty()){
            for(Task task : runningTasks){
                Map<String, Object> taskMap = new HashMap<>();

                taskMap.put("instanceId",task.getProcessInstanceId());
                // 流程当前进行的任务和负责人
                Task nowTask = taskService.createTaskQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .singleResult();


                TaskDTO taskDTO = new TaskDTO();
                BeanUtils.copyProperties(nowTask,taskDTO);
                taskMap.put("task", taskDTO.getName());
                taskMap.put("assignee",taskDTO.getAssignee());

                // 获得流程的所有任务
                Flow flow = flowService.selectByInstanceId(task.getProcessInstanceId());
                taskMap.put("allTask",differentTask(flow.getState(),taskNodes));

                // 获得当前流程所属合同名称
                Contract contract = contractService.findContractById(flow.getContractId());
                taskMap.put("contractId", flow.getContractId());
                taskMap.put("contractName", contract.getContractName());
                taskMap.put("reason", flow.getReason());

                taskMap.put("flag","进行中");

                result.add(taskMap);
            }
        }
        return result;
    }

    /**
     * 查询的是用户历史流程实例
     * @param assignee 用户的角色信息
     * @param taskNodes 所有任务列表
     * @return list
     */
    private List<Object> historyTask(String assignee,List<Map<String, String>> taskNodes){
        List<Object> list = new ArrayList<>();

        HistoricProcessInstanceQuery historicQuery = historyService.createHistoricProcessInstanceQuery()
                .involvedUser(assignee)
                .finished();
        List<HistoricProcessInstance> historicProcessInstances = historicQuery.list();
        if (!historicProcessInstances.isEmpty()){
            for (HistoricProcessInstance historicProcessInstance: historicProcessInstances){
                Map<String, Object> taskMap = new HashMap<>();

                taskMap.put("instanceId", historicProcessInstance.getId());

                Flow flow = flowService.selectByInstanceId(historicProcessInstance.getId());
                taskMap.put("allTask",differentTask(flow.getState(),taskNodes));

                // 获得当前流程所属合同名称
                Contract contract = contractService.findContractById(flow.getContractId());
                taskMap.put("contractId", flow.getContractId());
                taskMap.put("contractName", contract.getContractName());
                taskMap.put("reason", flow.getReason());

                taskMap.put("flag","已结束");

                list.add(taskMap);
            }
        }
        return list;
    }

    /**
     * 超级管理员用于查询正在进行中的所有流程实例
     * @param assignee 用户角色信息
     * @param taskNodes 所有任务的列表
     * @return list
     */
    private List<Object> allTask(String assignee,List<Map<String, String>> taskNodes){
        List<Object> list = new ArrayList<>();
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .processDefinitionId(deploymentId)
                .list();
        for (ProcessInstance processInstance : processInstances){
            Map<String, Object> taskMap = new HashMap<>();

            taskMap.put("instanceId",processInstance.getProcessInstanceId());

            // 流程当前进行的任务和负责人
            Task nowTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getProcessInstanceId())
                    .singleResult();

            TaskDTO taskDTO = new TaskDTO();
            BeanUtils.copyProperties(nowTask,taskDTO);
            taskMap.put("task", taskDTO.getName());
            taskMap.put("assignee",taskDTO.getAssignee());

            Flow flow = flowService.selectByInstanceId(processInstance.getId());

            // 获得流程的所有任务
            taskMap.put("allTask", differentTask(flow.getState(), taskNodes));

            // 获得当前流程所属合同名称
            Contract contract = contractService.findContractById(flow.getContractId());
            taskMap.put("contractId", flow.getContractId());

            taskMap.put("contractName", contract.getContractName());
            taskMap.put("reason", flow.getReason());

            taskMap.put("flag","进行中");

            list.add(taskMap);
        }
        return list;
    }

    /**
     * 根据部署的流程定义id查询该流程的所有任务列表
     * @return taskNodes 返回该流程定义的所有任务
     */
    private List<Map<String, String>> searchAllTaskByDefinitionId(){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(deploymentId)
                .singleResult();
        List<Map<String, String>> taskNodes = new ArrayList<>();


        if (processDefinition != null) {
            List<UserTask> userTasks = repositoryService.getBpmnModel(deploymentId)
                    .getMainProcess().findFlowElementsOfType(UserTask.class);

            for (UserTask userTask : userTasks) {
                Map<String, String> taskInfo = new HashMap<>();
                taskInfo.put("taskName",userTask.getName());
                taskInfo.put("taskAssignee",userTask.getAssignee());
                taskInfo.put("taskId",userTask.getId());
                taskNodes.add(taskInfo);
            }
        }

        return taskNodes;
    }

    /**
     * 区分不同流程实例的任务
     * @param taskState 任务状态
     * @param taskNodes 所有任务的列表
     * @return allTask 返回当前流程实例的任务列表
     */
    private List<Map<String, String>> differentTask(Integer taskState, List<Map<String, String>> taskNodes){
        List<Map<String, String>> allTask = new ArrayList<>(taskNodes);
        if(taskState == 0){
            allTask.removeIf(map -> 
                    map.get("taskName").equals("采购创建") || 
                    map.get("taskName").equals("入库确认") || 
                    map.get("taskName").equals("采购审批") || 
                    map.get("taskName").equals("采购检查"));
        }
        if(taskState == 2){
            allTask.removeIf(map ->
                    map.get("taskName").equals("生产完成") ||
                    map.get("taskName").equals("出库确认"));
        }
        return allTask;
    }
    

    @Override
    public Result skipTask(String userCode, ContractReasonDto contractReasonDto) throws Exception {
        // 获取实例id
        String instanceId = flowService.selectByContractId(contractReasonDto.getContractId()).getInstanceId();
        
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult(); 
        if (task == null) {
            throw new Exception("流程未启动或已执行完成，无法撤回");
        }
        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        //获取当前节点
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);
        //需要跳转的节点
        FlowNode toFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement("sid-01");
        if (toFlowNode == null) {
            throw new Exception("退回失败");
        }
        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());
        //清理活动方向
        flowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(toFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "跳转指定节点");
        //完成任务
        taskService.complete(task.getId());
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);
        log.info("跳转成功，from->{},to->{}", flowNode.getName(), toFlowNode.getName());
        
        // 更新合同的采购状态
//        Contract contract = new Contract();
//        contract.setContractId(contractReasonDto.getContractId());
//        contract.setIfPurchase(contractReasonDto.getIfPurchase());
//        if(contractService.updateContractIfPurchase(contract) < 1){
//            return Result.err(500, "合同状态更新失败");
//        }
        
        // 记录退回原因
        Flow flow = new Flow();
        flow.setContractId(contractReasonDto.getContractId());
        flow.setReason(contractReasonDto.getReason());
        if(flowService.updateReasonByContract(flow) < 1){
            return Result.err(500, "流程驳回原因更新失败");
        }
        return Result.ok("退回成功");
    }

    @Override
    public Result skipPurchaseTask(String userCode, PurchaseReasonDto purchaseReasonDto,String taskId) throws Exception {
        // 获取实例id
        String instanceId = flowService.selectByContractId(purchaseReasonDto.getContractId()).getInstanceId();

        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        if (task == null) {
            throw new Exception("流程未启动或已执行完成，无法撤回");
        }
        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        //获取当前节点
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);
        //需要跳转的节点
        FlowNode toFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskId);
        if (toFlowNode == null) {
            throw new Exception("退回失败");
        }
        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());
        //清理活动方向
        flowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(toFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "跳转指定节点");
        //完成任务
        taskService.complete(task.getId());
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);
        log.info("跳转成功，from->{},to->{}", flowNode.getName(), toFlowNode.getName());
        
        return Result.ok("执行成功");
    }

    /**
     * 重启流程实例
     * @param contract 包括合同状态以及工作流的类型
     * @return
     */
    @Override
    @Transactional
    public Result againInstance(Contract contract) {
        try{
            // 根据合同id删除运行中的工作流
            String instanceId = flowService.selectByContractId(contract.getContractId()).getInstanceId();
            runtimeService.deleteProcessInstance(instanceId, "驳回删除");
            historyService.deleteHistoricProcessInstance(instanceId);


            // 根据合同id删除flow中记录
            flowService.deleteByContractId(contract.getContractId());
            
            // 启动流程实例
            Map<String, Object> variables = new HashMap<>();
            variables.put("produce_man", "produce_man");
            variables.put("out_store", "out_store");
            variables.put("supper_manage", "supper_manage");
            variables.put("purchase_man", "purchase_man");
            variables.put("station_master","station_master");
            variables.put("in_store", "in_store");
            variables.put("status", Integer.valueOf(contract.getIfPurchase()));
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(deploymentId,variables);

            // 保存实例记录
            Flow flow = new Flow();
            flow.setInstanceId(processInstance.getId());
            flow.setContractId(contract.getContractId());
            flow.setState(Integer.valueOf(contract.getIfPurchase()));
            flowService.insertFlow(flow);

            // 完成第一个合同创建的任务
            Task task = taskService.createTaskQuery()
                    .processInstanceId(flow.getInstanceId())
                    .singleResult();
            taskService.complete(task.getId());

            return Result.ok("启动流程成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
