package com.eduardoalcaria.mygametracker.main;

import com.eduardoalcaria.mygametracker.database.ConnectionDAO;
import com.eduardoalcaria.mygametracker.model.Game;

import java.util.Scanner;

public class Caller {
    public static void main(String[] args) {
        ConnectionDAO conn = new ConnectionDAO();

        System.out.println(conn.connection());

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the game title: ");

        String gameTitleScan = scanner.nextLine();

        System.out.print("Enter the game gender: ");

        String gameGenScan = scanner.nextLine();


        Game game = new Game(gameTitleScan, gameGenScan);
        game.print();
    }
}
