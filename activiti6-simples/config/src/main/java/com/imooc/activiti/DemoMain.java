package com.imooc.activiti;

import com.google.common.collect.Maps;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DemoMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    public static void main(String[] args) throws ParseException {
        LOGGER.info("启动我们的程序");
        //创建流程引擎
        ProcessEngine processEngine = getProcessEngine();
        //部署定义好的流程文件
        ProcessDefinition processDefinition = getProcessDefinition(processEngine);
        //启动运行流程
        ProcessInstance processInstance = 启动运行流程(processEngine, processDefinition);
        //处理流程任务
        Scanner scanner = new Scanner(System.in);
        while (processInstance!=null && !processInstance.isEnded()){
            TaskService taskService = processEngine.getTaskService();
            List<Task> list = taskService.createTaskQuery().list();
            for (Task t:list) {
                LOGGER.info("待处理的任务:{}",t.getName());
                FormService formService = processEngine.getFormService();
                TaskFormData taskFormData = formService.getTaskFormData(t.getId());
                List<FormProperty> formProperties = taskFormData.getFormProperties();
                Map<String,Object> map = Maps.newHashMap();
                for (FormProperty form:formProperties) {
                    LOGGER.info("请输入 {}",form.getName());
                    if(StringFormType.class.isInstance(form.getType())){
                        String s = scanner.nextLine();
                        map.put(form.getId(),s);
                    }else if(DateFormType.class.isInstance(form.getType())){
                        String s = scanner.nextLine();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddd");
                        map.put(form.getId(),sdf.parse(s));
                    }
                }
                taskService.complete(t.getId(),map);
                processInstance=processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
          }

        }

        LOGGER.info("结束我们的程序");
    }

    private static ProcessInstance 启动运行流程(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动流程{}",processInstance.getProcessDefinitionKey());
        return processInstance;
    }

    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        String id = deploy.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(id).singleResult();
        LOGGER.info("流程定义文件{},流程ID{}",processDefinition.getName(),processDefinition.getId());
        return processDefinition;
    }

    private static ProcessEngine getProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = cfg.buildProcessEngine();
        String name = processEngine.getName();
        String version = processEngine.VERSION;
        LOGGER.info("名字：{},版本:{}",name,version);
        return processEngine;
    }
}
