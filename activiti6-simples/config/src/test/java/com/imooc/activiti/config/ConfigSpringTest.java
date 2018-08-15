package com.imooc.activiti.config;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiMembershipEventImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@RunWith(SpringJUnit4ClassRunner.class)//基于Spring的junit测试方式
@ContextConfiguration(locations = "classpath:activiti-context.xml")
public class ConfigSpringTest {

	@Rule
	@Autowired
	public ActivitiRule activitiRule;
	@Autowired
	public RuntimeService runtimeService;
	@Autowired
	public TaskService taskService;


	@Test
	@Deployment(resources = {"com/imooc/activiti/my-process_spring.bpmn20.xml"})
	public void test() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
		assertNotNull(processInstance);

		Task task = taskService.createTaskQuery().singleResult();
		taskService.complete(task.getId());
		runtimeService.dispatchEvent(new ActivitiMembershipEventImpl(ActivitiEventType.CUSTOM));//手动触发一个事件
		assertEquals("Activiti is awesome!", task.getName());
	}

}
