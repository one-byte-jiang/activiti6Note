package com.imooc.activiti.dbentity;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

/*
*
* 用户身份测试类
* */
public class DbIdentityTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");
    @Test
    public void testIdentity(){
        IdentityService identityService = activitiRule.getIdentityService();
        User user1 = identityService.newUser("user1");
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setEmail("test@qq.com");
        user1.setPassword("pwd");
        identityService.saveUser(user1);
        Group group1 = identityService.newGroup("group1");
        group1.setName("test");
        identityService.saveGroup(group1);
        identityService.createMembership(user1.getId(),group1.getId());
        identityService.setUserInfo(user1.getId(),"age","18");//设置用户扩展信息
    }
}
