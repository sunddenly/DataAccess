package org.hebut.scse.hibernate.inherit;

import org.hebut.scse.hibernate.HibernateUtil;
import org.hebut.scse.hibernate.pojo.inherit.join.Person;
import org.hebut.scse.hibernate.pojo.inherit.join.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class HibernateJoin {
    private SessionFactory factory;
    private Session session;
    private Transaction transaction;

    @Before
    public void init(){
        Configuration conf = HibernateUtil.conf;
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(conf.getProperties()).buildServiceRegistry();
        factory = conf.buildSessionFactory(serviceRegistry);
        session = factory.openSession();
        transaction = session.beginTransaction();
    }

    @After
    public void destroy(){
        transaction.commit();
        session.close();
        factory.close();
    }


    /**
     * 优点:
     * 1. 不需要使用了辨别者列.
     * 2. 子类独有的字段能添加非空约束.
     * 3. 没有冗余的字段.
     */

    /**
     * 查询:
     * 1. 查询父类记录, 做一个左外连接查询
     * 2. 对于子类记录, 做一个内连接查询.
     */
    @Test
    public void testQuery(){
        List<Person> persons = session.createQuery("FROM Person").list();
        System.out.println(persons);

        List<Student> stus = session.createQuery("FROM Student").list();
        System.out.println(stus);
    }

    /**
     * 插入操作:
     * 1. 对于子类对象至少需要插入到两张数据表中.
     */
    @Test
    public void testSave(){

        Person person = new Person(11,"AA");
        Student stu = new Student(22,"BB","ATGUIGU");

        session.save(person);
        session.save(stu);

    }
}