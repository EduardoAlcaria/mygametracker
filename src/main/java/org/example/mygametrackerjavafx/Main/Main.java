package org.example.mygametrackerjavafx.Main;

import org.apache.commons.lang3.time.StopWatch;
import org.example.mygametrackerjavafx.FolderFinder.FolderUserInput;
import org.example.mygametrackerjavafx.Model.Game;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessFolderVerifier;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessGameGetter;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessScanner;
import org.example.mygametrackerjavafx.View.SystemTrayUI;
import org.example.mygametrackerjavafx.connectionDAO.GamesDAOHandler;
import org.example.mygametrackerjavafx.connectionDAO.UserDAOHandler;


import java.nio.file.Path;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Main {
    private static final List<String> gamesDB = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        SystemTrayUI.startUI();

        if (!SystemTrayUI.isRemembered()) {
            while (SystemTrayUI.getCurrentUser() == null) {
                System.out.println("Pleace log in");
                Thread.sleep(3000);
            }
        }
        System.out.println(SystemTrayUI.getCurrentUser());

        int userID = UserDAOHandler.getUserID(SystemTrayUI.getCurrentUser());
        Game.setUserID(userID);
        FolderUserInput.UserCustomPathWriter();


        final String GREEN_BOLD = "\033[1;32m";
        final String RESET = "\033[0m";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        LocalDate now = LocalDate.now();

        Date today = Date.valueOf(now);

        Date parsedDateStart = null;
        Date parsedDateFinish = null;

        List<String> validGames = new ArrayList<>();

        Set<Integer> seenPids = new HashSet<>();
        Set<String> seenGameNames = new HashSet<>();

        Map<Integer, StopWatch> gameStopWatches = new HashMap<>();
        Map<Integer, String> gamePid = new HashMap<>();


        String gameName = "";
        String genre = "";

        List<Integer> pidsList = new ArrayList<>();
        String currentGame;


        while (true) {
            List<ProcessScanner.ProcessInfo> processes = ProcessScanner.getAllProcessPaths();

            for (ProcessScanner.ProcessInfo p : processes) {
                if (!seenPids.contains(p.pid)) {
                    seenPids.add(p.pid);

                    System.out.println("checking....");
                    if (ProcessFolderVerifier.matches(Path.of(p.path))) {
                        String foundGame = ProcessGameGetter.getGameName(p.path);

                        if (!seenGameNames.contains(foundGame)) {
                            gameName = foundGame;
                            seenGameNames.add(gameName);
                            validGames.add(gameName);

                            boolean dbExists = GamesDAOHandler.itExistsInDB(ProcessGameGetter.getGameName(p.path));
                            if (!dbExists) {
                                System.out.println(GREEN_BOLD + " Game Found: " + gameName + RESET);
                                System.out.println(GREEN_BOLD + " Path: " + p.path + RESET);
                                System.out.println("PID: " + GREEN_BOLD + p.pid + RESET);
                            } else {
                                System.out.println("known game found: " + GREEN_BOLD + gameName + RESET);
                                System.out.println("EXE: " + GREEN_BOLD + p.name + RESET);
                                System.out.println("PID: " + GREEN_BOLD + p.pid + RESET);
                                System.out.println(GREEN_BOLD + " Path: " + p.path + RESET);
                            }


                            gamesDB.add(gameName);


                            if (gamePid.isEmpty()) {
                                gamePid.put(p.pid, gameName);
                                pidsList.add(p.pid);
                                currentGame = gameName;
                                gameStopWatches.put(p.pid, StopWatch.createStarted());
                            } else {
                                pidsList.add(p.pid);
                                System.out.println("there is another gaming running, please close the one that you just opened");
                                while (true) {

                                    boolean oldGameIsRunning = ProcessScanner.isRunning(pidsList.getFirst());
                                    boolean newGameIsRunning = ProcessScanner.isRunning(p.pid);

                                    if (!newGameIsRunning) {
                                        System.out.println("the new one has been closed");
                                        break;
                                    }
                                    if (!oldGameIsRunning) {
                                        System.out.println("the old one has been closed");
                                        currentGame = gameName;
                                        gamePid.remove(pidsList.getFirst());
                                        gamePid.put(p.pid, gameName);
                                        gameStopWatches.remove(pidsList.getFirst());
                                        gameStopWatches.put(p.pid, StopWatch.createStarted());
                                        System.out.println("Switched to: " + currentGame);
                                        break;
                                    }
                                }
                            }

                        }
                    }
                }
            }

            Iterator<Map.Entry<Integer, String>> iterator = gamePid.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                int pid = entry.getKey();
                System.out.println("PID: " + GREEN_BOLD + pid + RESET);
                String gameNameMap = entry.getValue();

                boolean isRunning = ProcessScanner.isRunning(pid);

                String status = "";
                if (!isRunning) {
                    StopWatch sw = gameStopWatches.get(pid);
                    if (sw != null && sw.isStarted()) {
                        sw.stop();
                    }
                    long sessionTime = sw.getTime(TimeUnit.SECONDS);
                    System.out.println(sessionTime + " seconds spent playing " + GREEN_BOLD + gameNameMap + RESET);
                    gameStopWatches.remove(pid);

                    System.out.println("game has been closed: " + GREEN_BOLD + gameNameMap + RESET);

                    boolean dbExists = GamesDAOHandler.itExistsInDB(gameNameMap);

                    System.out.println(dbExists);

                    if (dbExists) {
                        long oldTime = GamesDAOHandler.getTotalTimeSpent(gameNameMap);

                        long newTime = oldTime + sessionTime;

                        System.out.println("old time " + GREEN_BOLD + oldTime + RESET + " new time " + GREEN_BOLD + newTime + RESET);

                        if (!GamesDAOHandler.getGameStatus(gameNameMap).equals("Finished")) {
                            System.out.println("Have beaten the game? [y/n]: ");
                            String choice = scanner.nextLine();
                            while (true) {
                                if (Character.toLowerCase(choice.charAt(0)) == 'y') {
                                    status = "Finished";
                                    break;
                                } else if (Character.toLowerCase(choice.charAt(0)) == 'n') {
                                    status = "Playing";
                                    break;
                                } else {
                                    System.out.println("only y or n please: ");
                                }
                            }
                        }

                        System.out.println(gameNameMap);
                        System.out.println(status);
                        System.out.println(newTime);

                        GamesDAOHandler.updateDB(newTime, status, gameNameMap);

                    } else {
                        System.out.println(GREEN_BOLD + gameNameMap + " Genre? " + RESET);
                        genre = scanner.nextLine();

                        System.out.println("You are registering " + GREEN_BOLD + gameNameMap + RESET + " as part of your beaten game collection? [y/n] ");
                        String choice = scanner.nextLine();
                        while (true) {
                            if (choice.equalsIgnoreCase("y")) {
                                status = "Finished";
                                System.out.println("Answer the following questions to help you track your progress: ");
                                System.out.println(GREEN_BOLD + "if you dont remember please leave them blank" + RESET);
                                while (true) {
                                    try {
                                        System.out.println("When did you start playing " + GREEN_BOLD + gameNameMap + RESET + "? (yyyy-mm-dd): ");
                                        String startDate = scanner.nextLine();

                                        System.out.println("When did you finish playing " + GREEN_BOLD + gameNameMap + RESET + "? (yyyy-mm-dd): ");
                                        String finishDate = scanner.nextLine();
                                        if (startDate.isEmpty()) {
                                            parsedDateStart = null;
                                        } else if (finishDate.isEmpty()) {
                                            parsedDateFinish = null;
                                        } else {
                                            parsedDateStart = new Date(formatter.parse(startDate).getTime());
                                            parsedDateFinish = new Date(formatter.parse(finishDate).getTime());
                                        }
                                        break;
                                    } catch (Exception e) {
                                        System.out.println("please enter a valid date");
                                    }
                                }

                                Game game = new Game(gameNameMap, genre, status, parsedDateStart, parsedDateFinish, sessionTime, Game.getUserID());
                                GamesDAOHandler.insert(game);
                                break;
                            }
                            if (choice.equalsIgnoreCase("n")) {

                                parsedDateStart = today;
                                parsedDateFinish = null;

                                Game game = new Game(gameNameMap, genre, status, parsedDateStart, parsedDateFinish, sessionTime, Game.getUserID());

                                GamesDAOHandler.insert(game);
                                break;

                            }
                            System.out.println("only y or n please: ");
                        }
                    }
                    iterator.remove();
                    seenPids.remove(pid);
                    seenGameNames.remove(gameNameMap);
                    validGames.remove(gameNameMap);

                }

            }

            gamesDB.clear();

            gameName = "";

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}