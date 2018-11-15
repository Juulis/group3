package controllers;

import app.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.Card;
import models.Player;

import java.awt.*;
import java.io.IOException;

import models.Card;
import models.Deck;
import pojo.TableModel;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
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
    @FXML
    private HBox playerOneHandBox;
    @FXML
    private HBox playerTwoHandBox;
    @FXML
    private AnchorPane cardPane;

    private Server server;
    private Deck deck;
    private List<String> getP1Hand = Arrays.asList("showplayerhand", "1", "2", "3", "4", "5", "6");
    private List<String> getP2Hand = Arrays.asList("showplayerhand", "2", "2", "3", "4", "5", "6");

    public TableViewController() throws IOException {
        deck = new Deck();
        server = Server.getInstance();
    }

    public void initialize() throws IOException {
        /*cardPane = FXMLLoader.load(getClass().getResource("/card/card.fxml"));
        playerOneHandBox.getChildren().add(cardPane);*/
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

    public void isTappedWarning() {
    }

    public void showPlayerHand(List<String> commands) throws IOException {
        System.out.println("Call to showPlayerHand() in FX");
        try {
            deck.getCardsFromJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String player = commands.get(1);
        for (int i = 2; i < commands.size(); i++) {
            Card card = deck.getCards().get(Integer.parseInt(commands.get(i)));
            String cardURL = "/card/card.fxml";
            switch (player) {
                case "1":
                    cardPane = FXMLLoader.load(getClass().getResource(cardURL));
                    playerOneHandBox.setSpacing(50);
                    playerOneHandBox.setAlignment(Pos.CENTER);
                    cardPane.setId(String.valueOf(card.getId()));
                    System.out.print("CardId " + card.getId() + "/ " + " imgURL: " + card.getImgURL() + " ");
                    System.out.println(cardPane);
                    //TODO: Code for showing card in FX for player 1
                    break;
                case "2":
                    cardPane = FXMLLoader.load(getClass().getResource(cardURL));
                    playerOneHandBox.getChildren().add(cardPane);
                    playerTwoHandBox.setSpacing(50);
                    playerTwoHandBox.setAlignment(Pos.CENTER);
                    cardPane.setId(String.valueOf(card.getId()));
                    System.out.print("CardId " + card.getId() + "/ " + " imgURL: " + card.getImgURL() + " ");
                    System.out.println(cardPane);
                    //TODO: Code for showing card in FX for player 2
                    break;
                default:
                    System.out.println("No player! Something wrong with string input from gameEngine");
                    break;
            }
        }
    }
}
