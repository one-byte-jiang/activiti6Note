package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class HistoryServiceTest {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");

    @Test
    @Deployment(resources = "my-process.bpmn20.xml")
    public  void  testHistory(){
        HistoryService historyService = activitiRule.getHistoryService();
        ProcessInstanceBuilder processInstanceBuilder = activitiRule.getRuntimeService().createProcessInstanceBuilder();
        Map<String,Object> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("key0","value0");
        variables.put("key1","value1");
        variables.put("key2","value2");
        Map<String,Object> transientvariables = Maps.newConcurrentMap();
        transientvariables.put("tkey1","tvalue1");
        transientvariables.put("tkey2","tvalue2");
        transientvariables.put("tkey3","tvalue3");
        ProcessInstance start = processInstanceBuilder.processDefinitionKey("my-process")
                .variables(variables)
                .transientVariables(transientvariables).start();

        Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(start.getId()).singleResult();
        activitiRule.getTaskService().complete(task.getId());
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().listPage(0, 100);

    }
}
