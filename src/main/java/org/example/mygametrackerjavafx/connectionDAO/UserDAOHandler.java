package org.example.mygametrackerjavafx.connectionDAO;

import org.example.mygametrackerjavafx.model.User;

public class UserDAOHandler {
    public static boolean createAccount(User user) throws Exception {
        if (user == null){
            throw new Exception("Username or password is null");
        }

        try {
            return UserDAO.createAccount(user.getName(), user.getPassword());
        }
        catch (Exception e){
            throw new Exception("failed to create the user account" + e);
        }
    }
    public static boolean login(String userName, String password) throws Exception {
        if (userName == null || password == null) {
            throw new IllegalArgumentException("userName or password is null");
        }
        try {
            return UserDAO.login(userName, password);
        }catch (IllegalArgumentException e){
            throw e;
        } catch (Exception e) {
            throw new Exception("failed to login" + e);
        }
    }
    public static int getUserID(String userName) throws Exception {
        if (userName == null) throw new IllegalArgumentException("userName is null");
        try {
            return UserDAO.getUserID(userName);
        }catch (Exception e){
            throw new Exception("failed to get the user id" + e);
        }
    }
}
