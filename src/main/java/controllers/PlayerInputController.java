package controllers;

import app.Server;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class PlayerInputController {
    @FXML
    private Pane playerInputPane;
    @FXML
    private TextField player1Name, player2Name;

    @FXML
    public void saveNames(){
        String name1 = player1Name.getText();
        String name2 = player2Name.getText();
        Server.getInstance().setPlayerNames( name1, name2);
    }

    @FXML
    public void startGame() throws IOException {
        saveNames();
        Pane tableViewPane = FXMLLoader.load(getClass().getResource("/tableview/tableview.fxml"));
        playerInputPane.getChildren().setAll(tableViewPane);
    }
}
