package org.example.mygametrackerjavafx.Model;

import java.sql.Date;

public class Game {
    private String gameName;
    private String gameGenre;
    private String gameStatus;
    private Date startPlayingOn;
    private Date finishedPlayingOn;
    private Long timeSpent;


    public Game(String gameName, String gameGenre, String gameStatus, Date startPlayingOn, Date finishedPlayingOn, Long timeSpent) {
        this.gameName = gameName;
        this.gameGenre = gameGenre;
        this.gameStatus = gameStatus;
        this.startPlayingOn = startPlayingOn;
        this.finishedPlayingOn = finishedPlayingOn;
        this.timeSpent = timeSpent;
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

