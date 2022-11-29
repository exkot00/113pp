package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao   {
    Connection connection = null;

    public UserDaoJDBCImpl() {   }

    public void createUsersTable() throws SQLException {                                    //создаем БД
        try ( Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            String sql = "CREATE TABLE IF NOT EXISTS newtable (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255), " +
                    "lastname VARCHAR(255), " +
                    "age INT, " +
                    "PRIMARY KEY (id))" ;
            statement.execute(sql);
            connection.commit();
            System.out.println("бд создана");
        } catch (SQLException e) {
            System.out.println("Ошибка создания бд");
            e.printStackTrace();
            if (!connection.isClosed()) {
                connection.rollback();
            }

        }
    }

    public void dropUsersTable() throws SQLException {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("DROP TABLE IF EXISTS newtable");
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка удаления таблицы");
            if (!connection.isClosed()) {
                connection.rollback();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO newtable (name, lastname, age) Values (?, ?, ?)")) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Юзер с именем " + name +" добавлен");
        } catch (SQLException e) {
            System.out.println("Ошибка добавления юзера");
            if (!connection.isClosed()) {
                connection.rollback();
            }
        }
    }

    public void removeUserById(long id) throws SQLException {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM newtable WHERE id = ?")) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, (int) id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("удалили юзера с id= " +id);
        } catch (SQLException e){
            System.out.println("ошибка удаления юзера");
            if (!connection.isClosed()) {
                connection.rollback();
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM newtable")) {
            connection.setAutoCommit(false);
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("lastname"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println("ошибка получения юзеров");
            if (!connection.isClosed()) {
                connection.rollback();
            }
        }
        return users;
    }

    public void cleanUsersTable() throws SQLException {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("TRUNCATE TABLE newtable");
            connection.commit();
            System.out.println("таблица очищена");
        } catch (SQLException e){
            System.out.println("ошибка отчистки таблицы");
            if (!connection.isClosed()) {
                connection.rollback();
            }
        }

    }
}
