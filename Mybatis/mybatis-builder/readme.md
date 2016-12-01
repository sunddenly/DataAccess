#### MyabtisBuilder 环境搭建
---

#### 相关步骤
1. 导入相关jar包：mysql，myabtis    
2. 创建数据库、表：/doc/db/initsql  
3. 添加Mybatis配置文件：/resources/config/mybatis/mybatis-config.xml  
4. 添加users表对应的pojo类：User，必须提供默认无参构造函数，否则会报错  
5. 添加pojo类映射配置文件：/com/hebut/builder/mapper/userMapper.xml  
6. 在mybatis配置文件注册pojo类的映射文件：/resources/config/mybatis/mybatis-config.xml  
7. dao中待用sql语句  

#### users表的CRUD
**1. XML 实现**  
　1.1 定义sql映射xml文件：/com/hebut/builder/mapper/userMapper.xml  
　1.2 在mybatis配置文件注册：/resources/config/mybatis/mybatis-config.xml  
　1.3 编写dao层，调用sql语句：  
**2. annotation实现**  
　2.1  定义sql映射的接口  
　2.2  在config中注册这个映射接口  
　2.3  在dao类中调用  

#### Mybatis 优化
**1.连接数据库的配置**  
　1.1 单独放在一个properties文件中   
　1.2 在mybatis配置文件中引用properties文件，必须放在第一行  
**2.定义实体类别名**  
　2.1 作用：简化sql映射xml文件中的引用  
　2.2 使用：编写别名bean类，完成bean在mybatis中的映射，typeAliases/package  
**3. 加入log4j的配置文件:**打印sql语句  
　3.1 定义log4j.proertities/log4j.xml文件：resources/log4j.proertities  
　3.2 定义log4j.xml文件时还需导入dtd：resources/log4j.dtd    

#### Mybatis 解决数据库字段名与实体类属性名冲突
**1. 准备Order表和数据**  
**2. 定义Order实体类**  
**3. 定义Order表映射：**/com/hebut/builder/mapper/xml/orderMapper.xml  
　3.1 通过sql语句指定相应的别名  
　3.2 通过resultMap指定  

#### Mybatis关联表查询
**1. 一对一关联[association]：**根据班级id查询班级信息(带老师的信息)  
　1.1 创建表和数据：teacher/class  
　1.2 定义实体类：Classes,Teacher  
　1.3 定义sql映射文件：ClassMapper.xml  
　1.4 进行dao层的测试  
**2. 一对多关联[collection]：**根据classId查询对应的班级信息,包括学生,老师  
　2.1 创建表和数据：student  
　2.2 定义实体类：Student  
　2.3 定义sql映射文件：ClassMapper.xml  
　2.4 进行dao层的测试  
**3. 关联方式**  
　3.1 嵌套结果：使用`嵌套结果映射`来处理`重复的联合结果的子集`,封装联表查询的数据,去除重复的数据  
　3.2 嵌套查询：通过执行另外一个SQL映射语句,来返回预期的复杂类型    

#### Mybatis 模糊查询
**1. 提出需求：** 实现`多条件查询`用户：姓名模糊匹配, 年龄在指定的最小值到最大值之间  
**2. 准备数据表和数据:**d_user  
**3. 查询条件封装：ConditionUser**  
**4. 定义表实体类：** User  
**5. 定义User表映射：**/com/hebut/builder/mapper/xml/conditionUserMapper.xml  
**6. MyBatis中可用的动态SQL标签：**     
``` java
if
choose(when,otherwise)
trim(where,set)
foreach()
```

#### Mybatis 调用存储过程
**1. 提出需求：**查询得到男性或女性的数量, 如果传入的是0就女性否则是男性  
**2. 准备数据库表和存储过程：**p_user  
**3. 定义表实体类：** PUser  
**4. 定义PUser表映射：**/com/hebut/builder/mapper/xml/puserMapper.xml  

#### Mybatis 缓存
**0. 概述：**正如大多数持久层框架一样，MyBatis 同样提供了一级缓存和二级缓存的支持  
**1. 一级缓存**  
基于PerpetualCache 的 HashMap本地缓存，其存储作用域为 Session，当 Session flush 或 close 之后，该Session中的所有 Cache 就将清空。  
**2. 二级缓存**  
二级缓存与一级缓存其机制相同，默认也是采用 PerpetualCache，HashMap存储，不同在于其存储作用域为 Mapper(Namespace)，并且可自定义存储源，如 Ehcache。  
**3. 缓存机制**
对于缓存数据更新机制，当某一个作用域(一级缓存Session/二级缓存Namespaces)的进行了 C/U/D 操作后，默认该作用域下所有 select 中的缓存将被clear。  
　3.1 映射语句文件中的所有select语句将会被缓存。  
　3.2 映射语句文件中的所有insert，update和delete语句会刷新缓存。   
　3.3 缓存会使用Least Recently Used（LRU，最近最少使用的）算法来收回。   
　3.4 缓存会根据指定的时间间隔来刷新。   
　3.5 缓存会存储1024个对象  
``` xml
<cache eviction="FIFO"  //回收策略为先进先出
       flushInterval="60000" //自动刷新时间60s
       size="512" //最多缓存512个引用对象
       readOnly="true"/> //只读
```

#### Mybatis 一级缓存
**0. 作用：**以session为单元，默认开启一级缓存  
**1.提出需求：**根据id查询对应的用户记录对象  
**2.准备数据库表和数据：**c_user  
**3.创建表的实体类：**User  
**4.定义CUser表映射：**/com/hebut/builder/mapper/xml/cuserMapper.xml  
**5. 清空一级缓存：**
　5.1 execute session.clearCache()  
　5.2 execute C/R/U/D  
　5.3 execute session.close()  
　5.4 查询条件分发生改变

#### Mybatis 二级缓存
**0. 作用：**以映射文件namespace为单元  
**1. 使用：**添加一个<cache>在cuserMapper.xml中











