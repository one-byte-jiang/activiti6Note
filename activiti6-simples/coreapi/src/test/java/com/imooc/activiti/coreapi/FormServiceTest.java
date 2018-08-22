package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class FormServiceTest {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();
    @Test
    @Deployment(resources = "my-process-form.bpmn20.xml")
    public void testFormService(){
        FormService formService = activitiRule.getFormService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();//获取流程定义文件
        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        System.out.println("startFormKey = "+startFormKey);
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> formProperties = startFormData.getFormProperties();//获取表单的属性
        for (FormProperty form:formProperties) {
            System.out.println("forms属性："+ToStringBuilder.reflectionToString(form,ToStringStyle.JSON_STYLE));
        }
        Map<String,String> variables = Maps.newConcurrentMap();//创建一个参数
        variables.put("message","test message");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variables);//我们用提交表单的方式开启流程
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> properties = taskFormData.getFormProperties();//获取表单的属性
        for (FormProperty form:properties) {
            System.out.println("forms属性："+ToStringBuilder.reflectionToString(form,ToStringStyle.JSON_STYLE));
        }
        variables.put("yesORno","yes");
        formService.submitTaskFormData(task.getId(),variables);
        task = activitiRule.getTaskService().createTaskQuery().singleResult();//查询一下task是否还存在
        System.out.println("task是否存在："+task);//如果为null说明任务已经完成了
    }
}
