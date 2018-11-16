package controllers;

import app.Server;
import javafx.fxml.*;
import javafx.scene.layout.*;

import java.io.IOException;

public class MenuController {

    @FXML
    private Pane menuPane;

    public void startGame() throws IOException {
        Pane playerInputPane = FXMLLoader.load(getClass().getResource("/playerInput/playerInput.fxml"));
        menuPane.getChildren().setAll(playerInputPane);

    }

    public void viewHighscore() throws IOException {
        Pane highscorePane = FXMLLoader.load(getClass().getResource("/highscore/highscore.fxml"));
        menuPane.getChildren().setAll(highscorePane);
    }

    public void quitGame() {
        System.exit(0);
    }
}
