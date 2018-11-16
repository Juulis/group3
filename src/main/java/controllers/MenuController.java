package controllers;

import app.Server;
import javafx.fxml.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MenuController {


    @FXML
    private AnchorPane menuPane;

    @FXML
    public void startGame() throws IOException {
        Pane playerInputPane = FXMLLoader.load(getClass().getResource("/playerInput/playerInput.fxml"));
        menuPane.getChildren().setAll(playerInputPane);
        Server.getInstance();
    }

    @FXML
    public void viewHighscore() throws IOException {
        AnchorPane highscorePane = FXMLLoader.load(getClass().getResource("/highscore/highscore.fxml"));
        menuPane.getChildren().setAll(highscorePane);
    }

    @FXML
    public void quitGame() {
        System.exit(0);
    }



}
