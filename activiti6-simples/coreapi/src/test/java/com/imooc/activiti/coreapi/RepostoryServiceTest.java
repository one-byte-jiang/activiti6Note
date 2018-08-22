package com.imooc.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.*;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class RepostoryServiceTest {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();
    @Test
    public void testRepostory(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();//获取Repostory
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();//创建一个流程部署对象
        deploymentBuilder.name("测试部署资源")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();//发布，会生产到DB中
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        Deployment deployment = deploymentQuery.deploymentId(deploy.getId()).singleResult();
        System.out.println(deployment.toString());
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).listPage(0, 100);
        System.out.println(processDefinitions.toString());
    }
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter(){
        //指定哪些用户才能启动流程
        RepositoryService repositoryService = activitiRule.getRepositoryService();//获取Repostory
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        //repositoryService.suspendProcessDefinitionById(processDefinition.getId());//暂停流程
        repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");//指定用户
        repositoryService.addCandidateStarterGroup(processDefinition.getId(),"group");//指定用户组
        List<IdentityLink> identityLinksForProcessDefinition = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());//获取流程相关的用户认证信息
        for (IdentityLink identityLink:identityLinksForProcessDefinition) {
            System.out.println(identityLink);
        }
    }
}
