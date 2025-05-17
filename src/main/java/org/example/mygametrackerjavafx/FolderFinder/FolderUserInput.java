package org.example.mygametrackerjavafx.FolderFinder;

import java.io.*;
import java.util.Scanner;

public class FolderUserInput {
    static Scanner scanner = new Scanner(System.in);
    private static final String USERCUSTOMPATHFILETXT = "userCustomGamePathChoice.txt";

    static {
        File file = new File(USERCUSTOMPATHFILETXT);
        try {

            if (!file.exists()){
                file.createNewFile();
            }
        }catch (IOException e){
            System.out.println("could not create the file: " + e.getMessage());
        }
    }
    public static boolean UserCustomPathWriter() throws IOException {
        char choice;
        String customPathName;

        BufferedReader reader = new BufferedReader(new FileReader(USERCUSTOMPATHFILETXT));
        String line;
        File fileToCheck;

        while ((line = reader.readLine()) != null) {
            fileToCheck = new File(line);

            if (fileToCheck.isDirectory()) {
                System.out.println("found a custom path: " + line);
                return true;
            }else{
                while (true) {
                    System.out.println("Do you have a custom game path? [y/n]: ");
                    choice = scanner.next().charAt(0);
                    scanner.nextLine();

                    if (Character.toLowerCase(choice) == 'y') {
                        while (true) {
                            System.out.println("type the directory: ");
                            customPathName = scanner.nextLine();

                            File file = new File(customPathName);

                            if (!file.isDirectory()) {
                                System.out.println("Please type only valid dirs");
                                continue;
                            }
                            if (file.isDirectory()) {
                                FileWriter writer = new FileWriter(USERCUSTOMPATHFILETXT, false);
                                writer.write(customPathName);
                                writer.close();
                                return true;
                            }
                        }

                    }

                    if (Character.toLowerCase(choice) == 'n') {
                        System.out.println("following the program without a custom path");
                        return false;
                    }
                    System.out.println("invalid option, please type only y or n:");
                }
            }

        }
        return false;
    }
}
