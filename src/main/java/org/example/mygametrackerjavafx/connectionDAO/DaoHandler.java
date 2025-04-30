package org.example.mygametrackerjavafx.connectionDAO;

import org.example.mygametrackerjavafx.Model.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.mygametrackerjavafx.connectionDAO.ConnectionDAO.getConnection;

public class DaoHandler {
    private static GamesDAO gamesDAO = new GamesDAO();

    public static void insert(Game game) throws SQLException {
        if (game.getGameName() != null && game.getGameGenre() != null && game.getGameStatus() != null && game.getStartPlayingOn() != null && game.getFinishedPlayingOn() != null) {
            gamesDAO.insert(game);
        }
    }

    public static List<Game> getAllGames() throws SQLException {
        List<Game> games = new ArrayList<>();
        String query = "SELECT * FROM games";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Game game = new Game(
                        rs.getString("game_title"),
                        rs.getString("game_genre"),
                        rs.getString("game_status"),
                        rs.getDate("start_playing_on"),
                        rs.getDate("finished_playing_on"),
                        rs.getLong("time_spent")
                );
                games.add(game);
            }
        }
        return games;
    }



    public static void updateDB(Long timeUpdated, String gameStatus, String gameName) throws SQLException {

        if (gameName == null || gameStatus == null || timeUpdated == null) {
            throw new IllegalArgumentException("gameName is null");
        }
        String sql = "SELECT time_spent FROM games WHERE game_title = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    timeUpdated += rs.getLong("time_spent");
                } else {
                    timeUpdated = 0L;
                }
            }

            gamesDAO.updateDB(timeUpdated, gameStatus, gameName);
        }
    }

    public static boolean itExistsInDB(String gameTitle) throws SQLException {

        if (gameTitle == null) throw new IllegalArgumentException("gameTitle is null");

        String sql = "SELECT game_title FROM games WHERE game_title = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameTitle);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
