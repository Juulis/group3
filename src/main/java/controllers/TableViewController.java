package controllers;

import app.Server;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Card;
import models.Deck;

import java.io.IOException;
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

    public TableViewController() throws IOException {
        deck = new Deck();
    }

    public void initialize() throws IOException {
        /*cardPane = FXMLLoader.load(getClass().getResource("/card/card.fxml"));
        playerOneHandBox.getChildren().add(cardPane);*/
        ImageView bajs = new ImageView();
        Image kiss = new Image("/cardpics/Card1.png");
        bajs.setImage(kiss);
        playerOneHandBox.getChildren().addAll(bajs);
    }

    private Stage stage;

    public void showWinner() {
        winner.setVisible(true);
        tableViewPane.getChildren().remove(winner);
        tableViewPane.getChildren().add(winner);
        update();
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
                    /*((ImageView) cardPane.getChildren()
                            .filtered(node -> node.getId().equals("cardImageView")).get(0)).setImage(new Image(card.getImgURL()));*/
                    System.out.print("CardId " + card.getId() + "/ " + " imgURL: " + card.getImgURL() + " ");
                    System.out.println(cardPane);
                    //TODO: Code for showing card in FX for player 1
                    break;
                case "2":
                    cardPane = FXMLLoader.load(getClass().getResource(cardURL));
                    playerTwoHandBox.getChildren().add(cardPane);
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
        update();
    }

    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    private void update() {
        Parent parent = tableViewPane;
        stage.getScene().setRoot(parent);
    }
}
