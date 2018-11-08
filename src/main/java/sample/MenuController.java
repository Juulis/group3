package sample;

import javafx.fxml.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MenuController {

    @FXML
    private AnchorPane menuPane;

    public void startGame() {
        System.out.println("START GAME PRESSED");
    }

    public void viewHighscore() throws IOException {
        AnchorPane highscorePane = FXMLLoader.load(getClass().getResource("/highscore/highscore.fxml"));
        menuPane.getChildren().setAll(highscorePane);
    }

    public void quitGame() {
        System.exit(0);
    }
}
