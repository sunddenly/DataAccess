package org.hebut.scse.hibernate.inherit;

import org.hebut.scse.hibernate.HibernateUtil;
import org.hebut.scse.hibernate.pojo.inherit.subclass.Person;
import org.hebut.scse.hibernate.pojo.inherit.subclass.Student;
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

public class HibernateSubclass {
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
     * 缺点:
     * 1. 使用了辨别者列.
     * 2. 子类独有的字段不能添加非空约束.
     * 3. 若继承层次较深, 则数据表的字段也会较多.
     */

    /**
     * 查询:
     * 1. 查询父类记录, 只需要查询一张数据表
     * 2. 对于子类记录, 也只需要查询一张数据表
     */
    @Test
    public void testQuery() {
        List<Person> persons = session.createQuery("FROM Person").list();
        System.out.println(persons);

        List<Student> stus = session.createQuery("FROM Student").list();
        System.out.println(stus);
    }

    /**
     * 插入操作:
     * 1. 对于子类对象只需把记录插入到一张数据表中.
     * 2. 辨别者列有 Hibernate 自动维护.
     */
    @Test
    public void testSave() {
        Person person = new Person("CC");
        Student stu = new Student(22, "DD", "ATGUIGU");
        session.save(person);
        session.save(stu);

    }
}