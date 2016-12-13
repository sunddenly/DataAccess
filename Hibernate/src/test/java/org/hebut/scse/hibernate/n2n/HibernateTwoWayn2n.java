package org.hebut.scse.hibernate.n2n;

import org.hebut.scse.hibernate.HibernateUtil;
import org.hebut.scse.hibernate.pojo.n2n.twoway.Category;
import org.hebut.scse.hibernate.pojo.n2n.twoway.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;


public class HibernateTwoWayn2n {
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
    public void testGet(){
        Category category = (Category) session.get(Category.class, 2);
        System.out.println(category.getName());

        //需要连接中间表
        Set<Item> items = category.getItems();
        System.out.println(items);

        System.out.println(items.iterator().next().getCategories());
    }

    @Test
    public void testSave(){
        Category category1 = new Category("C-AA");
        Category category2 = new Category("C-BB");

        Item item1 = new Item("I-AA");
        Item item2 = new Item("I-BB");


        //设定关联关系
        category1.getItems().add(item1);
        category1.getItems().add(item2);

        category2.getItems().add(item1);
        category2.getItems().add(item2);



        //执行保存操作
        session.save(category1);
        session.save(category2);

        session.save(item1);
        session.save(item2);
    }
}
