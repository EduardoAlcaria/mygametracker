package org.example.mygametrackerjavafx.Main;

import org.apache.commons.lang3.time.StopWatch;
import org.example.mygametrackerjavafx.FolderFinder.FolderUserInput;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessFolderVerifier;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessGameGetter;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessScanner;

import java.io.IOException;
import java.nio.file.Path;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws IOException {
        FolderUserInput.UserCustomPathWriter();

        final String GREEN_BOLD = "\033[1;32m";
        final String RESET = "\033[0m";

        StopWatch stopWatch = new StopWatch();
        Set<Integer> seenPids = new HashSet<>();

        String gameName = "";

        boolean isListening = true;

        while (true) {
            if (isListening) {
                System.out.println(GREEN_BOLD + " Listening ... " + RESET);
                isListening = false;
            }
            List<ProcessScanner.ProcessInfo> processes = ProcessScanner.getAllProcessPaths();

            for (ProcessScanner.ProcessInfo p : processes) {
                if (!seenPids.contains(p.pid)) {
                    seenPids.add(p.pid);

                    if (ProcessFolderVerifier.matches(Path.of(p.path)) && !gameName.equals(ProcessGameGetter.getGameName(p.path))) {
                        if (stopWatch.isStarted()) {
                            stopWatch.stop();
                            stopWatch.reset();
                        }
                        stopWatch.start();
                        gameName = ProcessGameGetter.getGameName(p.path);
                        System.out.println(GREEN_BOLD + " Game Found: " + gameName + RESET);
                        System.out.println(GREEN_BOLD + " Path: " + p.path + RESET);
                        isListening = true;
                        break;
                    }
                }

            }
            boolean isRunning = false;
            for (ProcessScanner.ProcessInfo p : processes) {
                String currentProcessName = ProcessGameGetter.getGameName(p.path);
                if (gameName.equals(currentProcessName)) {
                    isRunning = true;
                    break;
                }
            }

            if (!isRunning && stopWatch.isStarted()) {
                stopWatch.stop();
                stopWatch.reset();
                System.out.println(gameName);
                System.out.println(stopWatch.getTime(TimeUnit.SECONDS));
            }
            stopWatch.reset();
            gameName = "";
            seenPids.clear();
            isListening = true;


            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



        /* while (true) {
            System.out.println("1 for register a game");
            System.out.println("2 for see the games");
            System.out.println("3 to exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter the game name: ");
                    String gameName = scanner.nextLine();

                    game1.setGameName(gameName);

                    System.out.print("Enter the game genre: ");
                    String gameGenre = scanner.nextLine();

                    game1.setGameGenre(gameGenre);

                    System.out.print("Have you finished playing " + game1.getGameName() + " (y/n)? ");
                    char playingStatus = scanner.nextLine().charAt(0);
                    playingStatus = charVerification(playingStatus);

                    switch (playingStatus) {
                        case 'y':
                            game1.setStatus("Finished");
                            break;
                        case 'n':
                            game1.setStatus("Playing");
                    }

                    System.out.print("Do you remember when you started playing (y/n)? ");
                    char doYouRememberStartPlayingDate = Character.toLowerCase(scanner.next().charAt(0));
                    doYouRememberStartPlayingDate = charVerification(doYouRememberStartPlayingDate);

                    scanner.nextLine();

                    switch (doYouRememberStartPlayingDate) {
                        case 'y':
                            System.out.print("Enter the starting playing date (dd/mm/yyyy): ");
                            String startPlayingOn = scanner.nextLine();
                            Date dateStartPlaying = Date.valueOf(LocalDate.parse(startPlayingOn, formatter));
                            game1.setStartPlayingOn(dateStartPlaying);
                            break;

                        case 'n':
                            break;
                    }

                    if (Objects.equals(game1.getStatus(), "Finished")) {
                        System.out.print("Do you remember the date that you ended playing (y/n)? ");
                        char doYouRememberEndedPlayingDate = Character.toLowerCase(scanner.next().charAt(0));
                        doYouRememberEndedPlayingDate = charVerification(doYouRememberEndedPlayingDate);

                        scanner.nextLine();

                        switch (doYouRememberEndedPlayingDate) {
                            case 'y':
                                System.out.print("Enter the finished playing date (dd/mm/yyyy): ");
                                String finishedPlayingOn = scanner.nextLine();
                                Date dateFinishedPlaying = Date.valueOf(LocalDate.parse(finishedPlayingOn, formatter));
                                System.out.println(dateFinishedPlaying);
                                game1.setFinishedPlayingOn(dateFinishedPlaying);
                                break;

                            case 'n':
                                break;
                        }
                    }
                    insertGames.insert(game1.getGameName(), game1.getGameGenre(), game1.getStatus(),
                            game1.getStartPlayingOn(), game1.getFinishedPlayingOn());
                    break;

                case 2:
                    System.out.println();
                    insertGames.print();
                    break;
                case 3:
                    break;
            }
            if (choice == 3) break;
        }
        scanner.close();
    }

    public static char charVerification(char character) {
        if (character != 'y' && character != 'n') {
            do {
                System.out.print("Invalid Option, type ONLY Y or N: ");
                Scanner verifycationScanner = new Scanner(System.in);
                character = Character.toLowerCase(verifycationScanner.next().charAt(0));
            } while (character != 'y' && character != 'n');
        }
        return character;
    }
    */
}

