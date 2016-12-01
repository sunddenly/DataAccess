package org.hebut.scse.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by jxy on 2016/12/1.
 */
public class DBUtil {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    static {
        try {
            Properties props = new Properties();//Properties类详见5.3
            props.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
            driver = props.getProperty("driver");
            url = props.getProperty("url");
            user = props.getProperty("user");
            password = props.getProperty("password");
            Class.forName(driver);//注意这里没有引号！
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    protected static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }//catch块内容可不写，即所谓的“安静”的关闭连接
    }
}
