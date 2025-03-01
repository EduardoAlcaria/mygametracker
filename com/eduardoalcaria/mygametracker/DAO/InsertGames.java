package com.eduardoalcaria.mygametracker.DAO;

import java.sql.*;

public class InsertGames {
    public void insert(String gameTitle, String gameGenre, String gameStatus, Date startPlayingOn, Date finishedPlayingOn) {
        String sql = "INSERT INTO games (game_title, game_genre, game_status, start_playing_on, finished_playing_on) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameTitle);
            stmt.setString(2, gameGenre);
            stmt.setString(3, gameStatus);
            stmt.setDate(4, startPlayingOn);
            stmt.setDate(5, finishedPlayingOn);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("changes has been applied " + rowsUpdated + " rows");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void print() {
        String sql = "SELECT * FROM games";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()){
                int id = rs.getInt("game_id");
                String game_title = rs.getString("game_title");
                String game_genre = rs.getString("game_genre");
                String game_status = rs.getString("game_status");
                Date start_playing_on = rs.getDate("start_playing_on");
                Date finished_playing_on = rs.getDate("finished_playing_on");
                System.out.println("     id " + id);
                System.out.println("     title " + game_title);
                System.out.println("     genre " + game_genre);
                System.out.println("     status " + game_status);
                System.out.println("     started playing on " + start_playing_on);
                System.out.println("     finished playing on " + finished_playing_on);

                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}