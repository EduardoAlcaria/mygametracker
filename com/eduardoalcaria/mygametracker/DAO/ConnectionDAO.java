package com.eduardoalcaria.mygametracker.DAO;

import java.sql.*;

public class ConnectionDAO {
    private static final String url = "jdbc:mysql://localhost:3306/mygametracker_localhost";
    private static final String user = "root";
    private static final String password = "";
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }
}