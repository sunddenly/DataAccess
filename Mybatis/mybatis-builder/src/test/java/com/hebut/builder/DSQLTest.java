package com.hebut.builder;

import com.hebut.builder.pojo.Classes;
import com.hebut.builder.pojo.User;
import com.hebut.builder.pojo.UserCondition;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by jxy on 2016/6/26.
 */

public class DSQLTest {
    BuilderTest bt = new BuilderTest();
    @Test
    public void conditionQuery() throws IOException {
        SqlSessionFactory factory = bt.getFactory();
        SqlSession sqlSession = factory.openSession(true);
        String statement = "com.hebut.builder.mapper.xml.conditionUser"+".getUser";
        String name ="o";
//        name=null;
        UserCondition parameter = new UserCondition("%" + name + "%", 12, 18);
        List<User> list = sqlSession.selectList(statement, parameter);
        System.out.println(list);
    }


}
