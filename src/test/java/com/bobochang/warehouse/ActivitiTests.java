package com.bobochang.warehouse;

import com.bobochang.warehouse.entity.Flow;
import com.bobochang.warehouse.entity.User;
import com.bobochang.warehouse.service.FlowService;
import com.bobochang.warehouse.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LI
 * @date 2023/10/19
 * 用于测试工作流
 */
@SpringBootTest
@Slf4j
public class ActivitiTests {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ManagementService managementService;
    
    @Value("${warehouse.deploymentId}")
    private String deploymentId;
    /**
     * 部署流程定义
     */
    @Test
    void createDeploy(){
        InputStream inputStream = getClass().getResourceAsStream("/processes/flowv1.bpmn20.xml");
        repositoryService.createDeployment()
                .addInputStream("/processes/flowv1.bpmn20.xml", inputStream)
                .deploy(); //demo2:1:81952be3-6e2b-11ee-a90a-48a47209a1e7
    }

    /**
     * 开始流程定义
     */
    @Test
    void startInstance(){
        Map<String, Object> variables = new HashMap<>();
        variables.put("produce_man", "produce_man");
        variables.put("out_store", "out_store");
        variables.put("supper_manage", "supper_manage");
        variables.put("purchase_man", "purchase_man");
        variables.put("station_master","station_master");
        variables.put("in_store", "in_store");
        variables.put("status", 0);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(deploymentId,variables);

        log.info(processInstance.getProcessInstanceId());
        log.info(processInstance.getId()); // 57a83077-74a5-11ee-8a7c-48a47209a1e7
    }

    // 跳转到指定任务节点
    void skip(String instanceId) throws Exception {
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
            throw new Exception("跳转的下一节点为空");
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
    }

    /**
     * 根据流程实例id获得流程实例的当前任务和执行人
     */
    @Test
    void getTaskAndAssigneeByInstance() throws Exception {
        Task nowTask = taskService.createTaskQuery()
                .processInstanceId("7175bded-76d4-11ee-ba66-48a47209a1e7")
                .singleResult();
        log.info(nowTask.getAssignee());
        log.info(nowTask.getName());

//       skip("57a83077-74a5-11ee-8a7c-48a47209a1e7");
//        log.info(nowTask.get);
    }




    /**
     * 根据assignee查看当前未完成的任务并完成
     */
    @Test
    void completeTaskByAssignee(){
        TaskQuery query = taskService.createTaskQuery().taskAssignee("supper_manage");
        System.out.println(query.list());
        taskService.complete(query.list().get(0).getId());
    }

    /**
     * 根据流程实例id查询任务
     */
    @Test
    void completeTaskByInstanceId(){
        List<String> list = new ArrayList<>();
        list.add("46ebc9c1-6e2f-11ee-9c8c-48a47209a1e7");
        TaskQuery query = taskService.createTaskQuery().processInstanceIdIn(list);
        System.out.println(query.list());
//        taskService.complete(query.list().get(0).getId());
    }

    /**
     * 候选人拾取并完成任务
     */
    @Test
    void claimTask(){
        //校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .processInstanceId("46ebc9c1-6e2f-11ee-9c8c-48a47209a1e7")
                .taskCandidateUser("supper_manage")//根据候选人查询
                .singleResult();

        System.out.println(task.getName());
    }

    @Test
    void clearActivitiData() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .processDefinitionId(deploymentId)
                .list();
        System.out.println(list);
//        // 删除每个流程实例
//        for(ProcessInstance processInstance:list) {
//            runtimeService.deleteProcessInstance(processInstance.getId(), "Reason for deletion");
//        }
    }
    
    @Autowired
    private HistoryService historyService;
    
    @Test
    void selectHis(){
        HistoricProcessInstanceQuery historicQuery = historyService.createHistoricProcessInstanceQuery()
                .involvedUser("supper_manage")
                .finished();
        List<HistoricProcessInstance> historicProcessInstances = historicQuery.list();
        System.out.println(historicProcessInstances);

        // 删除每个流程实例
//        for(HistoricProcessInstance processInstance:historicProcessInstances) {
//            historyService.deleteHistoricProcessInstance(processInstance.getId());
//        }
    }
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private FlowService flowService;
    @Test
    void completeTask(){
        User user = userInfoService.findUserByCode("admin");
        String assignee = userInfoService.searchRoleCodeById(user.getUserId()).toString();

        // 根据角色查看自身未完成的任务并完成任务
        TaskQuery query = taskService.createTaskQuery().taskAssignee(assignee);
        
        Integer contractId = 111;
        Flow flow = flowService.selectByContractId(contractId);
        
        String instanceId = "";
        for (Task task: query.list()){
            log.info(task.getProcessInstanceId());
            if(flow.getInstanceId().equals(task.getProcessInstanceId())){
                instanceId = task.getProcessInstanceId();
                break;
            }
        }
//        flow.setInstanceId(query.list().stream()
//                .map(Task::getProcessInstanceId)
//                .collect(Collectors.toList()).get(0));
//
//        taskService.complete(query.list().get(0).getId());
//
//        // 更新工作流记录
//        flowService.updateFlow(flow);
    }
}
