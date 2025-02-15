package com.eduardoalcaria.mygametracker.model;

public class Game {
    private String gameName;
    private String gameGender;
    private int startPlayingOn;
    private int finishedPlayingOn;

    public void print(){
        System.out.println("Title: " + this.gameName);
        System.out.println("Gen: " + this.gameGender);
    }

    public Game(String gameName, String gameGender) {
        this.gameName = gameName;
        this.gameGender = gameGender;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameGender() {
        return gameGender;
    }

    public void setGameGender(String gameGender) {
        this.gameGender = gameGender;
    }

    public int getStartPlayingOn() {
        return startPlayingOn;
    }

    public void setStartPlayingOn(int startPlayingOn) {
        this.startPlayingOn = startPlayingOn;
    }

    public int getFinishedPlayingOn() {
        return finishedPlayingOn;
    }

    public void setFinishedPlayingOn(int finishedPlayingOn) {
        this.finishedPlayingOn = finishedPlayingOn;
    }
}
