package com.hebut.builder;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jxy on 2016/6/26.
 */

public class ProcedureTest {
    BuilderTest bt = new BuilderTest();
    @Test
    public void conditionQuery() throws IOException {
        SqlSessionFactory factory = bt.getFactory();
        SqlSession sqlSession = factory.openSession(true);
        String statement = "com.hebut.builder.mapper.xml.puser"+".getCount";
        Map<String, Integer> paramMap = new HashMap<>();
        paramMap.put("sex_id", 1);
        paramMap.put("user_count",-1);
        sqlSession.selectOne(statement, paramMap);
        Integer userCount = paramMap.get("user_count");
        System.out.println(userCount);

    }


}
