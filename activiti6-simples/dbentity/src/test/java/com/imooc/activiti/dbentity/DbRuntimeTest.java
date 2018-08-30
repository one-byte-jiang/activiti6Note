package com.imooc.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ConcurrentMap;

/*
*
* 运行时的测试类
* */
public class DbRuntimeTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");
    @Test
    public void testRuntime(){
        activitiRule.getRepositoryService().createDeployment()
                .name("二次审批流程")
                .addClasspathResource("second_approve.bpmn20.xml").deploy();
        ConcurrentMap<String, Object> map = Maps.newConcurrentMap();
        map.put("age","18");
        activitiRule.getRuntimeService()
                .startProcessInstanceByKey("second_approve",map);
    }
    /*给参与表 ACT_RU_IDENTITYLINK 赋值*/
    @Test
    public void testSetOwner(){
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("second_approve").singleResult();
        taskService.setOwner(task.getId(),"user1");
    }
}
