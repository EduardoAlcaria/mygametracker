package org.example.mygametrackerjavafx.connectionDAO;

public class UserDAOHandler {
    public static void createAccount(String userName) throws Exception {
        UserDAO.createAccount(userName);
    }
}
