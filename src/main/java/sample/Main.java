package sample;

import models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/menu/menu.fxml"));
        primaryStage.setTitle("This is not a chess inspired game!!");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("1. for FX\n2. for Console");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if (choice == 1) {
            launch(args);
        } else if (choice == 2) {
            GameEngine game = new GameEngine();
            game.startGame();
        }
    }
}
