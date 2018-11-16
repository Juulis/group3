package controllers;

import com.google.gson.*;
import com.google.gson.stream.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import pojo.*;
import java.io.*;
import java.util.*;

public class HighscoreController {

    @FXML
    private AnchorPane highscorePane;
    @FXML
    private TableView<TableModel> highscoreTableView;
    @FXML
    private TableColumn<TableModel, String> col_name;
    @FXML
    private TableColumn<TableModel, String> col_score;

    public void initialize() throws FileNotFoundException {
        System.out.println("Changed scene to Highscore.fxml");
        getHighscore();
    }

    public void goToMainMenu() throws IOException {
        AnchorPane menuPane = FXMLLoader.load(getClass().getResource("/menu/menu.fxml"));
        highscorePane.getChildren().setAll(menuPane);
    }

    public void getHighscore() throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader("src/main/java/json/Highscore.json"));
        List<TableModel> highscore = Arrays.asList(new Gson().fromJson(reader, HighscoreModel[].class));
        ObservableList<TableModel> list = FXCollections.observableArrayList(highscore);
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_score.setCellValueFactory(new PropertyValueFactory<>("score"));
        highscoreTableView.setItems(list);
    }
}

