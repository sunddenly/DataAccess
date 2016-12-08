package org.hebut.scse.hibernate.n21;

import org.hebut.scse.hibernate.HibernateUtil;
import org.hebut.scse.hibernate.pojo.n21.oneway.Customer;
import org.hebut.scse.hibernate.pojo.n21.oneway.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class HibernateOneWayn21 {
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
    public void testMany2OneSave(){
        Customer customer = new Customer("AA");;
        Order order1 = new Order("ORDER-1");
        Order order2 = new Order("ORDER-2");
        //设定关联关系
        order1.setCustomer(customer);
        order2.setCustomer(customer);
        //执行  save 操作: 先插入 Customer, 再插入 Order, 3 条 INSERT
        session.save(customer);
        session.save(order1);
        session.save(order2);

    }
    @Test
    public void testMany2One2Save(){
        Customer customer = new Customer("BB");;
        Order order1 = new Order("ORDER-3");
        Order order2 = new Order("ORDER-4");
        //设定关联关系
        order1.setCustomer(customer);
        order2.setCustomer(customer);
         //先插入 Order, 再插入 Customer. 3 条 INSERT, 2 条 UPDATE
        session.save(order1);
        session.save(order2);
        session.save(customer);
    }
    @Test
    public void testMany2OneGet(){
        // 若查n端的对象, 则默认为懒加载1端对象
        Order order = (Order) session.get(Order.class, 1);
        System.out.println(order.getOrderName());

        // 获取 Order 对象时, 默认情况下, 其关联的 Customer 对象是一个代理对象
        System.out.println(order.getCustomer().getClass().getName());


        // 需要使用到关联的对象时, 才发送对应的 SQL 语句.
//        session.close();
        Customer customer = order.getCustomer();
        System.out.println(customer.getCustomerName());

        //在查询 Customer 对象时, 若此时 session 已被关闭, 则默认情况下会发生 LazyInitializationException 异常
    }

    @Test
    public void testDelete(){
        //在不设定级联关系的情况下, 且 1 这一端的对象有 n 的对象在引用, 不能直接删除 1 这一端的对象
        Customer customer = (Customer) session.get(Customer.class, 1);
        session.delete(customer);
    }

    @Test
    public void testUpdate(){//级联更新，不用设置
        Order order = (Order) session.get(Order.class, 1);
        order.getCustomer().setCustomerName("AAA");
    }

}
