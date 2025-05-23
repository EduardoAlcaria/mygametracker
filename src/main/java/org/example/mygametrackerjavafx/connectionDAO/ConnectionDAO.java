package org.example.mygametrackerjavafx.connectionDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDAO {
    private static final String url = "";
    private static final String user = "";
    private static final String password = "";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            throw new SQLException("failed to get the connection" + e);
        }
    }

}