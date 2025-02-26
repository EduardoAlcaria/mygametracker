package com.eduardoalcaria.mygametracker.model;


import com.eduardoalcaria.mygametracker.DAO.ConnectionDAO;

import java.sql.*;
import java.time.LocalDate;

public class Game {
    private String gameName;
    private String gameGenre;
    private String status;
    private LocalDate startPlayingOn;
    private LocalDate finishedPlayingOn;

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

    public LocalDate getStartPlayingOn() {
        return startPlayingOn;
    }

    public void setStartPlayingOn(LocalDate startPlayingOn) {
        this.startPlayingOn = startPlayingOn;
    }

    public LocalDate getFinishedPlayingOn() {
        return finishedPlayingOn;
    }

    public void setFinishedPlayingOn(LocalDate finishedPlayingOn) {
        this.finishedPlayingOn = finishedPlayingOn;
    }
}

