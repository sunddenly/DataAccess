package com.hebut.builder;

import com.hebut.builder.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import sun.misc.Launcher;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

/**
 * Created by jxy on 2016/6/25.
 */
public class BuilderTest {

    public SqlSessionFactory getFactory() throws IOException {
        //加载mybatis的配置文件（它也加载关联的映射文件）
        String resource = "config/mybatis/mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        //方式二
        InputStream is = BuilderTest.class.getClassLoader().getResourceAsStream(resource);


        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sessionFactory;
    }

    @Test
    public void mybatisbuilder() throws IOException {

        SqlSessionFactory sessionFactory=getFactory();
        //创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession();
        //映射sql的标识字符串:nameSpace+mapperID
        String statement = "com.hebut.builder.mapper.xml.user"+".getUser";
        //执行查询返回一个唯一user对象的sql
        User user = session.selectOne(statement, 1);
        System.out.println(user);
        session.close();
    }
    @Test
    public  void TestAdd() throws IOException {
        SqlSessionFactory sessionFactory=getFactory();
        //创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession();
        //映射sql的标识字符串:nameSpace+mapperID
        String statement = "com.hebut.builder.mapper.xml.user"+".insertUser";
        int i=session.insert(statement,new User(-1,"kk",23));
        session.commit();
        System.out.println(i);
        session.close();
    }

    @Test
    public  void TestUpdate() throws IOException {
        SqlSessionFactory sessionFactory=getFactory();
        //创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession(true);
        //映射sql的标识字符串:nameSpace+mapperID
        String statement = "com.hebut.builder.mapper.xml.user"+".updateUser";
        int i=session.update(statement,new User(4,"kk444",23));
        System.out.println(i);
        session.close();
    }
    @Test
    public  void TestDelete() throws IOException {
        SqlSessionFactory sessionFactory=getFactory();
        //创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession(true);
        //映射sql的标识字符串:nameSpace+mapperID
        String statement = "com.hebut.builder.mapper.xml.user"+".deleteUser";
        int i=session.update(statement,4);
        System.out.println(i);
        session.close();
    }

    @Test
    public  void TestSelectAll() throws IOException {
        SqlSessionFactory sessionFactory=getFactory();
        //创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession(true);
        //映射sql的标识字符串:nameSpae+mapperID
        String statement = "com.hebut.builder.mapper.xml.user"+".getAllUsers";
        List<User> list = session.selectList(statement);
       session.close();
        System.out.println(list);
    }
}
