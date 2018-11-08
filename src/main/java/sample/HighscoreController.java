package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import java.io.IOException;


public class HighscoreController {

    @FXML
    private AnchorPane highscorePane;
    @FXML
    private Label highscoreLabel;

    public void initialize() {
        highscoreLabel.setText("");
    }

    public void goBack() throws IOException {
        AnchorPane menuPane = FXMLLoader.load(getClass().getResource("/menu/menu.fxml"));
        highscorePane.getChildren().setAll(menuPane);
    }
}
