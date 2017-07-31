package com.profullstack.springseed.jproject.domain.components.repository;

import com.profullstack.springseed.jproject.domain.DomainAbstractTest;
import com.profullstack.springseed.jproject.domain.components.model.Group;
import com.profullstack.springseed.jproject.domain.components.model.Menu;
import com.profullstack.springseed.jproject.domain.components.model.Role;
import com.profullstack.springseed.jproject.domain.components.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by christianxiao on 4/16/16.
 */
public class UserRepositoryTest extends DomainAbstractTest {

    @Autowired
    private UserRepository rep;

    @Test
    public void testFindUserByLoginIdAndName() throws Exception {
        //User user = rep.findUserByLoginIdAndPassword("co1","123");
        User user = rep.findUserByLoginIdAndPassword2("co1","123").get(0);
        List<Group> groups = user.getGroups();
        List<Role> roles = new LinkedList<>();
        for(Group g:groups){
            for(Role r:g.getRoles()){
                roles.add(r);
            }
        }
        List<Menu> menus = new LinkedList<>();
        for(Role r: roles){
            for(Menu m: r.getMenus()){
                menus.add(m);
            }
        }

        Assert.assertEquals(user.getLoginId(),"co1");
        Assert.assertEquals(groups.get(0).getName(),"GROUP_CO");
        Assert.assertEquals(roles.get(0).getName(),"ROLE_CO");
        Assert.assertEquals(menus.get(0).getName(),"general_report");
        //
        Assert.assertEquals(user.getEmployeeCard().getDepartment(),"it");
        Assert.assertEquals(user.getPhones().iterator().next().getOs(),"android");
    }
}