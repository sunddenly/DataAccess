package org.hebut.scse.hibernate.inherit;

import org.hebut.scse.hibernate.HibernateUtil;
import org.hebut.scse.hibernate.pojo.inherit.union.Person;
import org.hebut.scse.hibernate.pojo.inherit.union.Student;
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

public class HibernateUnion {
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
    @Test
    public void testUpdate(){
        String hql = "UPDATE Person p SET p.age = 20";
        session.createQuery(hql).executeUpdate();
    }

    /**
     * 优点:
     * 1. 无需使用辨别者列.
     * 2. 子类独有的字段能添加非空约束.
     *
     * 缺点:
     * 1. 存在冗余的字段
     * 2. 若更新父表的字段, 则更新的效率较低
     */

    /**
     * 查询:
     * 1. 查询父类记录, 需把父表和子表记录汇总到一起再做查询. 性能稍差.
     * 2. 对于子类记录, 也只需要查询一张数据表
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
     * 1. 对于子类对象只需把记录插入到一张数据表中.
     */
    @Test
    public void testSave(){
        Person person = new Person(11,"AA");
        Student stu = new Student(22,"BB","ATGUIGU");
        session.save(person);
        session.save(stu);

    }
}