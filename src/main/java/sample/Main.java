package sample;
/*
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/menu/menu.fxml"));
        primaryStage.setTitle("This is not a chess inspired game!!");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.show();
    }

   public static void main(String[] args) {
        launch(args);
    }
}*/

import models.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Deck deck = new Deck();
        deck.getCardsFromJSON();
        deck.getCards();
    }
}
