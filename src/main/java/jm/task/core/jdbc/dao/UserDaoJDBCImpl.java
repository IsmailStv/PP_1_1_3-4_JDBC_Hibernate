package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static Connection conn = null;
    {
        try {
            conn = Util.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserDaoJDBCImpl() {

    }

    // Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
    @Override
    public void createUsersTable() {

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), last_name VARCHAR(255), age TINYINT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
    @Override
    public void dropUsersTable() {

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Добавление User в таблицу
    @Override
    public void saveUser(String name, String last_name, byte age) {

        try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)")) {
            conn.setAutoCommit(false);
            pstm.setString(1, name);
            pstm.setString(2, last_name);
            pstm.setByte(3, age);
            pstm.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Удаление User из таблицы ( по id )
    @Override
    public void removeUserById(long id) {

        try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            conn.setAutoCommit(false);
            pstm.setLong(1, id);
            pstm.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            //e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Получение всех User(ов) из таблицы
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM users")) {
            while(resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("last_name"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Очистка содержания таблицы
    @Override
    public void cleanUsersTable() {

        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            statement.executeUpdate("TRUNCATE TABLE users");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
//            try {
//                conn.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}