package com.hebut.builder;

import com.hebut.builder.mapper.ano.UserMapper;
import com.hebut.builder.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Created by jxy on 2016/6/26.
 */
public class annotationTest {
   BuilderTest bt = new BuilderTest();

    @Test
    public void testAdd() throws IOException {
        SqlSessionFactory sessionFactory = bt.getFactory();
        SqlSession sqlSession = sessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int i = mapper.insertUser(new User(-1, "55", 45));
        System.out.println(i);
        sqlSession.close();
    }
}
