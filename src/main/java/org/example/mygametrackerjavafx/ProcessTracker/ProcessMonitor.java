package org.example.mygametrackerjavafx.ProcessTracker;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessMonitor {
    private static final Set<String> trustedPaths = new HashSet<>(List.of(
            "steam\\steamapps\\common",
            "c:\\games"));

    public static void main(String[] args) {


        Set<Integer> seenPids = new HashSet<>();

        while (true) {

            List<ProcessScanner.ProcessInfo> processes = ProcessScanner.getAllProcessPaths();

            for (ProcessScanner.ProcessInfo p : processes) {

                if (!seenPids.contains(p.pid)) {
                    seenPids.add(p.pid);

                    if (folderExists(p.path)) {
                        System.out.println(p.path);
                        System.out.println(getGameName(p.path));
                    }

                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getGameName(String path) {
        try {
            Path f = Paths.get(path);
            Path parentPath = f.getParent();
            if (parentPath != null) {
                return parentPath.getFileName().toString();
            }
        } catch (Exception ignored) {
            return "";
        }
        return "";
    }

    public static boolean folderExists(String path) {
        String pathLowerCase = path.toLowerCase();
        for (String trustedPath : trustedPaths) {
            if (pathLowerCase.contains(trustedPath)) {
                return true;
            }
        }
        return false;
    }
}