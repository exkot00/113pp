package jm.task.core.jdbc.util;



import java.sql.*;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String URL = "jdbc:mysql://localhost:3306/newdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "23782750053";

    private static Connection connection = null;

    private Util() { }

    public static Connection getConnection() {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            System.out.println("получили драйвер");
        } catch (SQLException e) {
            System.out.println("ошибка в блоке с драйвером");
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Подключились к бд в UTIL");
        } catch (SQLException e) {
            System.out.println("Не смогли подключиться к бд  в UTIL");
            e.printStackTrace();
        }
        return connection;
    }

}
