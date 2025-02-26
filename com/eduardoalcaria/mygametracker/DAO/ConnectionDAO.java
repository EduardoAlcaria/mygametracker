package com.eduardoalcaria.mygametracker.DAO;

import javax.swing.*;
import java.sql.*;


public class ConnectionDAO {

    public Connection connection(){
        Connection conn = null;
        try {

            String url = "jdbc:mysql://localhost:3306/mygametracker_localhost?user=root&password=";
            conn = DriverManager.getConnection(url);

            JOptionPane.showMessageDialog(null, "Connection Successful");


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return conn;
    }

}
