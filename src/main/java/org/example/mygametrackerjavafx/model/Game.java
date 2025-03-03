package org.example.mygametrackerjavafx.model;

import java.sql.Date;

public class Game {
    private String gameName;
    private String gameGenre;
    private String status;
    private Date startPlayingOn;
    private Date finishedPlayingOn;

    public void print() {
        System.out.println("Title: " + this.gameName);
        System.out.println("Gen: " + this.gameGenre);
        System.out.println("Status: " + this.status);
        System.out.println("Start Playing on: " + this.startPlayingOn);
        System.out.println("Finished Playing on: " + this.finishedPlayingOn);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameGenre() {
        return gameGenre;
    }

    public void setGameGenre(String gameGenre) {
        this.gameGenre = gameGenre;
    }

    public Date getStartPlayingOn() {
        return startPlayingOn;
    }

    public void setStartPlayingOn(Date startPlayingOn) {
        this.startPlayingOn = startPlayingOn;
    }

    public Date getFinishedPlayingOn() {
        return finishedPlayingOn;
    }

    public void setFinishedPlayingOn(Date finishedPlayingOn) {
        this.finishedPlayingOn = finishedPlayingOn;
    }
}

