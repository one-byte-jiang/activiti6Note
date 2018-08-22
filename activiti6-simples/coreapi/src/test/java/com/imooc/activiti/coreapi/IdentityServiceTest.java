package com.imooc.activiti.coreapi;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class IdentityServiceTest {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();
    @Test
    public void testIdentity(){
        IdentityService identityService = activitiRule.getIdentityService();
        User user1 = identityService.newUser("user1");//创建用户
        user1.setEmail("user1@126.com");
        User user2 = identityService.newUser("user2");//创建用户
        user2.setEmail("user2@126.com");
        identityService.saveUser(user1);
        identityService.saveUser(user2);

        Group group1 = identityService.newGroup("group1");//创建分组
        identityService.saveGroup(group1);
        Group group2 = identityService.newGroup("group2");//创建分组
        identityService.saveGroup(group2);

        identityService.createMembership("user1","group1");//创建用户与分组的关系
        identityService.createMembership("user2","group1");//创建用户与分组的关系

        identityService.createMembership("user1","group2");//创建用户与分组的关系
        identityService.createMembership("user2","group2");//创建用户与分组的关系

        List<User> group11 = identityService.createUserQuery().memberOfGroup("group1").listPage(0, 100);//查询出属于这个组下的用户
        for (User u:group11) {
            System.out.println("用户："+ToStringBuilder.reflectionToString(u,ToStringStyle.JSON_STYLE));
        }
    }
}
