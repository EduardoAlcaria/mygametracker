package org.example.mygametrackerjavafx.folderfinder;

import java.io.*;
import java.nio.file.Path;

public class FolderGetterValidUser {

    public static boolean isValid(Path dirToCheck) throws IOException {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("userCustomGamePathChoice.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                return dirToCheck.toString().contains(line);
            }

        } catch (FileNotFoundException e) {
            System.out.println("could not find the file: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("could not read the file: " + e.getMessage());
        }
        return false;
    }

}
