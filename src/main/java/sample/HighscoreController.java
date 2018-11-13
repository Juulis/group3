package sample;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import pojo.HighscoreModel;
import pojo.TableModel;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

