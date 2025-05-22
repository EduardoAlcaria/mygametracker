package org.example.mygametrackerjavafx.main;

import org.apache.commons.lang3.time.StopWatch;
import org.example.mygametrackerjavafx.folderfinder.FolderUserInput;
import org.example.mygametrackerjavafx.model.Game;
import org.example.mygametrackerjavafx.processtracker.ProcessFolderVerifier;
import org.example.mygametrackerjavafx.processtracker.ProcessGameGetter;
import org.example.mygametrackerjavafx.processtracker.ProcessScanner;
import org.example.mygametrackerjavafx.view.SystemTrayUI;
import org.example.mygametrackerjavafx.connectionDAO.GamesDAOHandler;
import org.example.mygametrackerjavafx.connectionDAO.UserDAOHandler;


import java.nio.file.Path;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Main {

    public static final String STATUS_FINISHED = "Finished";
    public static final String STATUS_PLAYING = "Playing";

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        initializeConfigs();

        final String GREEN_BOLD = "\033[1;32m";
        final String RESET = "\033[0m";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        LocalDate now = LocalDate.now();

        Date today = Date.valueOf(now);

        Date parsedDateStart;
        Date parsedDateFinish = null;

        Set<Integer> seenPids = new HashSet<>();
        Set<String> seenGameNames = new HashSet<>();

        Map<Integer, StopWatch> gameStopWatches = new HashMap<>();
        Map<Integer, String> gamePid = new HashMap<>();

        String gameName;
        String genre;
        String currentGame;

        String status = null;

        List<Integer> pidsList = new ArrayList<>();

        Game game;

        while (true) {
            List<ProcessScanner.ProcessInfo> processes = ProcessScanner.getAllProcessPaths();

            for (ProcessScanner.ProcessInfo p : processes) {

                if (!seenPids.contains(p.pid)) {
                    seenPids.add(p.pid);


                    if (ProcessFolderVerifier.matches(Path.of(p.path))) {
                        String foundGame = ProcessGameGetter.getGameName(p.path);

                        if (!seenGameNames.contains(foundGame)) {
                            gameName = foundGame;
                            seenGameNames.add(foundGame);

                            boolean dbExists = GamesDAOHandler.itExistsInDB(ProcessGameGetter.getGameName(p.path));

                            if (!dbExists) {
                                System.out.println(GREEN_BOLD + "New Game Found: " + gameName + RESET);
                            } else {
                                System.out.println("Known Game Found: " + GREEN_BOLD + gameName + RESET);
                            }
                            System.out.println(GREEN_BOLD + " Path: " + p.path + RESET);
                            System.out.println("PID: " + GREEN_BOLD + p.pid + RESET);




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


                long sessionTime = 0;

                if (!isRunning) {
                    StopWatch sw = gameStopWatches.get(pid);
                    if (sw != null && sw.isStarted()) {
                        sw.stop();
                        sessionTime = sw.getTime(TimeUnit.SECONDS);
                    }

                    System.out.println(sessionTime + " seconds spent playing " + GREEN_BOLD + gameNameMap + RESET);
                    gameStopWatches.remove(pid);

                    System.out.println("game has been closed: " + GREEN_BOLD + gameNameMap + RESET);

                    boolean dbExists = GamesDAOHandler.itExistsInDB(gameNameMap);


                    if (dbExists) {
                        long oldTime = GamesDAOHandler.getTotalTimeSpent(gameNameMap);

                        long newTime = oldTime + sessionTime;

                        System.out.println("old time " + GREEN_BOLD + oldTime + RESET + " new time " + GREEN_BOLD + newTime + RESET);

                        status = STATUS_FINISHED;

                        if (!GamesDAOHandler.getGameStatus(gameNameMap).equals(STATUS_FINISHED)) {

                            char choice = inputHandler("Have beaten the game?" + GREEN_BOLD + gameNameMap + RESET + "?: " );

                            if (choice == 'y') {
                                status = STATUS_FINISHED;
                            }else {
                                status = STATUS_PLAYING;
                            }
                        }

                        System.out.println(gameNameMap);
                        System.out.println(status);
                        System.out.println(newTime);

                        GamesDAOHandler.updateDB(newTime, status, gameNameMap);

                    } else {

                        System.out.println(GREEN_BOLD + gameNameMap + " Genre? " + RESET);
                        genre = SCANNER.nextLine();

                        char choice = inputHandler("Are you registering this game as part of you beaten games" +
                                "collection? " + GREEN_BOLD + gameNameMap + RESET + "?: ");

                        if (choice == 'y') {

                            status = STATUS_FINISHED;

                            System.out.println("Answer the following questions to help you track your progress: ");
                            System.out.println(GREEN_BOLD + "if you dont remember please leave them blank" + RESET);


                            String startDate = inputDateHandler("When did you start playing " + GREEN_BOLD + gameNameMap + RESET + "? (yyyy-mm-dd): ");

                            String finishDate = inputDateHandler("When did you finish playing " + GREEN_BOLD + gameNameMap + RESET + "? (yyyy-mm-dd): ");

                            parsedDateStart = new Date(formatter.parse(startDate).getTime());

                            parsedDateFinish = new Date(formatter.parse(finishDate).getTime());

                        }else {

                            parsedDateStart = today;

                        }
                        game = new Game(gameNameMap, genre, status, parsedDateStart, parsedDateFinish, sessionTime, Game.getUserID());
                        GamesDAOHandler.insert(game);
                    }

                    iterator.remove();
                    seenPids.clear();
                    gamePid.clear();
                    seenGameNames.clear();
                    pidsList.clear();


                }

            }

            gameName = "";

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static char inputHandler(String msg){
        char choice;

        while (true) {
            System.out.println(msg +" [y/n]: ");

            choice = SCANNER.nextLine().charAt(0);

            choice = Character.toLowerCase(choice);


            if (choice == 'y' || choice == 'n') {
               break;
            } else {
                System.out.println("only y or n please: ");
            }
        }

        return choice;
    }

    public static String inputDateHandler(String msg){
        String date;

        while (true) {
            System.out.println(msg + " (yyyy-mm-dd): ");

            date = SCANNER.nextLine();

            if (!date.isEmpty()) {
                break;
            } else {
                System.out.println("please enter a valid date");
            }
        }
        return date;
    }

    public static void initializeConfigs() throws Exception {
        SystemTrayUI.startUI();

        if (!SystemTrayUI.isRemembered()) {
            System.out.println("Please log in");
            while (SystemTrayUI.getCurrentUser() == null) {
                Thread.sleep(3000);
            }
        }
        System.out.println(SystemTrayUI.getCurrentUser());

        int userID = UserDAOHandler.getUserID(SystemTrayUI.getCurrentUser());

        Game.setUserID(userID);

        FolderUserInput.UserCustomPathWriter();
    }

}