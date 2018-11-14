package controllers;

import app.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.Card;
import models.Deck;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;


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
    public ImageView winner;

    @FXML
    public ImageView playerOneTurn;

    @FXML
    public ImageView playerTwoTurn;

    public static void showWinner() {

        Pane endImg = new Pane();

        Image winner = new Image("file:tableView/WinnerScreen.png");
        ImageView show = new ImageView(winner);

        endImg.getChildren().add(show);

        show.setImage(winner);
        show.setVisible(true);

    }

    public static void showPlayerTurn(int player) {

        Pane turnImg = new Pane();

        if (player == 1) {
            Image turn = new Image("file:tableView/Fire_GIF.gif");
            ImageView show = new ImageView(turn);

            turnImg.getChildren().add(show);

            show.setImage(turn);
            show.setVisible(true);

        } else if (player == 2) {
            Image turn = new Image("file:tableView/Fire_GIF.gif");
            ImageView show = new ImageView(turn);

            turnImg.getChildren().add(show);

            show.setImage(turn);
            show.setVisible(false);
        }
    }

    public static void setPlayer1HP(int i) {
    }

    public static void setPlayer2HP(int i) {
    }

    public static void sendToGraveYard(int cardID) {
    }

    public static void toSoonWarning() {
    }

    public static void playCard(int cardID) {
    }

    public static void isTappedWarning() {
    }

    public static void showPlayerHand(List<String> commands) {
        Deck deck = new Deck();
        String player = commands.get(0);
        commands.remove(0);
        for (String id : commands) {
            Card card = deck.getCards().get(Integer.parseInt(id));
            if (player.equals("1")) {
                //TODO: Code for showing card in FX for player 1
            } else if (player.equals("2")) {
                //TODO: Code for showing card in FX for player 2
            } else {
                System.out.println("No player! Something wrong with string input from gameEngine");
            }
        }
    }

    public void getSelectedCard(MouseEvent e) throws IOException {
        System.out.println("Selected card: " + e.getSource());
        Server.getInstance().msgToGameEngine("attack,2,1");
    }
}
