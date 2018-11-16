package controllers;

import javafx.event.Event;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.util.List;

public class TableViewController {

    @FXML
    private ProgressBar playerOneMana;
    @FXML
    private Rectangle playerOneDeck;
    @FXML
    private Circle playerOneGraveyard;
    @FXML
    private Label playerOneHp;
    @FXML
    private Button endTurn;
    @FXML
    private ProgressBar playerTwoMana;
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
    @FXML
    private HBox playerOneTableBox;
    @FXML
    private HBox playerTwoTableBox;
    private Deck deck;

    public TableViewController() throws IOException {
    }

    public void initialize() throws IOException {
        deck = new Deck();
        deck.getCardsFromJSON();
    }

    private Stage stage;

    public void showWinner() {
        winner.setVisible(true);
        update();
    }

    public void showPlayerTurn(int player) {

        if (player == 1) {
            playerOneTurn.setVisible(true);
            playerTwoTurn.setVisible(false);
        } else if (player == 2) {
            playerOneTurn.setVisible(false);
            playerTwoTurn.setVisible(true);
        } else {
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

    public void showPlayerHand(List<String> commands) throws IOException {
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
                    ((ImageView) cardPane.getChildren().get(cardPane.getChildren()
                            .indexOf(cardPane.lookup("#cardImageView"))))
                            .setImage(new Image(card.getImgURL()));
                    playerOneHandBox.getChildren().add(cardPane);
                    break;
                case "2":
                    cardPane = FXMLLoader.load(getClass().getResource(cardURL));
                    playerTwoHandBox.setSpacing(50);
                    playerTwoHandBox.setAlignment(Pos.CENTER);
                    cardPane.setId(String.valueOf(card.getId()));
                    ((ImageView) cardPane.getChildren().get(cardPane.getChildren()
                            .indexOf(cardPane.lookup("#cardImageView"))))
                            .setImage(new Image(card.getImgURL()));
                    playerTwoHandBox.getChildren().add(cardPane);
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

    private Card selectedCurrentCard;
    private Card selectedOpponentCard1;
    private Card selectedOpponentCard2;
    private static AnchorPane selectedPane;

    private Card getCardFromId(String id) {
        return deck.getCards().get(Integer.parseInt(id));
    }

    @FXML
    private void getSelectedPlaceHolder(Event event) {
        //check if selectedCurrentCard != null && opponentcards == null
        Rectangle rect = (Rectangle)event.getSource();
        swapPlaceHolder(rect);
    }

    @FXML
    private void getSelectedCard(Event event) {
        Card selectedCard = getCardFromId(((AnchorPane) event.getSource()).getId());
        selectedPane = (AnchorPane) event.getSource();
        if (selectedCurrentCard != null) {
//           does card exist in currentplayerhand or currentplayertable
            selectedCurrentCard = selectedCard;
        }
    }

    @FXML
    private void swapPlaceHolder(Rectangle r) {
        if(selectedPane != null) {
            playerOneTableBox.setSpacing(20);
            playerOneTableBox.setAlignment(Pos.CENTER);
            playerOneTableBox.getChildren().remove(playerOneTableBox.getChildren().size() - 1);
            playerOneTableBox.getChildren().add(0,selectedPane);
            update();
        }
        //set id to cardId
        //place card to placeHolder
        //if removing card from r , set id to random nr 0-100000
    }
}
