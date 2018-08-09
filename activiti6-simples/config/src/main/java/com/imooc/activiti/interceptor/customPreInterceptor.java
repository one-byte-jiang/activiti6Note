package com.imooc.activiti.interceptor;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.activiti.engine.impl.interceptor.CommandInterceptor;

public class customPreInterceptor implements CommandInterceptor{
    protected CommandInterceptor next;
    @Override
    public <T> T execute(CommandConfig config, Command<T> command) {
        System.out.println("pre进来了...");
        return   this.getNext().execute(config,command);
    }

    @Override
    public CommandInterceptor getNext() {
        return this.next;
    }

    @Override
    public void setNext(CommandInterceptor next) {
this.next=next;
    }
}
