package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao   {


    private  static final Connection connection = Util.getConnection(); //подключаемся к дб




    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {                                    //создаем БД
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS newtable (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255), " +
                    "lastname VARCHAR(255), " +
                    "age INT, " +
                    "PRIMARY KEY (id))" ;
            statement.execute(sql);
            System.out.println("бд создана");
        } catch (SQLException e) {
            System.out.println("Ошибка создания бд");
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS newtable");
        } catch (SQLException e) {
            System.out.println("Ошибка удаления таблицы");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO newtable (name, lastname, age) Values (?, ?, ?)")) {


            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            System.out.println("Юзер с именем " + name +" добавлен");
        } catch (SQLException e) {
            System.out.println("Ошибка добавления юзера");
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM newtable WHERE id = ?")) {
            preparedStatement.setInt(1, (int) id);
            preparedStatement.executeUpdate();

            System.out.println("удалили юзера с id= " +id);
        } catch (SQLException e){
            System.out.println("ошибка удаления юзера");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM newtable")) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("lastname"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);

            }
        } catch (SQLException e) {
            System.out.println("ошибка получения юзеров");
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE newtable");

            System.out.println("таблица очищена");
        } catch (SQLException e){
            System.out.println("ошибка отчистки таблицы");
        }

    }
}
