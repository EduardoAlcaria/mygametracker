package org.example.mygametrackerjavafx.FolderFinder;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

public class FolderUserInput{
    static Scanner scanner = new Scanner(System.in);
    private static final String USERCUSTOMPATHFILETXT = "userCustomGamePathChoice.txt";


    public static String UserCustomPathWriter() throws IOException {
        char choice;
        char lowerChoice;
        String customPathName;

        File doesFileExists = new File(USERCUSTOMPATHFILETXT);

        if (!doesFileExists.exists()) {
            while (true) {
                System.out.println("Do you have a custom game path? [y/n]: ");
                choice = scanner.next().charAt(0);
                scanner.nextLine();

                lowerChoice = Character.toLowerCase(choice);

                if (lowerChoice == 'y') {
                    while (true) {
                        System.out.println("type the directory: ");
                        customPathName = scanner.nextLine();

                        File file = new File(customPathName);

                        if (!file.isDirectory()) {
                            System.out.println("Please type only valid dirs");
                            continue;
                        }
                        if (file.isDirectory()) {
                            FileWriter writer = new FileWriter(USERCUSTOMPATHFILETXT, true);
                            writer.write(customPathName);
                            writer.close();
                            break;
                        }
                    }
                    break;

                }

                if (lowerChoice != 'n' && lowerChoice != 'y') {
                    System.out.println("invalid option");
                    continue;
                }

                if (lowerChoice == 'n') {
                    System.out.println("following the program without a custom path");
                    break;
                }

            }
        }
        return null;
    }
    public static String UserCustomPathReader() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(USERCUSTOMPATHFILETXT));
        String line;
        while ((line = reader.readLine()) != null) {
            return line;
        }

        return null;
    }
}
