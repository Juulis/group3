package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.GameEngine;
import models.Player;

import java.io.IOException;


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
    public ImageView winner;

    @FXML
    public ImageView playerOneTurn;

    @FXML
    public ImageView playerTwoTurn;

    public void showWinner() throws IOException {

        Pane endImg = new Pane();

        GameEngine checkPlaying = new GameEngine();
        boolean checkWinner = checkPlaying.playing;

        if (!checkWinner) {
            Image winner = new Image("file:tableView/WinnerScreen.png");
            ImageView show = new ImageView(winner);

            endImg.getChildren().add(show);

            show.setImage(winner);
            show.setVisible(true);
        }
    }

    public void showPlayerTurn() throws IOException {

        Pane turnImg = new Pane();

        GameEngine checkPlayerTurn = new GameEngine();

        Player current = checkPlayerTurn.currentPlayer;
        Player opponent = checkPlayerTurn.opponentPlayer;

        if (current!=null) {
            Image turn = new Image("file:tableView/Fire_GIF.gif");
            ImageView show = new ImageView(turn);

            turnImg.getChildren().add(show);

            show.setImage(turn);
            show.setVisible(true);

        }else if (opponent!=null) {
            Image turn = new Image("file:tableView/Fire_GIF.gif");
            ImageView show = new ImageView(turn);

            turnImg.getChildren().add(show);

            show.setImage(turn);
            show.setVisible(false);
        }
    }

    public void initialize() throws IOException{
    showWinner();
    showPlayerTurn();
    }
}
