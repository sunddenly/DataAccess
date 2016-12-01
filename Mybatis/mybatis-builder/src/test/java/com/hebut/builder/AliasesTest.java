package com.hebut.builder;

import com.hebut.builder.pojo.Order;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;

public class AliasesTest {
    BuilderTest bt = new BuilderTest();
    @Test
    public void sqlTest() throws IOException {
        SqlSessionFactory factory = bt.getFactory();
        SqlSession sqlSession = factory.openSession(true);

        String statement = "com.hebut.builder.mapper.xml.order"+".selectOrder";
        Order aaa = sqlSession.selectOne(statement,2);

        System.out.println(aaa);
    }
    @Test
    public void reultMapTest() throws IOException {
        SqlSessionFactory factory = bt.getFactory();
        SqlSession sqlSession = factory.openSession(true);

        String statement = "com.hebut.builder.mapper.xml.order"+".selectOrderResultMap";
        Order aaa = sqlSession.selectOne(statement,2);

        System.out.println(aaa);
    }
}
