#url=jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=utf8
#driver=com.mysql.jdbc.Driver
#username=jxy
#password=123456



# 创建数据库，并创建权限用户
# CREATE DATABASE 'mybatis' CHARACTER SET utf8;
# 创建数据库
CREATE DATABASE mybatis 
CHARACTER SET utf8 
COLLATE utf8_general_ci;

# 创建用户权限
CREATE USER 'jxy'@'%' IDENTIFIED BY '123456';#后面的表示密码ssm
GRANT ALL PRIVILEGES ON mybatis.* TO 'jxy'@'%';

FLUSH PRIVILEGES;
# 创建表格
use mybatis;
CREATE TABLE users(
	id INT PRIMARY KEY AUTO_INCREMENT, 
	NAME VARCHAR(20), 
	age INT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 插入数据
INSERT INTO users(NAME, age) VALUES('Tom', 12);
INSERT INTO users(NAME, age) VALUES('Jack', 11);

#别名测试
CREATE TABLE orders(
	order_id INT PRIMARY KEY AUTO_INCREMENT,
	order_no VARCHAR(20), 
	order_price FLOAT
);
INSERT INTO orders(order_no, order_price) VALUES('aaaa', 23);
INSERT INTO orders(order_no, order_price) VALUES('bbbb', 33);
INSERT INTO orders(order_no, order_price) VALUES('cccc', 22);

#关联查询测试
CREATE TABLE teacher(
	t_id INT PRIMARY KEY AUTO_INCREMENT, 
	t_name VARCHAR(20)
);
CREATE TABLE class(
	c_id INT PRIMARY KEY AUTO_INCREMENT, 
	c_name VARCHAR(20), 
	teacher_id INT
);
ALTER TABLE class ADD CONSTRAINT fk_teacher_id FOREIGN KEY (teacher_id) REFERENCES teacher(t_id);	

INSERT INTO teacher(t_name) VALUES('LS1');
INSERT INTO teacher(t_name) VALUES('LS2');

INSERT INTO class(c_name, teacher_id) VALUES('bj_a', 1);
INSERT INTO class(c_name, teacher_id) VALUES('bj_b', 2);

CREATE TABLE student(
	s_id INT PRIMARY KEY AUTO_INCREMENT, 
	s_name VARCHAR(20), 
	class_id INT
);
INSERT INTO student(s_name, class_id) VALUES('xs_A', 1);
INSERT INTO student(s_name, class_id) VALUES('xs_B', 1);
INSERT INTO student(s_name, class_id) VALUES('xs_C', 1);
INSERT INTO student(s_name, class_id) VALUES('xs_D', 2);
INSERT INTO student(s_name, class_id) VALUES('xs_E', 2);
INSERT INTO student(s_name, class_id) VALUES('xs_F', 2);


#存储过程
create table p_user(  
	id int primary key auto_increment,  
	name varchar(10),
	sex char(2)
); 

insert into p_user(name,sex) values('A',"男");  
insert into p_user(name,sex) values('B',"女");  
insert into p_user(name,sex) values('C',"男");  

#创建存储过程(查询得到男性或女性的数量, 如果传入的是0就女性否则是男性)
DELIMITER $
CREATE PROCEDURE mybatis.ges_user_count(IN sex_id INT, OUT user_count INT)
BEGIN  
IF sex_id=0 THEN
SELECT COUNT(*) FROM mybatis.p_user WHERE p_user.sex='女' INTO user_count;
ELSE
SELECT COUNT(*) FROM mybatis.p_user WHERE p_user.sex='男' INTO user_count;
END IF;
END 
$

#调用存储过程
DELIMITER ;
SET @user_count = 0;
CALL mybatis.ges_user_count(1, @user_count);
SELECT @user_count;


#缓存
CREATE TABLE c_user(
	id INT PRIMARY KEY AUTO_INCREMENT, 
	NAME VARCHAR(20), 
	age INT
);
INSERT INTO c_user(NAME, age) VALUES('Tom', 12);
INSERT INTO c_user(NAME, age) VALUES('Jack', 11);
#Mybatis-Spring
CREATE TABLE s_user(
	user_id INT AUTO_INCREMENT PRIMARY KEY,
	user_name VARCHAR(30),
	user_birthday DATE,
	user_salary DOUBLE
)