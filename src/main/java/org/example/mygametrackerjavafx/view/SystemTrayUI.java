package org.example.mygametrackerjavafx.view;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.mygametrackerjavafx.model.User;
import org.example.mygametrackerjavafx.connectionDAO.UserDAOHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class SystemTrayUI {
    private static final PopupMenu popup = new PopupMenu();
    private static final Menu loginMenu = new Menu("Login");
    private static String currentUser;
    private static MenuItem logoutButton = new MenuItem("Logout");
    private static final String credentialsPath = "credentials.txt";
    private static final File file = new File(credentialsPath);
    private static boolean userLogOut = false;

    private enum currentPage{
        REGISTER,
        LOGIN
    }

    public static String getCurrentUser() throws Exception {
        if (isRemembered()){
            currentUser = loadLoginLocally()[0];
            updateLogInLabel();
        }
        return currentUser;
    }
    public static boolean isRemembered() throws Exception {
        return file.exists() && file.length() > 0;
    }

    public static void startUI() throws Exception {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported.");
            return;
        }

        if (!isRemembered()) {
            loginHandler();
        }

        buttons();

        String imagePath = "donut_pixel_art_16x16.png";
        Image image = Toolkit.getDefaultToolkit().getImage(imagePath);


        TrayIcon trayIcon = new TrayIcon(image, "My Tray App", popup);
        trayIcon.setImageAutoSize(true);


        trayIcon.addActionListener(e -> {
            System.out.println("Tray icon clicked");
        });

        try {
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);
            System.out.println("App running in system tray.");
        } catch (AWTException e) {
            System.out.println("Tray Icon could not be added.");
            e.printStackTrace();
        }
    }

    private static void buttons() {

        MenuItem exitItem = new MenuItem("Exit");
        MenuItem loginButton = new MenuItem("Login");
        MenuItem registerButton = new MenuItem("Register");

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        loginButton.addActionListener(e -> {
            try {
                loginHandler();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        registerButton.addActionListener(e -> {
            try {
                registerHandler();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        loginMenu.add(loginButton);
        loginMenu.add(registerButton);
        loginMenu.add(exitItem);

        popup.add(loginMenu);
    }
    public static void showLoginDialogImmediately() {
        SwingUtilities.invokeLater(() -> {
            try {
                String[] input = inputHandler(currentPage.LOGIN);
                if (input != null) {
                    if (UserDAOHandler.login(input[0], input[1])) {
                        currentUser = input[0];
                        JOptionPane.showMessageDialog(null, "Login successful");
                        updateLogInLabel();
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong username or password");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error showing login dialog: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void loginHandler() throws Exception {

        String[] input = inputHandler(currentPage.LOGIN);
        if (input == null) return;

        if (UserDAOHandler.login(input[0], input[1])) {
            currentUser = input[0];
            JOptionPane.showMessageDialog(null, "Login successful");
            updateLogInLabel();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong username or password");
        }

    }

    private static void registerHandler() throws Exception {
        String[] input = inputHandler(currentPage.REGISTER);
        if (input == null) return;

        User user = new User(input[0], input[1]);

        if (UserDAOHandler.createAccount(user)) {
            JOptionPane.showMessageDialog(null, "Registration successful");
            loginHandler();
        } else {
            JOptionPane.showMessageDialog(null, "The user already exists");
        }

    }

    private static String[] inputHandler(currentPage page) throws Exception {
        if (!isRemembered() || userLogOut) {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints cs = new GridBagConstraints();

            cs.fill = GridBagConstraints.HORIZONTAL;
            cs.insets = new Insets(10, 10, 10, 10);


            JTextField userField = new JTextField(15);
            JPasswordField passField = new JPasswordField(15);

            JCheckBox rememberMe = new JCheckBox("Remember me");

            cs.gridx = 0;
            cs.gridy = 0;
            panel.add(new JLabel("Username:"), cs);

            cs.gridx = 1;
            panel.add(userField, cs);

            cs.gridx = 0;
            cs.gridy = 1;
            panel.add(new JLabel("Password:"), cs);

            cs.gridx = 1;
            panel.add(passField, cs);

            cs.gridx = 0;
            cs.gridy = 2;

            int result;
            if (page == currentPage.LOGIN){
                panel.add(rememberMe, cs);
                result = JOptionPane.showConfirmDialog(null, panel, "🔐 Login",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            }else{
                result = JOptionPane.showConfirmDialog(null, panel, "🔐 Register",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            }

            if (result == JOptionPane.OK_OPTION) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return inputHandler(page);
                }

                if (rememberMe.isSelected()) {
                    saveLoginLocally(username, password);
                }
                return new String[]{username, password};
            }

        }
        return null;
    }

    private static void logOut() throws Exception {
        currentUser = null;
        loginMenu.setLabel("Login");
        loginMenu.removeAll();
        userLogOut = true;
        buttons();
    }

    private static void updateLogInLabel() {
        if (currentUser != null) {
            loginMenu.setLabel(currentUser);
            loginMenu.removeAll();
            loginMenu.add(logoutButton);
            logoutButton.addActionListener(e -> {
                try {
                    logOut();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    private static void saveLoginLocally(String userName, String password) {
        password = BCrypt.hashpw(password, BCrypt.gensalt());

        try {
            FileWriter writer = new FileWriter(credentialsPath);
            writer.write(userName + "\n" + password);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] loadLoginLocally() throws IOException {
        String[] credentials = new String[2];
        int linesRead = 0;
        BufferedReader reader = new BufferedReader(new FileReader(credentialsPath));
        String line;
        while ((line = reader.readLine()) != null) {
            if (linesRead == 0) {
                credentials[0] = line;
            }
            if (linesRead == 1) {
                credentials[1] = line;
            }
            linesRead++;
        }
        return credentials;
    }

}