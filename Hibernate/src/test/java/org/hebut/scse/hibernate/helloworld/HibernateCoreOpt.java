package org.hebut.scse.hibernate.helloworld;
import javafx.concurrent.Worker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;


public class HibernateCoreOpt {
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


//    @Test
//    public void testComponent(){
//        Worker worker = new Worker();
//        Pay pay = new Pay();
//
//        pay.setMonthlyPay(1000);
//        pay.setYearPay(80000);
//        pay.setVocationWithPay(5);
//
//        worker.setName("ABCD");
//        worker.setPay(pay);
//
//        session.save(worker);
//    }

    @Test
    public void testBlob() throws Exception{
//		News news = new News();
//		news.setAuthor("cc");
//		news.setContent("CONTENT");
//		news.setDate(new Date());
//		news.setDesc("DESC");
//		news.setTitle("CC");
//
//		InputStream stream = new FileInputStream("Hydrangeas.jpg");
//		Blob image = Hibernate.getLobCreator(session)
//				              .createBlob(stream, stream.available());
//		news.setImage(image);
//
//		session.save(news);

//        News news = (News) session.get(News.class, 1);
//        Blob image = news.getImage();
//
//        InputStream in = image.getBinaryStream();
//        System.out.println(in.available());
    }

    @Test
    public void testPropertyUpdate(){
        News news = (News) session.get(News.class, 1);
        news.setTitle("aaaa");

        System.out.println(news.getDes());
        System.out.println(news.getDate().getClass());
    }

    @Test
    public void testIdGenerator() throws InterruptedException{
        News news = new News("AA", "aa", new java.sql.Date(new Date().getTime()));
        session.save(news);

//		Thread.sleep(5000);
    }

    @Test
    public void testDynamicUpdate(){
        News news = (News) session.get(News.class, 1);
        news.setAuthor("AABCD");

    }

//    @Test
//    public void testDoWork(){
//        session.doWork(new Work() {
//
//            @Override
//            public void execute(Connection connection) throws SQLException {
//                System.out.println(connection);
//
//                //调用存储过程.
//            }
//        });
//    }

    /**
     * evict: 从 session 缓存中把指定的持久化对象移除
     */
    @Test
    public void testEvict(){
        News news1 = (News) session.get(News.class, 1);
        News news2 = (News) session.get(News.class, 2);

        news1.setTitle("AA");
        news2.setTitle("BB");

        session.evict(news1);
    }

    /**
     * delete: 执行删除操作. 只要 OID 和数据表中一条记录对应, 就会准备执行 delete 操作
     * 若 OID 在数据表中没有对应的记录, 则抛出异常
     *
     * 可以通过设置 hibernate 配置文件 hibernate.use_identifier_rollback 为 true,
     * 使删除对象后, 把其 OID 置为  null
     */
    @Test
    public void testDelete(){
//		News news = new News();
//		news.setId(11);

        News news = (News) session.get(News.class, 163840);
        session.delete(news);

        System.out.println(news);
    }

    /**
     * 注意:
     * 1. 若 OID 不为 null, 但数据表中还没有和其对应的记录. 会抛出一个异常.
     * 2. 了解: OID 值等于 id 的 unsaved-value 属性值的对象, 也被认为是一个游离对象
     */
    @Test
    public void testSaveOrUpdate(){
        News news = new News("FFF", "fff", new Date());
        news.setId(11);

        session.saveOrUpdate(news);
    }

    /**
     * update:
     * 1. 若更新一个持久化对象, 不需要显示的调用 update 方法. 因为在调用 Transaction
     * 的 commit() 方法时, 会先执行 session 的 flush 方法.
     * 2. 更新一个游离对象, 需要显式的调用 session 的 update 方法. 可以把一个游离对象
     * 变为持久化对象
     *
     * 需要注意的:
     * 1. 无论要更新的游离对象和数据表的记录是否一致, 都会发送 UPDATE 语句.
     *    如何能让 updat 方法不再盲目的出发 update 语句呢 ? 在 .hbm.xml 文件的 class 节点设置
     *    select-before-update=true (默认为 false). 但通常不需要设置该属性.
     *
     * 2. 若数据表中没有对应的记录, 但还调用了 update 方法, 会抛出异常
     *
     * 3. 当 update() 方法关联一个游离对象时,
     * 如果在 Session 的缓存中已经存在相同 OID 的持久化对象, 会抛出异常. 因为在 Session 缓存中
     * 不能有两个 OID 相同的对象!
     *
     */
    @Test
    public void testUpdate(){
        News news = (News) session.get(News.class, 1);

        transaction.commit();
        session.close();

//		news.setId(100);

        session = factory.openSession();
        transaction = session.beginTransaction();

//		news.setAuthor("SUN");

        News news2 = (News) session.get(News.class, 1);
        session.update(news);
    }

    /**
     * get VS load:
     *
     * 1. 执行 get 方法: 会立即加载对象.
     *    执行 load 方法, 若不适用该对象, 则不会立即执行查询操作, 而返回一个代理对象
     *
     *    get 是 立即检索, load 是延迟检索.
     *
     * 2. load 方法可能会抛出 LazyInitializationException 异常: 在需要初始化
     * 代理对象之前已经关闭了 Session
     *
     * 3. 若数据表中没有对应的记录, Session 也没有被关闭.
     *    get 返回 null
     *    load 若不使用该对象的任何属性, 没问题; 若需要初始化了, 抛出异常.
     */
    @Test
    public void testLoad(){

        News news = (News) session.load(News.class, 10);
        System.out.println(news.getClass().getName());

//		session.close();
//		System.out.println(news);
    }

    @Test
    public void testGet(){
        News news = (News) session.get(News.class, 1);
//		session.close();
        System.out.println(news);
    }

    /**
     * persist(): 也会执行 INSERT 操作
     *
     * 和 save() 的区别 :
     * 在调用 persist 方法之前, 若对象已经有 id 了, 则不会执行 INSERT, 而抛出异常
     */
    @Test
    public void testPersist(){
        News news = new News();
        news.setTitle("EE");
        news.setAuthor("ee");
        news.setDate(new Date());
        news.setId(200);

        session.persist(news);
    }

    /**
     * 1. save() 方法
     * 1). 使一个临时对象变为持久化对象
     * 2). 为对象分配 ID.
     * 3). 在 flush 缓存时会发送一条 INSERT 语句.
     * 4). 在 save 方法之前的 id 是无效的
     * 5). 持久化对象的 ID 是不能被修改的!
     */
    @Test
    public void testSave(){
        News news = new News();
        news.setTitle("CC");
        news.setAuthor("cc");
        news.setDate(new Date());
        news.setId(100);

        System.out.println(news);//ID为null
        session.save(news);
        System.out.println(news);//ID已生成
        //news.setId(101);//由于ID生成方式非手动，所以该操作无效
    }


}