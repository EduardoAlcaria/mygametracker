package org.example.mygametrackerjavafx.ProcessTracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessScanner {
    public static class ProcessInfo {
        public final int pid;
        public final String path;

        public ProcessInfo(int pid, String path) {
            this.pid = pid;
            this.path = path;
        }

        @Override
        public String toString() {
            return "PID: " + pid + " | Path: " + path;
        }
    }

    public static List<ProcessInfo> getAllProcessPaths(){
        List<ProcessInfo> processList = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("wmic process get ProcessId,ExecutablePath /format:csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",", 3);
                if (parts.length >= 3 && !parts[2].isEmpty()) {
                    try {
                        int pid = Integer.parseInt(parts[2].trim());
                        String path = parts[1].trim();
                        processList.add(new ProcessInfo(pid, path));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processList;
    }
}