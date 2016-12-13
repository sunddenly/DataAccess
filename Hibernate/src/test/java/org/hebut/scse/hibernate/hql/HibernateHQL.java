package org.hebut.scse.hibernate.hql;

import org.hebut.scse.hibernate.HibernateUtil;
import org.hebut.scse.hibernate.pojo.hql.Department;
import org.hebut.scse.hibernate.pojo.hql.Employee;
import org.hebut.scse.hibernate.pojo.inherit.join.Person;
import org.hebut.scse.hibernate.pojo.inherit.join.Student;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class HibernateHQL {
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
    public void testLeftJoin(){
        String hql = "SELECT DISTINCT d FROM Department d LEFT JOIN d.emps";
        Query query = session.createQuery(hql);

        List<Department> depts = query.list();
        System.out.println(depts.size());

        for(Department dept: depts){
            System.out.println(dept.getName() + ", " + dept.getEmps().size());
        }

    //		List<Object []> result = query.list();
    //		result = new ArrayList<>(new LinkedHashSet<>(result));
    //		System.out.println(result);
    //
    //		for(Object [] objs: result){
    //			System.out.println(Arrays.asList(objs));
    //		}
    }
    @Test
    public void testLeftJoinFetch(){
    //		String hql = "SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.emps";
        String hql = "FROM Department d LEFT JOIN FETCH d.emps";
        Query query = session.createQuery(hql);

        List<Department> depts = query.list();
        depts = new ArrayList<>(new LinkedHashSet(depts));
        System.out.println(depts.size());

        for(Department dept: depts){
            System.out.println(dept.getName() + "-" + dept.getEmps().size());
        }
    }
    @Test
    public void testGroupBy(){
        String hql = "SELECT min(e.salary), max(e.salary) "
                + "FROM Employee e "
                + "GROUP BY e.dept "
                + "HAVING min(salary) > :minSal";

        Query query = session.createQuery(hql)
                .setFloat("minSal", 1000);

        List<Object []> result = query.list();
        for(Object [] objs: result){
            System.out.println(Arrays.asList(objs));
        }
    }
    @Test
    public void testFieldQuery2(){
        String hql = "SELECT new Employee(e.email, e.salary, e.dept) "
                + "FROM Employee e "
                + "WHERE e.dept = :dept";
        Query query = session.createQuery(hql);

        Department dept = new Department();
        dept.setId(1);
        List<Employee> result = query.setEntity("dept", dept)
                .list();

        for(Employee emp: result){
            System.out.println(emp.getId() + ", " + emp.getEmail()
                    + ", " + emp.getSalary() + ", " + emp.getDept());
        }
    }
    @Test
    public void testFieldQuery(){
        String hql = "SELECT e.email, e.salary, e.dept FROM Employee e WHERE e.dept = :dept";
        Query query = session.createQuery(hql);

        Department dept = new Department();
        dept.setId(1);
        List<Object[]> result = query.setEntity("dept", dept)
                .list();

        for(Object [] objs: result){
            System.out.println(Arrays.asList(objs));
        }
    }
    @Test
    public void testNamedQuery(){
        Query query = session.getNamedQuery("salaryEmps");

        List<Employee> emps = query.setFloat("minSal", 5000)
                .setFloat("maxSal", 10000)
                .list();

        System.out.println(emps.size());
    }
    @Test
    public void testPageQuery(){
        String hql = "FROM Employee";
        Query query = session.createQuery(hql);

        int pageNo = 1;
        int pageSize = 5;

        List<Employee> emps =
                query.setFirstResult((pageNo - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .list();
        System.out.println(emps);
    }
    @Test
    public void testHQLNamedParameter(){

        //1. 创建 Query 对象
        //基于命名参数.
        String hql = "FROM Employee e WHERE e.salary > :sal AND e.email LIKE :email";
        Query query = session.createQuery(hql);

        //2. 绑定参数
        query.setFloat("sal", 5000)
                .setString("email", "%u%");

        //3. 执行查询
        List<Employee> emps = query.list();
        System.out.println(emps.size());
    }

    @Test
    public void testHQL(){

        //1. 创建 Query 对象
        //基于位置的参数.
        String hql = "FROM Employee e WHERE e.salary > ? AND e.email LIKE ? AND e.dept = ? "
                + "ORDER BY e.salary";
        Query query = session.createQuery(hql);

        //2. 绑定参数
        //Query 对象调用 setXxx 方法支持方法链的编程风格.
        Department dept = new Department();
        dept.setId(1);
        query.setFloat(0, 6000)
                .setString(1, "%n%")
                .setEntity(2, dept);

        //3. 执行查询
        List<Employee> emps = query.list();
        System.out.println(emps.size());
    }
}