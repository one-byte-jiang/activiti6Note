package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class RuntimeServiceTest {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @org.activiti.engine.test.Deployment(resources = "my-process.bpmn20.xml")
    public void testStartProcess(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String,Object> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("key1","value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);//开始流程。ID会变，KEY不会变，当然启动流程前要先部署流程，可以使用注解
        System.out.println("流程启动完成："+processInstance);
    }
    @Test
    @org.activiti.engine.test.Deployment(resources = "my-process.bpmn20.xml")
    public void testStartProcessById(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        Map<String,Object> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("key1","value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);//开始流程。ID会变，KEY不会变，当然启动流程前要先部署流程，可以使用注解
        System.out.println("流程启动完成："+processInstance);
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = "my-process.bpmn20.xml")
    public void testVariables(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        Map<String,Object> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("key1","value1");
        variables.put("key2","value2");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);//开始流程。ID会变，KEY不会变，当然启动流程前要先部署流程，可以使用注解
        System.out.println("流程启动完成："+processInstance);

        runtimeService.setVariable(processInstance.getId(),"key3","value3");
        runtimeService.setVariable(processInstance.getId(),"key2","test");
        Map<String, Object> variables1 = runtimeService.getVariables(processInstance.getId());
        System.out.println("variables1:"+variables1);
    }
    @Test
    @org.activiti.engine.test.Deployment(resources = "my-process.bpmn20.xml")
    public void testExecution(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());//开始流程。ID会变，KEY不会变，当然启动流程前要先部署流程，可以使用注解
        List<Execution> executions = runtimeService.createExecutionQuery().listPage(0, 100);
        for (Execution e:executions) {
            System.out.println(e);
        }
    }

}
