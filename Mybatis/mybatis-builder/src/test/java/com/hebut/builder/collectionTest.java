package com.hebut.builder;

import com.hebut.builder.pojo.Classes;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jxy on 2016/6/26.
 */

public class collectionTest {
    BuilderTest bt = new BuilderTest();
    @Test
    public void resultCollection() throws IOException {
        SqlSessionFactory factory = bt.getFactory();
        SqlSession sqlSession = factory.openSession(true);
        String statement = "com.hebut.builder.mapper.xml.class"+".getClass3";
        Classes classInfo = sqlSession.selectOne(statement, 1);
        System.out.println(classInfo);
    }
    @Test
    public void queryCollection() throws IOException {
        SqlSessionFactory factory = bt.getFactory();
        SqlSession sqlSession = factory.openSession(true);
        String statement = "com.hebut.builder.mapper.xml.class"+".getClass4";
        Classes classInfo = sqlSession.selectOne(statement, 2);
        System.out.println(classInfo);
    }

}
