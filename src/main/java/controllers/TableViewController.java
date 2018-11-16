package controllers;

import app.Server;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.util.List;

public class TableViewController {
    private Card selectedCurrentCard;
    private Card selectedOpponentCard1;
    private Card selectedOpponentCard2;
    private static AnchorPane selectedPane;

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

    private int activePlayer;
    @FXML
    private HBox playerOneTableBox;
    @FXML
    private HBox playerTwoTableBox;
    private Deck deck;
    private Server server;

    public TableViewController() throws IOException {
        deck = new Deck();
        deck.getCardsFromJSON();
    }

    public void initialize() throws IOException {
        server = Server.getInstance();
    }

    private Stage stage;

    public void showWinner() {
        winner.setVisible(true);
        update();
    }

    @FXML
    private void endTurn() throws IOException {
        server.msgToGameEngine("endturn");
        clearCards();
    }

    public void showPlayerTurn(int player) {
        if (player == 1) {
            activePlayer = 1;
            playerOneTurn.setVisible(true);
            playerTwoTurn.setVisible(false);
        } else if (player == 2) {
            activePlayer = 2;
            playerOneTurn.setVisible(false);
            playerTwoTurn.setVisible(true);
        } else {
            System.out.println("no Player");
        }
        update();
        System.out.println(activePlayer);

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
        String player = commands.get(1);
        for (int i = 2; i < commands.size(); i++) {
            Card card = deck.getCards().get(Integer.parseInt(commands.get(i)));
            String cardURL = "/card/card.fxml";
            switch (player) {
                case "1":
                    try {
                        cardPane = FXMLLoader.load(getClass().getResource(cardURL));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    playerOneHandBox.setSpacing(50);
                    playerOneHandBox.setAlignment(Pos.CENTER);
                    cardPane.setId(String.valueOf(card.getId()));
                    ((ImageView) cardPane.getChildren().get(cardPane.getChildren()
                            .indexOf(cardPane.lookup("#cardImageView"))))
                            .setImage(new Image(card.getImgURL()));
                    playerOneHandBox.getChildren().add(cardPane);
                    break;
                case "2":
                    try {
                        cardPane = FXMLLoader.load(getClass().getResource(cardURL));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    private Card getCardFromId(String id) {
        return deck.getCards().get(Integer.parseInt(id));
    }

    @FXML
    private void getSelectedPlaceHolder(Event event) {
        //check if selectedCurrentCard != null && opponentcards == null
        Rectangle rect = (Rectangle)event.getSource();
        swapPlaceHolder();
    }

    @FXML
    private void getSelectedCard(Event event) {
        Card selectedCard = getCardFromId(((AnchorPane) event.getSource()).getId());
        selectedPane = (AnchorPane) event.getSource();;
        if (selectedCurrentCard != null) {
//           does card exist in currentplayerhand or currentplayertable
            selectedCurrentCard = selectedCard;
        }
    }

    @FXML
    private void swapPlaceHolder() {
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

    private void clearCards() {
        selectedCurrentCard = null;
        selectedOpponentCard1 = null;
        selectedOpponentCard2 = null;
    }
}
