package org.example.mygametrackerjavafx.ProcessTracker;


public class ProcessTracker {

    public static boolean isGameRunning(String gameName) {
        try {
            return ProcessHandle.allProcesses().anyMatch(processes -> processes.info()
                    .command()
                    .map(cmd -> cmd.toLowerCase().contains(gameName.toLowerCase()))
                    .orElse(false));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
