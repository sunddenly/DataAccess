package org.hebut.scse.hibernate.helloworld;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;

import java.sql.Date;

public class HelloWord {
    /**
     * 获取Session
     * 1. 创建 Configuration 对象:
     *         对应 hibernate 的基本配置信息和 对象关系映射信息
     * 2. 创建一个 ServiceRegistry 对象:
     *          hibernate 4.x 新添加的对象,hibernate 的任何配置和服务都需要在该对象中注册后才能有效.
     * 3. 创建SessionFactory对象：conf.buildSessionFactory(serviceRegistry)
     * 4. 创建Session对象：factory.openSession()
     */
    //4.0 之前这样创建
    //sessionFactory = conf.buildSessionFactory();
    public static SessionFactory getFactory(){
        Configuration conf = new Configuration().configure("/config/hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(conf.getProperties()).buildServiceRegistry();
        SessionFactory factory = conf.buildSessionFactory(serviceRegistry);

        return factory;
    }
    @Test
    public void test() {
        SessionFactory factory = getFactory();
        Session session = factory.openSession();

        Transaction transaction = session.beginTransaction();

        News news = new News("Java2", "ATGUIGU", new Date(new java.util.Date().getTime()));
        session.save(news);

        transaction.commit();

        session.close();
        factory.close();
    }
	
}
