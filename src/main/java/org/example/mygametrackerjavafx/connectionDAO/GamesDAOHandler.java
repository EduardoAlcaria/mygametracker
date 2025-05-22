package org.example.mygametrackerjavafx.connectionDAO;

import org.example.mygametrackerjavafx.model.Game;

import java.sql.SQLException;
import java.util.List;


public class GamesDAOHandler {

    public static void insert(Game game) throws Exception {
        if (game == null) {
            throw new IllegalArgumentException("game is null");
        }
        try {
            GamesDAO.insert(game);
        } catch (Exception e) {
            throw new Exception("failed to insert the game into the database" + e);
        }
    }


    public static List<Game> getAllGames() throws SQLException {
        try {
            return GamesDAO.getAllGames();
        }catch (SQLException e){
            throw new SQLException("failed to get all games from the database" + e);
        }
    }

    public static Long getTotalTimeSpent(String gameName) throws SQLException {
        if (gameName == null) throw new IllegalArgumentException("gameName is null");
        try {
            return GamesDAO.getTotalTimeSpent(gameName);
        } catch (SQLException e) {
            throw new SQLException("failed to get the total time spent in the database" + e);
        }
    }

    public static void updateDB(Long timeUpdated, String gameStatus, String gameName) throws SQLException {
        System.out.println("updating the database with the new game data ...");
        if (gameName == null || gameStatus == null || timeUpdated == null) {
            throw new IllegalArgumentException("game name, game status or time updated is null");
        }try {
            GamesDAO.updateDB(timeUpdated, gameStatus, gameName);
        }catch (SQLException e){
            throw new SQLException("failed to update the database" + e);
        }
    }

    public static boolean itExistsInDB(String gameName) throws Exception {
        if (gameName == null) throw new IllegalArgumentException("gameName is null");
        try {
            return GamesDAO.itExistsInDB(gameName);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            throw new Exception("failed to check if the game exists in the database" + e);
        }
        return false;
    }

    public static String getGameStatus(String gameName) throws SQLException {
        if (gameName == null) throw new IllegalArgumentException("gameName is null");
        try {
            return GamesDAO.getGameStatus(gameName);
        }catch (SQLException e){
            throw new SQLException("failed to get the game status from the database" + e);
        }
    }

}
