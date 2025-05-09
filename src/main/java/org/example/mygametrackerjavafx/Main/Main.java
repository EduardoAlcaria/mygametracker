package org.example.mygametrackerjavafx.Main;

import org.apache.commons.lang3.time.StopWatch;
import org.example.mygametrackerjavafx.FolderFinder.FolderUserInput;
import org.example.mygametrackerjavafx.Model.Game;
import org.example.mygametrackerjavafx.Model.User;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessFolderVerifier;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessGameGetter;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessScanner;
import org.example.mygametrackerjavafx.connectionDAO.GamesDAOHandler;
import org.example.mygametrackerjavafx.connectionDAO.UserDAOHandler;

import java.io.IOException;
import java.nio.file.Path;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Main {
    private static List<String> gamesDB = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        FolderUserInput.UserCustomPathWriter();


        System.out.println("[1] Login or [2] register account? ");
        int choiceLogin = scanner.nextInt();
        scanner.nextLine();
        if (choiceLogin == 1) {
            System.out.println("Login system");
            System.out.println("----------------------------");

            System.out.println("User name: ");
            String userName = scanner.nextLine();


            System.out.println("Password: ");
            String password = scanner.nextLine();

            User user = new User(userName, password);

            UserDAOHandler.createAccount(userName);
        }
        if (choiceLogin == 2){
            System.out.println("Register system");
            System.out.println("----------------------------");

            System.out.println("Register User name: ");
            String userName = scanner.next();

            System.out.println("Register Password: ");

            String password = scanner.next();
        }



        final String GREEN_BOLD = "\033[1;32m";
        final String RESET = "\033[0m";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");



        Set<Integer> seenPids = new HashSet<>();
        Map<Integer, StopWatch> gameStopWatches = new HashMap<>();
        Map<Integer, String> gamePid = new HashMap<>();

        LocalDate now = LocalDate.now();

        Date today = Date.valueOf(now);

        String gameName = "";

        Date parsedDateStart = null;
        Date parsedDateFinish = null;

        String genre = "";

        while (true) {
            List<ProcessScanner.ProcessInfo> processes = ProcessScanner.getAllProcessPaths();

            for (ProcessScanner.ProcessInfo p : processes) {
                if (!seenPids.contains(p.pid)) {
                    seenPids.add(p.pid);

                    if (ProcessFolderVerifier.matches(Path.of(p.path)) && !gameName.equals(ProcessGameGetter.getGameName(p.path))) {
                        gameName = ProcessGameGetter.getGameName(p.path);
                        boolean dbExists = GamesDAOHandler.itExistsInDB(ProcessGameGetter.getGameName(p.path));
                        if (!dbExists) {
                            System.out.println(ProcessGameGetter.getGameName(p.path));
                            System.out.println(GREEN_BOLD + " Game Found: " + gameName + RESET);
                            System.out.println(GREEN_BOLD + " Path: " + p.path + RESET);
                            gamesDB.add(gameName);
                            gamePid.put(p.pid, gameName);
                            gameStopWatches.put(p.pid, StopWatch.createStarted());
                            break;
                        } else {
                            System.out.println("known game found: " + GREEN_BOLD + gameName + RESET);
                            gamePid.put(p.pid, gameName);
                            gameStopWatches.put(p.pid, StopWatch.createStarted());
                            break;
                        }
                    }
                }

            }

            Iterator<Map.Entry<Integer, String>> iterator = gamePid.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                int pid = entry.getKey();
                String gameNameMap = entry.getValue();

                boolean isRunning = ProcessScanner.isRunning(pid);

                String status = "playing";
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

                    if (dbExists) {
                        long oldTime = GamesDAOHandler.getTotalTimeSpent(gameNameMap);

                        long newTime = oldTime + sessionTime;

                        System.out.println("old time " + GREEN_BOLD + oldTime + RESET + " new time " + GREEN_BOLD + newTime + RESET);

                        System.out.println("Have beaten the game? [y/n]: ");
                        String choice = scanner.nextLine();

                        while (true) {
                            if (Character.toLowerCase(choice.charAt(0)) == 'y') {
                                status = "Finished";
                                parsedDateFinish = today;
                                break;
                            } else if (Character.toLowerCase(choice.charAt(0)) == 'n') {
                                break;
                            } else {
                                System.out.println("only y or n please: ");
                            }
                        }
                        System.out.println(gameNameMap);
                        GamesDAOHandler.updateDB(newTime, status, gameNameMap);
                    } else {
                        if (!dbExists) {
                            System.out.println(GREEN_BOLD + gameNameMap + " Genre? " + RESET);
                            genre = scanner.nextLine();

                            System.out.println("You are registering " + GREEN_BOLD + gameNameMap + RESET + " as part of your beaten game collection? [y/n] ");
                            String choice = scanner.nextLine();
                            while (true) {
                                if (choice.equalsIgnoreCase("y")) {
                                    status = "Finished";
                                    System.out.println("Answer the following questions to help you track your progress: ");
                                    System.out.println("if you dont remember please leave them blank");
                                    while (true){
                                        try {
                                            System.out.println("When did you start playing " + GREEN_BOLD + gameNameMap + RESET + "? (yyyy-mm-dd): ");
                                            String startDate = scanner.nextLine();

                                            System.out.println("When did you finish playing " + GREEN_BOLD + gameNameMap + RESET + "? (yyyy-mm-dd): ");
                                            String finishDate = scanner.nextLine();
                                            parsedDateStart = new Date(formatter.parse(startDate).getTime());
                                            parsedDateFinish = new Date(formatter.parse(finishDate).getTime());
                                            break;
                                        }catch (Exception e){
                                            System.out.println("please enter a valid date");
                                        }
                                    }

                                    Game game = new Game(gameNameMap, genre, status, parsedDateStart, parsedDateFinish, sessionTime);
                                    GamesDAOHandler.insert(game);
                                    break;
                                }
                                if (choice.equalsIgnoreCase("n")) {

                                    parsedDateStart = today;
                                    parsedDateFinish = null;

                                    Game game = new Game(gameNameMap, genre, status, parsedDateStart, parsedDateFinish, sessionTime);

                                    GamesDAOHandler.insert(game);
                                    break;

                                }
                                System.out.println("only y or n please: ");
                            }
                        }
                    }
                    iterator.remove();
                    seenPids.remove(pid);
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