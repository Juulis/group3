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
    private Line playerTwoMana;

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

    public void showWinner() {

        Pane endImg = new Pane();

        Image winner = new Image("file:tableView/WinnerScreen.png");
        ImageView show = new ImageView(winner);

        endImg.getChildren().add(show);

        show.setImage(winner);
        show.setVisible(true);

    }

    public void showPlayerTurn(int player) {

        if (player == 1) {
            playerOneTurn.setVisible(true);
            playerTwoTurn.setVisible(false);
        } else if (player == 2) {
            playerOneTurn.setVisible(false);
            playerTwoTurn.setVisible(true);
        }else{
            System.out.println("no Player");
        }
        update();
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
        for (int i = 2;i<commands.size();i++) {
            Card card = deck.getCards().get(Integer.parseInt(commands.get(i)));
            if (player.equals("1")) {
                System.out.println("showing card "+card.getId());
                //TODO: Code for showing card in FX for player 1
            } else if (player.equals("2")) {
                System.out.println("showing card "+card.getId());
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
