package org.example.mygametrackerjavafx.connectionDAO;

import org.example.mygametrackerjavafx.Model.Game;

public class DaoHandler {
    public static void insert(Game game){
        if (game.getGameName() != null && game.getGameGenre() != null && game.getGameStatus() != null && game.getStartPlayingOn() != null && game.getFinishedPlayingOn() != null) {
            GamesDAO gamesDAO = new GamesDAO();
            gamesDAO.insert(game);
        }
    }

    public static void findAll(){
        GamesDAO gamesDAO = new GamesDAO();
        try {
            gamesDAO.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void updateDB(Game game){
        if (game.getGameName() != null && game.getGameGenre() != null && game.getGameStatus() != null && game.getStartPlayingOn() != null && game.getFinishedPlayingOn() != null) {
            GamesDAO gamesDAO = new GamesDAO();
            try {
                gamesDAO.updateDB(game);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
