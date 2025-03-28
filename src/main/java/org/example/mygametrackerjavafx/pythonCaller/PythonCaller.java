package org.example.mygametrackerjavafx.pythonCaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class PythonCaller {
    public static void main(String[] args) throws IOException {
        Date date = new Date();
        try {
             ProcessBuilder processBuilder = new ProcessBuilder("python",
                     System.getProperty("user.dir") + "\\src\\main\\java\\org\\example\\mygametrackerjavafx\\python_mygametracker\\steamWebScrapper.py",
                     "cyberpunk 2077");
             Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line=null;
            while ((line=reader.readLine()) != null) {
                    System.out.println(date + "  " + line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
