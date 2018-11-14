package controllers;

import app.Server;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.Card;
import models.Player;

import java.io.IOException;
import models.Card;
import models.Deck;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
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

    @FXML
    private HBox playerOneHandBox;
    @FXML
    private HBox playerTwoHandBox;
    @FXML
    private AnchorPane cardPane;
    private Server server;
    private Deck deck;
    private String cardURL = "/card/card.fxml";

    public TableViewController() throws IOException {
        deck = new Deck();
        server = Server.getInstance();
    }

    public void initialize() throws IOException {
    }

    public void showWinner() {

        Pane endImg = new Pane();

        Image winner = new Image("file:tableview/WinnerScreen.png");
        ImageView show = new ImageView(winner);

        show.setImage(winner);
        show.setVisible(true);
        endImg.getChildren().add(show);

    }

    public void showPlayerTurn(int player) {

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

    public void setPlayer1HP(int i) {
    }

    public void setPlayer2HP(int i) {
    }

    public void sendToGraveYard(int cardID) {
    }

    public void toSoonWarning() {
    }

    public void playCard(int cardID) {
    }

    public static void isTappedWarning() {
    }

    public void showPlayerHand(List<String> commands) throws IOException {
        int handSize = 5;
        try {
            deck.getCardsFromJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String player = commands.get(1);
        for (int i = 2; i < commands.size(); i++) {
            Card card = deck.getCards().get(Integer.parseInt(commands.get(i)));
            switch (player) {
                case "1":
                    for (int j = 0; j < handSize; j++) {
                        cardPane = FXMLLoader.load(getClass().getResource(cardURL));
                        playerOneHandBox.getChildren().add(cardPane);
                        playerOneHandBox.setSpacing(50);
                        playerOneHandBox.setAlignment(Pos.CENTER);
                        cardPane.setId(String.valueOf(card.getId()));
                    }
                    //TODO: Code for showing card in FX for player 1
                    break;
                case "2":
                    for (int k = 0; k < handSize; k++) {
                        cardPane = FXMLLoader.load(getClass().getResource("/card/card.fxml"));
                        playerTwoHandBox.getChildren().add(cardPane);
                        playerTwoHandBox.setSpacing(50);
                        playerTwoHandBox.setAlignment(Pos.CENTER);
                        cardPane.setId(String.valueOf(card.getId()));
                    }
                    //TODO: Code for showing card in FX for player 2
                    break;
                default:
                    System.out.println("No player! Something wrong with string input from gameEngine");
                    break;
            }
        }
    }

    public void renderCurrentPlayerHand() throws IOException {
    }

    public void renderOpponentPlayerHand() throws IOException {
        renderCurrentPlayerHand();

    }
}
