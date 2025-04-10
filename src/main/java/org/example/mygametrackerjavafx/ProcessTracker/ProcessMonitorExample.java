package org.example.mygametrackerjavafx.ProcessTracker;

import java.util.HashSet;
import java.util.Set;

/**
 * Example class showing how to use the ProcessMonitor
 */
public class ProcessMonitorExample {
    public static void main(String[] args) {
        // Keep track of processes we've seen to avoid duplicates
        Set<Integer> seenProcesses = new HashSet<>();

        try (ProcessMonitor monitor = new ProcessMonitor()) {
            // Start monitoring and provide a callback
            monitor.startMonitoring(process -> {
                int pid = process.getProcessId();

                // Only show each process once
                if (!seenProcesses.contains(pid)) {
                    seenProcesses.add(pid);

                    System.out.println("Process: " + process.getName() +
                            " (PID: " + pid + ")");

                    if (!process.getExecutablePath().isEmpty()) {
                        System.out.println("  Path: " + process.getExecutablePath());
                    } else {
                        System.out.println("  Path: [Not available]");
                    }

                    if (!process.getCommandLine().isEmpty()) {
                        System.out.println("  Command Line: " + process.getCommandLine());
                    } else {
                        System.out.println("  Command Line: [Not available]");
                    }

                    System.out.println("  Is New: " + process.isNewProcess());
                    System.out.println();
                } else if (process.isNewProcess()) {
                    // This is a special case for when we detect a new process with a PID
                    // that we've seen before (PID reuse after process termination)
                    System.out.println("NEW Process: " + process.getName() +
                            " (PID: " + pid + ")");

                    if (!process.getExecutablePath().isEmpty()) {
                        System.out.println("  Path: " + process.getExecutablePath());
                    } else {
                        System.out.println("  Path: [Not available]");
                    }

                    if (!process.getCommandLine().isEmpty()) {
                        System.out.println("  Command Line: " + process.getCommandLine());
                    } else {
                        System.out.println("  Command Line: [Not available]");
                    }

                    System.out.println();
                }
            });

            // Run for 2 minutes
            System.out.println("Monitoring processes for 2 minutes...");
            System.out.println("Start any application to see it detected.");
            Thread.sleep(120000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}