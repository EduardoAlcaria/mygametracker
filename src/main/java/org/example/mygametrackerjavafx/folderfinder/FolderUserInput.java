package org.example.mygametrackerjavafx.folderfinder;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.io.*;

public class FolderUserInput {
    private static final String USERCUSTOMPATHFILETXT = "userCustomGamePathChoice.txt";

    public static void UserCustomPathWriter() throws IOException {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        File customDir = new File(USERCUSTOMPATHFILETXT);
        boolean validPathExists = false;

        try {
            if (!customDir.exists()) {
                customDir.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Could not create the file: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Could not create the file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(USERCUSTOMPATHFILETXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                File fileToCheck = new File(line);
                if (fileToCheck.isDirectory()) {
                    System.out.println("Found a custom path: " + line);
                    validPathExists = true;
                    break;
                }
            }
        }

        if (!validPathExists) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Do you have a custom game path?",
                    "Custom Path", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                while (true) {
                    String customPathName = JOptionPane.showInputDialog(null,
                            "Enter the directory path:",
                            "Custom Game Path", JOptionPane.QUESTION_MESSAGE);

                    if (customPathName == null) {

                        System.out.println("Custom path input cancelled");
                        break;
                    }

                    File file = new File(customPathName);
                    if (!file.isDirectory()) {
                        JOptionPane.showMessageDialog(null,
                                "Please enter a valid directory path",
                                "Invalid Path", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    try (FileWriter writer = new FileWriter(USERCUSTOMPATHFILETXT, false)) {
                        writer.write(customPathName);
                        System.out.println("Custom path saved: " + customPathName);
                    }
                    break;
                }
            } else {
                System.out.println("Following the program without a custom path");
            }
        }
    }
}