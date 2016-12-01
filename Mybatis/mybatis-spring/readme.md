#### Spring整合Mybatis
---

>**整合步骤：**
>>1. 添加Jar包  
>>2. 数据库表  
>>3. 实体类: User  
>>4. DAO接口: UserMapper (XXXMapper)  
>>5. SQL映射文件: userMapper.xml(与接口忽略大小写同名)  
>>6. spring的配置文件: beans.xml  
>>7. mybatis的配置文件: mybatis-config.xml  

---

#### Step1：添加jar包
> **mybatis**  
>> mybatis-3.2.0.jar  
>> mybatis-spring-1.1.1.jar  
>> log4j-1.2.17.jar  

> **spring**
>> spring-aop-3.2.0.RELEASE.jar  
>> spring-beans-3.2.0.RELEASE.jar  
>> spring-context-3.2.0.RELEASE.jar  
>> spring-core-3.2.0.RELEASE.jar  
>> spring-expression-3.2.0.RELEASE.jar  
>> spring-jdbc-3.2.0.RELEASE.jar  
>> spring-test-3.2.4.RELEASE.jar  
>> spring-tx-3.2.0.RELEASE.jar  
>> aopalliance-1.0.jar  
>> cglib-nodep-2.2.3.jar  
>> commons-logging-1.1.1.jar  

> **MYSQL驱动包**
>> mysql-connector-java-5.0.4-bin.jar  

---

#### Step2：数据库表
``` sql
CREATE TABLE s_user(
	user_id INT AUTO_INCREMENT PRIMARY KEY,
	user_name VARCHAR(30),
	user_birthday DATE,
	user_salary DOUBLE
)
```

#### Step3：实体类
``` java
public class User {
	private int id;
	private String name;
	private Date birthday;
	private double salary;
    //set,get方法
}
```

#### Step4：DAO UserMapper
``` java
public interface UserMapper {
	void save(User user);
	void update(User user);
	void delete(int id);
	User findById(int id);
	List<User> findAll();
}
```

###Step5：SQL映射文件 userMapper.xml
与接口忽略大小写同名
``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 
    namespace:与接口全类名一致
    id:必须与接口的某个对应方法一致
-->
<mapper namespace="com.hebut.sm.mapper.ano.UserMapper">
    <resultMap type="User" id="userResult">
        <result column="user_id" property="id"/>
        <result column="user_name" property="name"/>
        <result column="user_birthday" property="birthday"/>
        <result column="user_salary" property="salary"/>
    </resultMap>

    <!-- 取得插入数据后的id -->
    <insert id="save" keyColumn="user_id" keyProperty="id" useGeneratedKeys="true">
        insert into s_user(user_name,user_birthday,user_salary)
        values(#{name},#{birthday},#{salary})
    </insert>

    <update id="update">
        update s_user
        set user_name = #{name},
        user_birthday = #{birthday},
        user_salary = #{salary}
        where user_id = #{id}
    </update>

    <delete id="delete">
        delete from s_user
        where user_id = #{id}
    </delete>

    <select id="findById" resultMap="userResult">
        select *
        from s_user
        where user_id = #{id}
    </select>

    <select id="findAll" resultMap="userResult">
        select *
        from s_user
    </select>

</mapper>
```

#### Step6：Spring的配置文件: beans.xml
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
		http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-4.2.xsd
		http://www.springframework.org/schema/tx
		http://www.springframework.org/schema/tx/spring-tx-4.2.xsd">
    <!-- 1. 数据源 : DriverManagerDataSource -->
    <bean id="dataSource"
          class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/mybatis"/>
        <property name="username" value="jxy"/>
        <property name="password" value="123456"/>
    </bean>
    <!--
        2. mybatis的SqlSession的工厂: SqlSessionFactoryBean
            dataSource：引用数据源
            typeAliasesPackage：指定实体类的包名，自动将实体的的简单类名映射成别名
    -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!-- 自动扫描 mapper.xml文件(读取的是编译出来的classes目录下的module目录下的具体模块的mapping目录下的任意xml文件) -->
        <property name="mapperLocations" value="classpath:com/hebut/sm/mapper/xml/*.xml"></property>
        <!--最新mybatis的配置文件位置-->
        <property name="configLocation" value="classpath:config/mybatis/mybatis-config.xml"></property>
        <property name="typeAliasesPackage" value="com.hebut.sm.pojo"/>
    </bean>
    <!--
        3. mybatis自动扫描加载Sql映射文件 : MapperScannerConfigurer
            sqlSessionFactory：引用上面定义的sqlSessionFactory
            basePackage：指定Sql映射接口所在的包
    -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.hebut.sm.mapper.ano"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    </bean>

    <!-- 4. 事务管理 : DataSourceTransactionManager -->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- 5. 使用声明式事务 -->
    <tx:annotation-driven transaction-manager="txManager" />
</beans>
```

#### Step7:mybatis的配置文件: mybatis-config.xml
``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
	<!-- Spring整合myBatis后，这个配置文件基本可以不要了-->
	<!-- 设置外部配置文件 -->
	<!-- 设置类别名 -->
	<!-- 设置数据库连接环境 -->
	<!-- 映射文件 -->	
</configuration>
```
