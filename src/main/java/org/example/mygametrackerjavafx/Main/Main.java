package org.example.mygametrackerjavafx.Main;

import org.apache.commons.lang3.time.StopWatch;
import org.example.mygametrackerjavafx.FolderFinder.FolderUserInput;
import org.example.mygametrackerjavafx.Model.Game;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessFolderVerifier;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessGameGetter;
import org.example.mygametrackerjavafx.ProcessTracker.ProcessScanner;
import org.example.mygametrackerjavafx.connectionDAO.DaoHandler;
import org.example.mygametrackerjavafx.connectionDAO.GamesDAO;

import java.io.IOException;
import java.nio.file.Path;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws IOException {
        FolderUserInput.UserCustomPathWriter();

        final String GREEN_BOLD = "\033[1;32m";
        final String RESET = "\033[0m";

        StopWatch stopWatch = new StopWatch();
        Set<Integer> seenPids = new HashSet<>();

        List<String> gamesDB = new ArrayList<>();
        List<String> gameGenreDB = new ArrayList<>();
        List<String> gameStatusDB = new ArrayList<>();
        List<Date> startPlayingDB = new ArrayList<>();
        List<Date> finishedPlayingDB = new ArrayList<>();
        List<Long> timeSpendPLayingDB = new ArrayList<>();

        String gameName = "";

        boolean isListening = true;
        Scanner scanner = new Scanner(System.in);
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
                        gamesDB.add(gameName);
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
                long section = 0;
                section += stopWatch.getTime(TimeUnit.SECONDS);
                timeSpendPLayingDB.add(section);
            }
            stopWatch.reset();


            for (int i = 0; i < gamesDB.size(); i++) {
                System.out.println(GREEN_BOLD + gamesDB.get(i) + " Genre? " + RESET);
                String genre = scanner.nextLine();

                System.out.println(GREEN_BOLD + gameName + " Status? " + RESET);
                String status = scanner.nextLine();

                System.out.println(GREEN_BOLD + gameName + " Start Date? " + RESET);
                String startDate = scanner.nextLine();

                System.out.println(GREEN_BOLD + gameName + " Finish Date? " + RESET);
                String finishDate = scanner.nextLine();

                gameGenreDB.add(genre);
                gameStatusDB.add(status);
                startPlayingDB.add(Date.valueOf(startDate));
                finishedPlayingDB.add(Date.valueOf(finishDate));

                Game game = new Game(gamesDB.get(i), gameGenreDB.get(i),
                        gameStatusDB.get(i), startPlayingDB.get(i), finishedPlayingDB.get(i), timeSpendPLayingDB.get(i));


                DaoHandler.updateDB(game);




                gamesDB.clear();
                gameStatusDB.clear();
                timeSpendPLayingDB.clear();
                startPlayingDB.clear();
                finishedPlayingDB.clear();




                DaoHandler.findAll();

            }


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
}