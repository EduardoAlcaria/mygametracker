package com.eduardoalcaria.mygametracker.main;

import com.eduardoalcaria.mygametracker.model.Game;

import java.util.Scanner;

public class Caller {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the game title: ");

        String gameTitleScan = scanner.nextLine();

        System.out.print("Enter the game gender: ");

        String gameGenScan = scanner.nextLine();


        Game game = new Game(gameTitleScan, gameGenScan);
        game.print();
    }
}
