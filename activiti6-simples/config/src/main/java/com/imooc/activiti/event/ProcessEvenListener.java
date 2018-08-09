package com.imooc.activiti.event;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

public class ProcessEvenListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType type = event.getType();
        if(ActivitiEventType.PROCESS_STARTED.equals(type)){//监听流程启动类型
            System.out.println("流程启动："+event.getProcessInstanceId());
        }else if(ActivitiEventType.PROCESS_COMPLETED.equals(type)){//流程结束
            System.out.println("流程结束："+event.getProcessInstanceId());
        }else  if(ActivitiEventType.CUSTOM.equals(type)){
            System.out.println("自定义事件");
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
