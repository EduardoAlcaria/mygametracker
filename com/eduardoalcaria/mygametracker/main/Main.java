package com.eduardoalcaria.mygametracker.main;

import com.eduardoalcaria.mygametracker.DAO.ConnectionDAO;
import com.eduardoalcaria.mygametracker.model.Game;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Game game1 = new Game();


        System.out.print("Enter the game name: ");
        String gameName = scanner.nextLine();

        game1.setGameName(gameName);

        System.out.print("Enter the game genre: ");
        String gameGenre = scanner.nextLine();

        game1.setGameGenre(gameGenre);

        System.out.print("Have you finished playing " + game1.getGameName() + " (y/n)? ");
        char playingStatus = scanner.nextLine().charAt(0);
        playingStatus = charVerification(playingStatus);

        switch (playingStatus){
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
                LocalDate date = LocalDate.parse(startPlayingOn, formatter);
                game1.setStartPlayingOn(date);
                break;

            case 'n':
                break;
        }

        if (game1.getStatus() != "Playing"){
            System.out.print("Do you remember the date that you ended playing (y/n)? ");
            char doYouRememberEndedPlayingDate = Character.toLowerCase(scanner.next().charAt(0));
            doYouRememberEndedPlayingDate = charVerification(doYouRememberEndedPlayingDate);

            scanner.nextLine();


            switch (doYouRememberEndedPlayingDate) {
                case 'y':
                    System.out.print("Enter the finished playing date (dd/mm/yyyy): ");
                    String finishedPlayingOn = scanner.nextLine();
                    LocalDate date = LocalDate.parse(finishedPlayingOn, formatter);
                    game1.setFinishedPlayingOn(date);
                    break;

                case 'n':
                    break;
            }
        }
        game1.print();
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/mygametracker_localhost"; // Change this
        String user = "root"; // Change this
        String password = ""; // Change this

        // SQL Insert Statement
        String sql = "INSERT INTO games (game_title, game_genre, game_status, start_playing_on, finished_playing_on)" +
                " VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Date dateStart;
            Date dateFinished;
            if (game1.getStartPlayingOn() != null){
                 dateStart = Date.valueOf(game1.getStartPlayingOn());
            }else
                dateStart = null;
            if (game1.getFinishedPlayingOn() != null){
                dateFinished = Date.valueOf(game1.getFinishedPlayingOn());
            }else
                dateFinished = null;
            // Setting values for the placeholders (?)
            stmt.setString(1, game1.getGameName());
            stmt.setString(2, game1.getGameGenre());
            stmt.setString(3, game1.getStatus());
            stmt.setDate(4, dateStart);
            stmt.setDate(5, dateFinished);
            // Execute the insert
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
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
