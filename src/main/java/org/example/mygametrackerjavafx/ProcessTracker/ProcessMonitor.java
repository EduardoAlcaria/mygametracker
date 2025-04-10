package org.example.mygametrackerjavafx.ProcessTracker;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OleAuto;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A robust Windows Process Monitor using direct process listing via Runtime.
 * This approach avoids WMI issues by using standard Java Process APIs.
 */
public class ProcessMonitor implements Closeable {
    private boolean initialized = false;
    private ExecutorService executorService;
    private boolean monitoring = false;
    private Consumer<ProcessInfo> processCallback;

    /**
     * Process information class
     */
    public static class ProcessInfo {
        private final String name;
        private final int processId;
        private final String commandLine;
        private final String executablePath;
        private final boolean isNewProcess;

        public ProcessInfo(String name, int processId, String commandLine, String executablePath, boolean isNewProcess) {
            this.name = name;
            this.processId = processId;
            this.commandLine = commandLine;
            this.executablePath = executablePath;
            this.isNewProcess = isNewProcess;
        }

        public String getName() { return name; }
        public int getProcessId() { return processId; }
        public String getCommandLine() { return commandLine; }
        public String getExecutablePath() { return executablePath; }
        public boolean isNewProcess() { return isNewProcess; }

        @Override
        public String toString() {
            return "Process[name=" + name + ", pid=" + processId +
                    ", path=" + executablePath + ", new=" + isNewProcess + "]";
        }
    }

    /**
     * Creates a new ProcessMonitor.
     */
    public ProcessMonitor() {
        // No COM initialization here - we'll use a different approach
        this.initialized = true;
    }

    /**
     * Starts monitoring for processes.
     *
     * @param callback the callback to call when a process is found or created
     */
    public synchronized void startMonitoring(Consumer<ProcessInfo> callback) {
        if (monitoring) {
            return;
        }

        this.processCallback = callback;
        this.monitoring = true;

        // Initialize the executor
        executorService = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Process-Monitor");
            t.setDaemon(true);
            return t;
        });

        // Start monitoring
        executorService.execute(() -> {
            try {
                // First get existing processes
                queryExistingProcesses();

                // Then periodically check for new processes
                List<Integer> knownPids = getCurrentProcessIds();

                while (monitoring) {
                    try {
                        Thread.sleep(2000); // Check every 2 seconds

                        List<Integer> currentPids = getCurrentProcessIds();
                        List<Integer> newPids = new ArrayList<>(currentPids);
                        newPids.removeAll(knownPids);

                        if (!newPids.isEmpty()) {
                            queryProcessesById(newPids, true);
                            knownPids = currentPids;
                        }
                    } catch (InterruptedException e) {
                        if (!monitoring) break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Gets all existing processes on the system using tasklist command.
     */
    private void queryExistingProcesses() {
        try {
            Process process = Runtime.getRuntime().exec("tasklist /fo csv /nh");
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // Parse CSV line from tasklist
                    // Format: "name","PID","Session Name","Session#","Mem Usage"
                    String[] parts = parseCSV(line);
                    if (parts.length >= 2) {
                        String name = parts[0];
                        int pid = Integer.parseInt(parts[1]);

                        // Get more details from wmic
                        String[] details = getProcessDetails(pid);
                        String execPath = details[0];
                        String commandLine = details[1];

                        ProcessInfo processInfo = new ProcessInfo(name, pid, commandLine, execPath, false);
                        if (processCallback != null) {
                            processCallback.accept(processInfo);
                        }
                    }
                } catch (Exception e) {
                    // Skip problematic entries
                    System.err.println("Error parsing process: " + line);
                    e.printStackTrace();
                }
            }

            process.waitFor();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse CSV line respecting quotes
     */
    private String[] parseCSV(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }

        result.add(currentValue.toString());
        return result.toArray(new String[0]);
    }

    /**
     * Get process executable path and command line using wmic
     */
    private String[] getProcessDetails(int pid) {
        String execPath = "";
        String commandLine = "";

        try {
            // First get the executable path
            Process process = Runtime.getRuntime().exec(
                    "wmic process where processid=" + pid + " get ExecutablePath /format:list");
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("ExecutablePath=")) {
                    execPath = line.substring("ExecutablePath=".length());
                    break;
                }
            }

            process.waitFor();
            reader.close();

            // Then get the command line
            process = Runtime.getRuntime().exec(
                    "wmic process where processid=" + pid + " get CommandLine /format:list");
            reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("CommandLine=")) {
                    commandLine = line.substring("CommandLine=".length());
                    break;
                }
            }

            process.waitFor();
            reader.close();

        } catch (Exception e) {
            // Ignore errors for specific processes that might not allow access
        }

        return new String[] { execPath, commandLine };
    }

    /**
     * Get current process IDs from the system
     */
    private List<Integer> getCurrentProcessIds() {
        List<Integer> pids = new ArrayList<>();

        try {
            Process process = Runtime.getRuntime().exec("tasklist /fo csv /nh");
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = parseCSV(line);
                    if (parts.length >= 2) {
                        int pid = Integer.parseInt(parts[1]);
                        pids.add(pid);
                    }
                } catch (Exception e) {
                    // Skip problematic entries
                }
            }

            process.waitFor();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pids;
    }

    /**
     * Query processes by their process IDs
     */
    private void queryProcessesById(List<Integer> pids, boolean isNew) {
        if (pids.isEmpty()) return;

        for (Integer pid : pids) {
            try {
                Process process = Runtime.getRuntime().exec("tasklist /fi \"PID eq " + pid + "\" /fo csv /nh");
                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream()));

                String line = reader.readLine();
                if (line != null) {
                    try {
                        String[] parts = parseCSV(line);
                        if (parts.length >= 2) {
                            String name = parts[0];

                            // Get more details from wmic
                            String[] details = getProcessDetails(pid);
                            String execPath = details[0];
                            String commandLine = details[1];

                            ProcessInfo processInfo = new ProcessInfo(name, pid, commandLine, execPath, isNew);
                            if (processCallback != null) {
                                processCallback.accept(processInfo);
                            }
                        }
                    } catch (Exception e) {
                        // Skip problematic entries
                    }
                }

                process.waitFor();
                reader.close();

            } catch (Exception e) {
                // Skip problematic PIDs
            }
        }
    }

    /**
     * Stops monitoring for processes.
     */
    public synchronized void stopMonitoring() {
        if (!monitoring) {
            return;
        }

        monitoring = false;

        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
            executorService = null;
        }

        processCallback = null;
    }

    /**
     * Checks if monitoring is active.
     *
     * @return true if monitoring, false otherwise
     */
    public boolean isMonitoring() {
        return monitoring;
    }

    /**
     * Releases all resources.
     */
    @Override
    public void close() {
        stopMonitoring();

        if (initialized) {
            initialized = false;
        }
    }
}