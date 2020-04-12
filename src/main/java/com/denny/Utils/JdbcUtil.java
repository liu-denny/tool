package com.denny.Utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Description
 * @auther denny
 * @create 2020-03-03 17:25
 */
public class JdbcUtil{

     private String driver;
     private String url;
     private String user;
     private String password;

    public JdbcUtil(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    //返回数据库连接
     public Connection getConnection(){
        try{
            //注册数据库的驱动
            Class.forName(driver);
            //获取数据库连接（里面内容依次是：主机名和端口、用户名、密码）
            Connection connection = DriverManager.getConnection(url,user,password);
            //返回数据库连接
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
     }
}
