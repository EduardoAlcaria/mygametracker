package org.example.mygametrackerjavafx.FolderFinder;

import java.io.*;
import java.nio.file.Path;

public class FolderGetterValid {

    public static boolean isValid(Path dirToCheck) {

        String MUST_MATCH_TXT = "mustMatch.txt";

        File doesFileExists = new File(MUST_MATCH_TXT);

        try {
            if (doesFileExists.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(MUST_MATCH_TXT));
                String line;

                while ((line = reader.readLine()) != null) {
                    Path normalized = dirToCheck.toAbsolutePath().normalize();
                    return normalized.toString().toLowerCase().contains(line);
                }
            }

        } catch (FileNotFoundException e) {

            System.out.println("could not find the file: " + e.getMessage());;

        } catch (IOException e) {

            System.out.println("could not read the file: " + e.getMessage());;

        }
        return false;
    }
}
