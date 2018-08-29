package com.imooc.activiti.dbentity;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

/*
*
* 通用设置的测试类
* */
public class DbRepostoryTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");
    @Test
    public void testDeploy(){
        activitiRule.getRepositoryService().createDeployment()
                .name("二次审批流程")
                .addClasspathResource("second_approve.bpmn20.xml").deploy();
    }
    /*对一个流程进行挂起操作*/
    @Test
    public void testSuspent(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        repositoryService.suspendProcessDefinitionById("second_approve:2:15004");//挂起流程
        System.out.println( repositoryService.isProcessDefinitionSuspended("second_approve:2:15004"));
        repositoryService.activateProcessDefinitionById("second_approve:2:15004");//激活流程
        System.out.println( repositoryService.isProcessDefinitionSuspended("second_approve:2:15004"));
    }
}
