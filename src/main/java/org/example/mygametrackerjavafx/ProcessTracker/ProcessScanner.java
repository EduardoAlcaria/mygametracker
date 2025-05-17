package org.example.mygametrackerjavafx.ProcessTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessScanner {
    public static class ProcessInfo {
        public final int pid;
        public final String path;
        public final String name;

        public ProcessInfo(int pid, String path, String name) {
            this.pid = pid;
            this.path = path;
            this.name = name;
        }

        @Override
        public String toString() {
            return "PID: " + pid + " | Path: " + path + " | Name: " + name;
        }
    }

    public static List<ProcessInfo> getAllProcessPaths(){
        List<ProcessInfo> processList = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("wmic process get Name,ProcessId,ExecutablePath /format:csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",", 4);
                if (parts.length >= 4 && !parts[3].isEmpty()) {
                    try {
                        String name = parts[2].trim();
                        String path = parts[1].trim();
                        int pid = Integer.parseInt(parts[3].trim());
                        processList.add(new ProcessInfo(pid, path, name));
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

    public static boolean isRunning(int pid) {
        try {
            Process proc = Runtime.getRuntime().exec("cmd /c tasklist /FI \"PID eq " + pid + "\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(String.valueOf(pid))){
                    return true;
                }
            }
            reader.close();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}