### Hibernate 系列项目
####1、HelloWorld项目  
（1）涉及Hibernate配置方法  
> hibernate.cfg.xml：hibernate主配置文件
> hibernate.reveng.xml：hibernate插件配置文件
> News.hbm.xml：插件生成的映射文件

（2）涉及Hibernate插件使用  
> pom配置插件
> 指定插件的配置文件路径
> 指定插件生成文件路径

（3）涉及向hibernate数据库插入一条记录  
> API 加载主配置文件
> 创建sessionFactory
> 创建session
> session.save(news);

####2、Hibernate 缓存
test/HibernateCache  
####3、Hibernate 核心操作
test/HibernateCoreOpt
####4、Hibernate 配置
-c3p0 ==>1. resources/hibernate.cfg.xml  
-jdbc.fetch_size ==>1. resources/hibernate.cfg.xml  

