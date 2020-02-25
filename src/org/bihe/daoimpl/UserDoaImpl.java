package org.bihe.daoimpl;

import org.bihe.interfaces.UserDAO;
import org.bihe.models.User;
import org.bihe.sevices.DBConnection;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDoaImpl implements UserDAO {
    private static DBConnection dbConnection = new DBConnection();

    private static Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setID(rs.getInt("ID"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("FirstName"));
        user.setLastName(rs.getString("LastName"));
        user.setEmail(rs.getString("Email"));
        user.setPassword(rs.getString("Password"));

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM messenjer.Users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)
        ) {
            List<User> users = new LinkedList();

            while (rs.next()) {
                users.add(this.createUserFromResultSet(rs));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public List<String> getAllUsernames() {
        List<User> users = this.getAllUsers();
        List<String> usernames = new LinkedList<>();

        if (users != null) {
            for (User user : users) {
                usernames.add(user.getUsername());
            }
        }
        return usernames;
    }

    @Override
    public User getUserByID(int id) {
        String sql = "SELECT * FROM messenjer.Users where ID=" + id;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)
        ) {
            if (rs.next()) {
                return this.createUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM messenjer.Users where username=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return this.createUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveUser(User user) {
        String sql = "INSERT INTO messenjer.Users (username, Password, FirstName, LastName, Email) VALUES (?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, user.getEmail());

            int i = pstmt.executeUpdate();
            if (i == 1) {   // if there is one affected row in the query.
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return false;
    }

    // Do not need this methods in this phase
    // TODO implement delete and update methods
    @Override
    public boolean deleteUser(int id) {
        return false;
    }

    @Override
    public boolean updateUser(int id) {
        return false;
    }
}
