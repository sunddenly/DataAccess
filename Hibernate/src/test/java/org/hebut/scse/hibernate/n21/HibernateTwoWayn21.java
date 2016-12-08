package org.hebut.scse.hibernate.n21;

import org.hebut.scse.hibernate.HibernateUtil;
import org.hebut.scse.hibernate.pojo.n21.twoway.Customer;
import org.hebut.scse.hibernate.pojo.n21.twoway.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class HibernateTwoWayn21 {
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
    public void testCascade(){
        Customer customer = (Customer) session.get(Customer.class, 3);
        customer.getOrders().clear();
    }

    @Test
    public void testDelete(){
        //在不设定级联关系的情况下, 且 1 这一端的对象有 n 的对象在引用, 不能直接删除 1 这一端的对象
        Customer customer = (Customer) session.get(Customer.class, 1);
        session.delete(customer);
    }

    @Test
    public void testUpdat2(){
        Customer customer = (Customer) session.get(Customer.class, 1);
        customer.getOrders().iterator().next().setOrderName("GGG");
    }

    @Test
    public void testUpdate(){
        Order order = (Order) session.get(Order.class, 1);
        order.getCustomer().setCustomerName("AAA");
    }

    @Test
    public void testOne2ManyGet(){
        //1. 对1端关联集合使用延迟加载
        Customer customer = (Customer) session.get(Customer.class, 3);
        System.out.println(customer.getCustomerName());
        //2. 返回关联集合的 Hibernate 内置的集合类型，该类型具有延迟加载和存放代理对象的功能.
        System.out.println(customer.getOrders().getClass());//PersistentSet

        //session.close();
        //3. 可能会抛出 LazyInitializationException 异常
//        System.out.println(customer.getOrders().size());
        System.out.println(customer.getOrders());
        //4. 再需要使用集合中元素的时候进行初始化.
    }

    @Test
    public void testOne2ManyGet2(){
        //1. 对1端关联集合使用延迟加载
        Order o = (Order) session.get(Order.class, 1);
        System.out.println(o.getOrderName());

        System.out.println(o.getCustomer());
        //4. 再需要使用集合中元素的时候进行初始化.
    }

    @Test
    public void testMany2OneSave(){
        Customer customer = new Customer("AA");
        Order order1 = new Order("ORDER-3");
        Order order2 = new Order("ORDER-4");
        //设定关联关系
        order1.setCustomer(customer);
        order2.setCustomer(customer);

        customer.getOrders().add(order1);
        customer.getOrders().add(order2);

        //执行save 操作: 先插入 Customer, 再插入 Order, 3 条 INSERT, 2 条 UPDATE
        session.save(customer);
        session.save(order1);
        session.save(order2);

    }
    @Test
    public void testMany2OneSave2(){
        Customer customer = new Customer("BB");
        Order order1 = new Order("ORDER-3");
        Order order2 = new Order("ORDER-4");
        //设定关联关系
        order1.setCustomer(customer);
        order2.setCustomer(customer);

        customer.getOrders().add(order1);
        customer.getOrders().add(order2);


        //先插入 Order, 再插入 Cusomer, 3 条 INSERT, 4 条 UPDATE
        session.save(order1);
        session.save(order2);

        session.save(customer);
    }

}
