package org.example.mygametrackerjavafx.View;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.mygametrackerjavafx.connectionDAO.GamesDAO;

public class GameTrackerUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("mygametracker");
        GamesDAO insertGames = new GamesDAO();

        Text text = new Text("My Game Tracker V:0.0.1");
        text.setFont(Font.font("Arial", 20)); // Set font and size
        text.setFill(Color.PURPLE); // Change text color

        Label nameLabel = new Label("Game Name:");
        TextField nameField = new TextField();

        Label genreLabel = new Label("Game Genre:");
        TextField genreField = new TextField();

        Label statusLabel = new Label("Finished? (y/n):");
        TextField statusField = new TextField();

        Button addButton = new Button("Add Game");
        Button showGamesButton = new Button("Show Games");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(text,nameLabel, nameField, genreLabel, genreField, statusLabel, statusField, addButton, showGamesButton);

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String genre = genreField.getText();
            String status = statusField.getText().toLowerCase().equals("y") ? "Finished" : "Playing";

            System.out.println("Added game: " + name + " | Genre: " + genre + " | Status: " + status);
        });

        showGamesButton.setOnAction(e -> {
            System.out.println("Showing games...");

        });

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
