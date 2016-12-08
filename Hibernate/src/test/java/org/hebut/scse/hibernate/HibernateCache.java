package org.hebut.scse.hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;


public class HibernateCache {
    private SessionFactory factory;
    private Session session;
    private Transaction transaction;

    @Before
    public void init(){
        Configuration conf = new Configuration().configure("/config/hibernate.cfg.xml");
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
    public void save() {
        News news = new News("Java2", "ATGUIGU", new Date(new java.util.Date().getTime()));
        session.save(news);
    }

    @Test
    public void sessionCache(){
        News news = (News)session.get(News.class, 1);
        System.out.println(news);

        News news2 = (News) session.get(News.class, 1);
        System.out.println(news2);
    }
    /**
     * flush: 保持数据表中的记录和 Session 缓存中的对象的状态保持一致. 为了保持一致, 则可能会发送对应的 SQL 语句.
     * 1. 在 Transaction 的 commit() 方法中: 先调用 session 的 flush 方法, 再提交事务
     * 2. flush() 方法会可能会发送 SQL 语句, 但不会提交事务.
     * 3. 注意: 在未提交事务或显式的调用 session.flush() 方法之前, 也有可能会进行 flush() 操作.
     *    1). 执行 HQL 或 QBC 查询, 会先进行 flush() 操作, 以得到数据表的最新的记录
     *    2). 若记录的 ID 是由底层数据库使用自增的方式生成的, 则在调用 save() 方法时, 就会立即发送 INSERT 语句.
     * 因为 save 方法后, 必须保证对象的 ID 是存在的，只有插完后才会知道，对象的OID是多少
     */
    @Test
    public void sessionFlushTest(){
        News news = (News)session.get(News.class, 1);
        news.setAuthor("123123123");//commit前默认调用flush，commit中也会对此调用flush
        session.flush();//即使flush了，数据库也看不到变化

    }

    @Test
    public void sessionFlushQBC(){
        News news = (News)session.get(News.class, 1);
        news.setAuthor("123123123");
        News news2 = (News) session.createCriteria(News.class).uniqueResult();//QBC操作
        System.out.println(news2);
    }

    @Test
    public void sessionFlushSave(){
        News news = new News("Java", "SUN", new Date());
        session.save(news);//native生成OID时，会立即flush发送sql语句，可设点断电调试
    }
    /**
     * refresh(): 会强制发送 SELECT 语句, 以使 Session 缓存中对象的状态和数据表中对应的记录保持一致!
     */
    @Test
    public void testRefresh(){
        News news = (News) session.get(News.class, 1);
        System.out.println(news);

        //需要设置隔离级别才能看到效果，通过断点调试
        session.refresh(news);//若数据库记录通过其他途径得以修改，该操作会刷新缓存
        System.out.println(news);
    }
    /**
     * clear(): 清理缓存
     */
    @Test
    public void testClear(){
        News news1 = (News) session.get(News.class, 1);

        session.clear();

        News news2 = (News) session.get(News.class, 1);
    }
}
