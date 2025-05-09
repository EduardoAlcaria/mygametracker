package org.example.mygametrackerjavafx.connectionDAO;

import org.example.mygametrackerjavafx.Model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDAO {
    public static void createAccount(String userName) {
        System.out.println("user name: " + userName);
        if (userName == null) {
            throw new IllegalArgumentException("userName is null");
        }
        if (getUserFromDb(userName) != null) {
            System.out.println("user name" + getUserFromDb(userName));
        }


    }

    public static String getUserFromDb(String user) {
        String sqlQuery = "SELECT user_name FROM users WHERE user_name = '" + user + "'";
        try (Connection conn = ConnectionDAO.getConnection();
             Statement stmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                return rs.getString("user_name");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
