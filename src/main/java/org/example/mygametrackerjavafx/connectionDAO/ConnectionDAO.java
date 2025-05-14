package org.example.mygametrackerjavafx.connectionDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDAO {
    private static final String url = "jdbc:mysql://root:fnXJaDfEaTXCBVScTJSMTEOAFnsNjDGA@mainline.proxy.rlwy.net:17069/railway";
    private static final String user = "root";
    private static final String password = "fnXJaDfEaTXCBVScTJSMTEOAFnsNjDGA";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            throw new SQLException("failed to get the connection" + e);
        }
    }
}