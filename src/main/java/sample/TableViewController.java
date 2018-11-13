package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


public class TableViewController {

    @FXML
    private Line playerOneEnergy;

    @FXML
    private Rectangle playerOneDeck;

    @FXML
    private Circle playerOneGraveyard;

    @FXML
    private Label playerOneHp;

    @FXML
    private Rectangle playerOneHandOne;
    @FXML
    private Rectangle playerOneHandTwo;
    @FXML
    private Rectangle playerOneHandThree;
    @FXML
    private Rectangle playerOneHandFour;
    @FXML
    private Rectangle playerOneHandFive;

    @FXML
    private Rectangle playerOneTableOne;
    @FXML
    private Rectangle playerOneTableTwo;
    @FXML
    private Rectangle playerOneTableThree;
    @FXML
    private Rectangle playerOneTableFour;
    @FXML
    private Rectangle playerOneTableFive;
    @FXML
    private Rectangle playerOneTableSix;
    @FXML
    private Rectangle playerOneTableSeven;

    @FXML
    private Button endTurn;

    @FXML
    private Line playerTwoEnergy;

    @FXML
    private Rectangle playerTwoDeck;

    @FXML
    private Circle playerTwoGraveyard;

    @FXML
    private Label playerTwoHp;

    @FXML
    private Rectangle playerTwoHandOne;
    @FXML
    private Rectangle playerTwoHandTwo;
    @FXML
    private Rectangle playerTwoHandThree;
    @FXML
    private Rectangle playerTwoHandFour;
    @FXML
    private Rectangle playerTwoHandFive;

    @FXML
    private Rectangle playerTwoTableOne;
    @FXML
    private Rectangle playerTwoTableTwo;
    @FXML
    private Rectangle playerTwoTableThree;
    @FXML
    private Rectangle playerTwoTableFour;
    @FXML
    private Rectangle playerTwoTableFive;
    @FXML
    private Rectangle playerTwoTableSix;
    @FXML
    private Rectangle playerTwoTableSeven;
    @FXML
    private Pane tableViewPane;
    @FXML
    public ImageView tableImageView;
    @FXML
    private Pane currentPlayerHandPane;
    @FXML
    private ImageView playerOneHandImageView;
    @FXML
    AnchorPane cardPane;


    public void initialize() throws IOException {
        cardPane = FXMLLoader.load(getClass().getResource("/card/card.fxml"));
        
        cardPane.setLayoutY(762);
        cardPane.setLayoutX(898);
        tableViewPane.getChildren().add(cardPane);
    }

    public void getSelectedCard(MouseEvent e) {
        System.out.println("Selected card: " + e.getSource());
    }

    /**
     * initializes the players by
     * setting the players decks and cards in hands
     */
}
