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
涉及hibernate.cfg.xml文件  

- c3p0              
- jdbc.fetch_size  
- jdbc.batch_size
- show_sq 
- format_sql
- hbm2ddl.auto

####5、Hibernate 映射配置
涉及helloworld/news.hbm.xml和test/HibernateCoreOpt文件     

- hibernate-mapping package  
- class  dynamic-insert/select-before-update
- id  OID/generator/generator
- type 映射 时间/大对象

####6、Hibernate 组成关系映射
涉及 pojo包中的Worker和Pay
