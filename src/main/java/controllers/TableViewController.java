package controllers;

import app.Server;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private Pane currentPlayerHandPane;
    @FXML
    public ImageView winner;
    @FXML
    public ImageView playerOneTurn;
    @FXML
    public ImageView playerTwoTurn;

    private Stage stage;

    public void showWinner() {
        winner.setVisible(true);
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

    public void showPlayerHand(List<String> commands) {
        Deck deck = new Deck();
        try {
            deck.getCardsFromJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String player = commands.get(1);
        for (int i = 2; i < commands.size(); i++) {
            Card card = deck.getCards().get(Integer.parseInt(commands.get(i)));
            if (player.equals("1")) {
                System.out.println("showing card " + card.getId());
                //TODO: Code for showing card in FX for player 1
            } else if (player.equals("2")) {
                System.out.println("showing card " + card.getId());
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

    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    private void update() {
        Scene scene = new Scene(tableViewPane);
        stage.setScene(scene);
        stage.show();
    }
}
