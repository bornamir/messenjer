package org.bihe.sevices;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/messenjer";
    private static final String USERNAME = "borna";
    private static final String PASS = "borna620";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private Connection connection;
    public DBConnection() {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() throws SQLException{
            return DriverManager.getConnection(URL, USERNAME, PASS);
    }
}
