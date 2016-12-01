package com.hebut.builder;

import com.hebut.builder.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jxy on 2016/6/26.
 */

public class CacheTest {
    BuilderTest bt = new BuilderTest();

    /**
     * 测试一级缓存
     * @throws IOException
     */
    @Test
    public void FirstCache() throws IOException {
        SqlSessionFactory factory = bt.getFactory();
        SqlSession session = factory.openSession();
        String statement = "com.hebut.builder.mapper.xml.cuser" + ".getUser";
        User user = session.selectOne(statement, 1);
        System.out.println(user);

        /**
         * 一级缓存默认就会被使用
         */
        user = session.selectOne(statement, 1);//直接输出不会再次查询
        System.out.println(user);

        /**
         * 1. 必须是同一个Session,如果session对象已经close()过了就不可能用了
         */

        session = factory.openSession();
        user = session.selectOne(statement, 1);//不同的session，会执行sql语句
        System.out.println(user);

        /**
         * 2. 查询条件是一样的
         */
        user = session.selectOne(statement, 2);//查询条件发生改变，会执行sql语句
        System.out.println(user);

        /**
         * 3. 没有执行过session.clearCache()清理缓存
         */

        session.clearCache();
        user = session.selectOne(statement, 2);//执行过session.clearCache(),会执行sql语句
        System.out.println(user);

        /**
         * 4. 没有执行过增删改的操作(这些操作都会清理缓存)
         */

        session.update("com.atguigu.mybatis.test8.userMapper.updateUser",
                new User(2, "user", 23));
        user = session.selectOne(statement, 2);
        session.close();

    }

    /*
     * 测试二级缓存
     */
    @Test
    public void testCache2() throws IOException {
        SqlSessionFactory factory = bt.getFactory();
        SqlSession session1 = factory.openSession();
        SqlSession session2 = factory.openSession();

        String statement = "com.hebut.builder.mapper.xml.cuser" + ".getUser";


        User user = session1.selectOne(statement, 1);
        session1.commit();
        System.out.println("user="+user);

        user = session2.selectOne(statement, 1);
        session2.commit();
        System.out.println("user2="+user);
    }

}
