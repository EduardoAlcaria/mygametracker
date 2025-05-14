package org.example.mygametrackerjavafx.connectionDAO;

import org.example.mygametrackerjavafx.Model.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.mygametrackerjavafx.connectionDAO.ConnectionDAO.getConnection;

public class GamesDAO {
    protected static void insert(Game game) {
        String sql = "INSERT INTO games (game_title, game_genre, game_status, start_playing_on, finished_playing_on, time_spent, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, game.getGameName());
            stmt.setString(2, game.getGameGenre());
            stmt.setString(3, game.getGameStatus());
            stmt.setDate(4, game.getStartPlayingOn());
            stmt.setDate(5, game.getFinishedPlayingOn());
            stmt.setLong(6, game.getTimeSpent());
            stmt.setInt(7, Game.getUserID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("changes has been applied " + rowsUpdated + " rows");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    protected static List<Game> getAllGames() throws SQLException {
        System.out.println("getting all games from the database ...");
        List<Game> games = new ArrayList<>();
        String query = "SELECT * FROM games WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, Game.getUserID());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Game game = new Game(
                        rs.getString("game_title"),
                        rs.getString("game_genre"),
                        rs.getString("game_status"),
                        rs.getDate("start_playing_on"),
                        rs.getDate("finished_playing_on"),
                        rs.getLong("time_spent"),
                        rs.getInt("user_id")
                );
                games.add(game);
            }
        }
        return games;
    }

    protected static void updateDB(Long timeUpdated, String gameStatus, String gameName) throws SQLException {

        String sqlUpdate = "UPDATE games SET time_spent = ?, game_status = ? WHERE game_title = ? AND user_id = ?";


        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {


            stmt.setLong(1, timeUpdated);
            stmt.setString(2, gameStatus);
            stmt.setString(3, gameName);
            stmt.setInt(4, Game.getUserID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("changes has been applied " + rowsUpdated + " rows");
            } else {
                System.out.println("no changes has been applied");
            }


        } catch (SQLException e) {
            throw new SQLException("failed to update the database" + e);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    protected static boolean itExistsInDB(String gameTitle) throws SQLException {
        if (gameTitle == null) throw new IllegalArgumentException("gameTitle is null");

        String sql = "SELECT game_title FROM games WHERE game_title = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameTitle);
            stmt.setInt(2, Game.getUserID());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    protected static Long getTotalTimeSpent(String gameName) throws SQLException {
        System.out.println("getting the total time spent in the database ...");
        String query = "SELECT time_spent FROM games WHERE game_title = ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, gameName);
            pstmt.setInt(2, Game.getUserID());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("time_spent");
                } else {
                    return 0L;
                }
            }
        }
    }
    protected static String getGameStatus(String gameName) throws SQLException {
        String sqlQuery = "SELECT game_status FROM games WHERE game_title = ?";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {
            stmt.setString(1, gameName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("game_status");
            } else {
                throw new RuntimeException("game not found in the database");
            }
        }
    }
}