package org.example.mygametrackerjavafx.connectionDAO;

import com.sun.jna.platform.win32.WinNT;
import com.sun.source.tree.BreakTree;
import org.example.mygametrackerjavafx.Model.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GamesDAO {
    public void insert(Game game) {
        String sql = "INSERT INTO games (game_title, game_genre, game_status, start_playing_on, finished_playing_on, time_spent) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, game.getGameName());
            stmt.setString(2, game.getGameGenre());
            stmt.setString(3, game.getGameStatus());
            stmt.setDate(4, game.getStartPlayingOn());
            stmt.setDate(5, game.getFinishedPlayingOn());
            stmt.setLong(6, game.getTimeSpent());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("changes has been applied " + rowsUpdated + " rows");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findAll() {
        String sql = "SELECT * FROM games";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("game_id");
                String game_title = rs.getString("game_title");
                String game_genre = rs.getString("game_genre");
                String game_status = rs.getString("game_status");
                Date start_playing_on = rs.getDate("start_playing_on");
                Date finished_playing_on = rs.getDate("finished_playing_on");
                long time_spent = rs.getLong("time_spent");
                System.out.println("     id " + id);
                System.out.println("     title " + game_title);
                System.out.println("     genre " + game_genre);
                System.out.println("     status " + game_status);
                System.out.println("     started playing on " + start_playing_on);
                System.out.println("     finished playing on " + finished_playing_on);
                System.out.println("     time spent " + time_spent + " seconds");

                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean doesItExistsOnDB(String gameTitle) throws SQLException {
        String sql = "SELECT 1 game_title FROM games WHERE game_title = ?";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, gameTitle);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void updateDB(Game game) throws SQLException {

        if (!doesItExistsOnDB(game.getGameName())) {
            insert(game);
            return;
        }

        String sqlUpdate = "UPDATE games SET game_genre = ?, " +
                "game_status = ?, start_playing_on = ?, finished_playing_on " +
                "= ?, time_spent = ? WHERE game_title = ?";

        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

            stmt.setString(1, game.getGameGenre());
            stmt.setString(2, game.getGameStatus());
            stmt.setDate(3, game.getStartPlayingOn());
            stmt.setDate(4, game.getFinishedPlayingOn());
            stmt.setLong(5, game.getTimeSpent());
            stmt.setString(6, game.getGameName());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("changes has been applied " + rowsUpdated + " rows");
            } else {
                System.out.println("no changes has been applied");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


}