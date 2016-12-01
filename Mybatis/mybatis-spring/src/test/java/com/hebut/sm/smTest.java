package com.hebut.sm;

import com.hebut.sm.mapper.ano.UserMapper;
import com.hebut.sm.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)//使用Spring的测试框架
@ContextConfiguration(locations = {"classpath*:config/spring/beans.xml"})
public class smTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testAdd() {
        User user = new User(-1, "Jim", new Date(), 1234);
        mapper.save(user);

        int id = user.getId();
        System.out.println(id);
    }
    @Test
    public void update() {
        User user = mapper.findById(2);
        user.setSalary(2000);
        mapper.update(user);
    }

    @Test
    public void delete() {
        mapper.delete(3);
    }

    @Test
    public void findById() {
        User user = mapper.findById(2);
        System.out.println(user);
    }

    @Test
    public void findAll() {
        List<User> users = mapper.findAll();
        System.out.println(users);
    }

}
