package org.example.mygametrackerjavafx.connectionDAO;


import java.sql.*;

public class UserDAO {

    protected static boolean createAccount(String user, String password) {
        if (getUserFromDb(user)) {
            System.out.println("the user already exist in the database");
            return false;
        }
        String sqlQuery = "INSERT INTO users (user_name, password_hash) VALUES (?, ?)";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {


            stmt.setString(1, user);
            stmt.setString(2, password);


            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("failed to create the user account" + e);
        }

    }

    protected static boolean login(String userName, String password) throws SQLException {
        if (getUserFromDb(userName)) {
            String sqlQuery = "SELECT * FROM users WHERE user_name = ?";
            try (Connection conn = ConnectionDAO.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

                stmt.setString(1, userName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {

                        String dbPassword = rs.getString("password_hash");
                        return dbPassword.equals(password);
                    }
                }
            }
        }
        return false;
    }


    protected static boolean getUserFromDb(String user) {
        String sqlQuery = "SELECT 1 FROM users WHERE user_name = ?";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setString(1, user);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("failed to check if the user exists" + e);
        }
    }

    protected static int getUserID(String userName) {
        String sqlQuery = "SELECT user_id FROM users WHERE user_name = ?";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            } else {
                throw new RuntimeException("user not found in the database");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
