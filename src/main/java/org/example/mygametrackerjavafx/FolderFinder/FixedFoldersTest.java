package org.example.mygametrackerjavafx.FolderFinder;

import java.io.File;
import java.nio.file.Path;


public class FixedFoldersTest {
    public static void main(String[] args) {

        boolean check1 = FixedFolders.matches(Path.of("C:\\Program Files (x86)\\Steam\\steamapps\\common"));
        boolean check2 = FixedFolders.matches(Path.of("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Grand Theft Auto V"));
        boolean check3 = FixedFolders.matches(Path.of("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Grand Theft Auto V\\PlayGTAV.exe"));
        File check4 = new File("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Grand Theft Auto V\\PlayGTAV.exe");
        File check5 = new File("C:\\Program Files (x86)\\Steam\\steamapps\\mon\\Counter-Strike Global Offensive\\game\\bin\\win64\\cs2.exe");





        int c = 0;


        if (check1) {
            c++;
            System.out.println(c + "/5 passed");
        } else {
            System.out.println(c + "/5 failed");
        }
        if (check2) {
            c++;
            System.out.println(c + "/5 passed");
        } else {
            System.out.println(c + "/5 failed");
        }
        if (check3) {
            c++;
            System.out.println(c + "/5 passed");
        } else {
            System.out.println(c + "/5 failed");
        }

        if (check4.isFile()) {
            check4 = new File(check4.getParent());
            if (FixedFolders.matches(check4.toPath())) {
                c++;
                System.out.println(c + "/5 passed");
            }
        } else {
            System.out.println(c + "/5 failed");

        }
        if (check5.isFile()) {
            check5 = new File(check5.getParent());
            if (FixedFolders.matches(check5.toPath())) {
                c++;
                System.out.println(c + "/5 passed");

            }
        } else {
            c++;
            System.out.println(c + "/5 failed");
        }

    }
}
