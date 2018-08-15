package com.imooc.activiti;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigJobTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

	@Test
	@Deployment(resources = {"com/imooc/activiti/my-process-job.bpmn20.xml"})
	public void test() {
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		assertNotNull(processInstance);

		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		/*自行编辑的 ↓↓*/
		activitiRule.getTaskService().complete(task.getId());

	}

}
