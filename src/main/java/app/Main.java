package app;

import controllers.TableViewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.GameEngine;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/menu/menu.fxml"));
        primaryStage.setTitle("This is not a chess inspired game!!");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setResizable(false);
        primaryStage.show();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tableview/tableview.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TableViewController tvc = loader.getController();
        Server.getInstance().setTvc(tvc,primaryStage);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("1. for FX\n2. for Console");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if (choice == 1) {
            launch(args);
        } else if (choice == 2) {
            GameEngine game = new GameEngine();
            game.startGame("console");
        }
    }
}
