package org.example.mygametrackerjavafx.model;

import java.sql.Date;

public class Game {
    private String gameName;
    private String gameGenre;
    private String gameStatus;
    private Date startPlayingOn;
    private Date finishedPlayingOn;
    private Long timeSpent;
    private static int userID;


    public Game(String gameName, String gameGenre, String gameStatus, Date startPlayingOn, Date finishedPlayingOn, Long timeSpent, int userId) {
        this.gameName = gameName;
        this.gameGenre = gameGenre;
        this.gameStatus = gameStatus;
        this.startPlayingOn = startPlayingOn;
        this.finishedPlayingOn = finishedPlayingOn;
        this.timeSpent = timeSpent;
        Game.userID = userId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameGenre(String gameGenre) {
        this.gameGenre = gameGenre;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setStartPlayingOn(Date startPlayingOn) {
        this.startPlayingOn = startPlayingOn;
    }

    public void setFinishedPlayingOn(Date finishedPlayingOn) {
        this.finishedPlayingOn = finishedPlayingOn;
    }

    public void setTimeSpent(Long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        Game.userID = userID;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameGenre() {
        return gameGenre;
    }

    public String getGameStatus() {
        return gameStatus;
    }


    public Date getStartPlayingOn() {
        return startPlayingOn;
    }

    public Date getFinishedPlayingOn() {
        return finishedPlayingOn;
    }

    public Long getTimeSpent() {
        return timeSpent;
    }
}

