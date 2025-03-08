package org.example.mygametrackerjavafx.Main;

import org.example.mygametrackerjavafx.connectionDAO.InsertGames;
import org.example.mygametrackerjavafx.Model.Game;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

import static org.example.mygametrackerjavafx.ProcessTracker.ProcessTracker.isGameRunning;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Game game1 = new Game();

        InsertGames insertGames = new InsertGames();

        String gameNameProcess = "Opera GX";

        if (isGameRunning(gameNameProcess)){
            System.out.println("the game is running: " + gameNameProcess);
        }else{
            System.out.println("the game is not running");
        }

        while (true) {
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
}
