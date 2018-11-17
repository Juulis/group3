package controllers;

import app.Server;
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
    private Card selectedCurrentCard;
    private Card selectedOpponentCard1;
    private Card selectedOpponentCard2;
    private static AnchorPane selectedPane;
    private Deck deck;
    private Server server;
    private int activePlayer;

    @FXML
    private ProgressIndicator playerOneHpRound;
    @FXML
    private ProgressIndicator playerTwoHpRound;
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
        if (player.equals("1"))
            playerOneHandBox.getChildren().clear();
        if (player.equals("2"))
            playerTwoHandBox.getChildren().clear();

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
    private void getSelectedPlaceHolder(Event event) throws IOException {
        //TODO: check if selectedCurrentCard != null && opponentcards == null
        Rectangle placeHolder = (Rectangle) event.getSource();
        swapPlaceHolder(placeHolder);
    }

    @FXML
    private void getSelectedCard(Event event) {
        Card selectedCard = getCardFromId(((AnchorPane) event.getSource()).getId());
        selectedPane = (AnchorPane) event.getSource();
        if (selectedCurrentCard != null) {
//          TODO: does card exist in currentplayerhand or currentplayertable
            selectedCurrentCard = selectedCard;
        }
    }

    @FXML
    private void swapPlaceHolder(Rectangle placeHolder) throws IOException {
        if (selectedPane != null) {
            if (playerOneTableBox.getChildren().contains(placeHolder)) { //checks to see if we clicked p1 or p2 table
                playerOneTableBox.setSpacing(20);
                playerOneTableBox.setAlignment(Pos.CENTER);
                playerOneTableBox.getChildren().remove(playerOneTableBox.getChildren().size() - 1);
                playerOneTableBox.getChildren().add(0, selectedPane);
            } else if (playerTwoTableBox.getChildren().contains(placeHolder)) {
                playerTwoTableBox.setSpacing(20);
                playerTwoTableBox.setAlignment(Pos.CENTER);
                playerTwoTableBox.getChildren().remove(playerTwoTableBox.getChildren().size() - 1);
                playerTwoTableBox.getChildren().add(0, selectedPane);
            } else {
                return;
            }
            update();
            server.msgToGameEngine("playcard," + selectedPane.getId());
        }
    }


    private void clearCards() {
        selectedCurrentCard = null;
        selectedOpponentCard1 = null;
        selectedOpponentCard2 = null;
    }

    @FXML
    private void pickCard(MouseEvent mouseEvent) throws IOException {
        if (playerOneHandBox.getChildren().size() <= 5 && ((Rectangle) mouseEvent.getSource()).getId().equals("playerOneDeck")) {
            server.msgToGameEngine("pickcard,1");
        } else if (playerTwoHandBox.getChildren().size() <= 5 && ((Rectangle) mouseEvent.getSource()).getId().equals("playerTwoDeck")) {
            server.msgToGameEngine("pickcard,2");
        }
    }


    public void setPlayerHP(int player, int hp) {
        if (player == 1) {
            playerOneHp.setText(Integer.toString(hp));
            playerOneHpRound.setProgress(Math.abs(((double) hp / 20) - 1));
        } else if (player == 2) {
            playerTwoHp.setText(Integer.toString(hp));
            playerTwoHpRound.setProgress(Math.abs(((double) hp / 20) - 1));
        }
    }
}
