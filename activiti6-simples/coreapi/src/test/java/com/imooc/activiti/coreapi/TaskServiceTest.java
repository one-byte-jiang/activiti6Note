package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class TaskServiceTest {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();
    @Test
    @Deployment(resources = "my-process-task.bpmn20.xml")
    public void testTaskService(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();
        Map<String,Object> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("message","test message");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process",variables);
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println("task desc"+task.getDescription());
        taskService.setVariable(task.getId(),"test1","value1");
        taskService.setVariableLocal(task.getId(),"local1","value2");
        Map<String, Object> variables2 = taskService.getVariables(task.getId());
        Map<String, Object> variablesLocal = taskService.getVariablesLocal(task.getId());

        Map<String, Object> variables1 = runtimeService.getVariables(task.getExecutionId());

        System.out.println("variables2 = "+variables2);
        System.out.println("variablesLocal = "+variablesLocal);
        System.out.println("variables1 = "+variables1);

        variables.put("cKey1","cValue1");
        taskService.complete(task.getId(),variables);//向下驱动流程
    }

    @Test
    @Deployment(resources = "my-process-task.bpmn20.xml")
    public void testTaskServiceUser(){//跟用户关联
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();
        Map<String,Object> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("message","test message");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process",variables);
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println("task desc"+task.getDescription());
        taskService.setOwner(task.getId(),"user1");//指定办理人
        List<Task> dajiang = taskService.createTaskQuery().taskCandidateUser("dajiang").taskUnassigned().listPage(0, 100);
        for (Task t:dajiang) {
            taskService.claim(t.getId(),"dajiang");
        }
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink:identityLinksForTask) {
            System.out.println("identityLink = "+identityLink);
        }
        //查询当前dajiang的可执行任务
               List<Task> dajiang1 = taskService.createTaskQuery().taskAssignee("dajiang").listPage(0, 100);
        for (Task t:dajiang1) {
            Map<String,Object> vars = Maps.newConcurrentMap();//创建一个参数
            vars.put("ckey1","cvalue1");
            taskService.complete(t.getId(),vars);
        }
        //看一下流程是否还存在
        List<Task> dajiang2 = taskService.createTaskQuery().taskAssignee("dajiang").listPage(0, 100);
        System.out.println("是否存在："+CollectionUtils.isEmpty(dajiang2));
    }

    @Test
    @Deployment(resources = "my-process-task.bpmn20.xml")
    public void testTaskAttachment(){//任务附件
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();
        Map<String,Object> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("message","test message");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process",variables);
        Task task = taskService.createTaskQuery().singleResult();
        //添加一个附件
        taskService.createAttachment("url",task.getId(),processInstance.getProcessInstanceId(),"附件","我的附件","https://www.duba.com");
        //查询任务附件
        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment a:taskAttachments) {
            System.out.println("任务附件："+ToStringBuilder.reflectionToString(a,ToStringStyle.JSON_STYLE));
        }
    }

    @Test
    @Deployment(resources = "my-process-task.bpmn20.xml")
    public void testTaskComment(){//任务评论
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();
        Map<String,Object> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("message","test message");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process",variables);
        Task task = taskService.createTaskQuery().singleResult();
        //添加多个备注
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"我是备注1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"我是备注2");
        //查询任务备注
        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment a:taskComments) {
            System.out.println("任务备注："+ToStringBuilder.reflectionToString(a,ToStringStyle.JSON_STYLE));
        }
        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event a:taskEvents) {
            System.out.println("任务事件："+ToStringBuilder.reflectionToString(a,ToStringStyle.JSON_STYLE));
        }
    }
}
