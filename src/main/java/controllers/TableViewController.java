package controllers;

import app.Server;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TableViewController {
    private static Card selectedCurrentCard;
    private static Card selectedOpponentCard1;
    private static Card selectedOpponentCard2;
    private static AnchorPane selectedPane;
    private Deck deck;
    private Server server;
    private static int activePlayer;
    @FXML
    private Label p2manalabel;
    @FXML
    private Label p1manalabel;
    @FXML
    private Label player1label;
    @FXML
    private Label player2label;
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
    @FXML
    private Label messagebar;
    @FXML
    private Label decklabel1;
    @FXML
    private Label decklabel2;

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
    }

    public void sendToGraveYard(String cardID, String player) {
        List<Node> nodesToRemove = new ArrayList<>();
        if (deck.getCards().get(Integer.parseInt(cardID)) instanceof MagicCard) {
            if (player.equals("1")) {
                for (Node n : playerOneHandBox.getChildren()) {
                    if (n.getId().equals(cardID)) {
                        nodesToRemove.add(n);
                    }
                }
                playerOneHandBox.getChildren().removeAll(nodesToRemove);
            } else {
                for (Node n : playerTwoHandBox.getChildren()) {
                    if (n.getId().equals(cardID)) {
                        nodesToRemove.add(n);
                    }
                }
                playerTwoHandBox.getChildren().removeAll(nodesToRemove);
            }
        } else {
            if (player.equals("1")) {
                for (Node n : playerOneTableBox.getChildren()) {
                    if (n.getId().equals(cardID)) {
                        nodesToRemove.add(n);
                    }
                }
                playerOneTableBox.getChildren().removeAll(nodesToRemove);
            } else {
                for (Node n : playerTwoTableBox.getChildren()) {
                    if (n.getId().equals(cardID)) {
                        nodesToRemove.add(n);
                    }
                }
                playerTwoTableBox.getChildren().removeAll(nodesToRemove);
            }
        }
        addNewPlaceholderForP1();
        addNewPlaceholderForP2();
        update();
    }

    public String randomId() {
        Random r = new Random();
        int randomNr = r.nextInt(1000000);
        return String.valueOf(randomNr);
    }

    public void addNewPlaceholderForP1() {
        Rectangle rect = new Rectangle();
        rect.setArcHeight(500);
        rect.setArcWidth(50);
        rect.setId(randomId());
        playerOneTableBox.getChildren().add(rect);
    }

    public void addNewPlaceholderForP2() {
        Rectangle rect = new Rectangle();
        rect.setArcHeight(500);
        rect.setArcWidth(50);
        rect.setId(randomId());
        playerTwoTableBox.getChildren().add(rect);
    }

    public void toSoonWarning() {
    }

    public void playCard(int cardID) {
    }

    public void tapCard(String cardID) {
        for (Node n : playerOneTableBox.getChildren()) {
            if (n.getId().equals(cardID)) {
                n.lookup("#cardImageView").setEffect(new GaussianBlur());
            } else {
                for (Node o : playerTwoTableBox.getChildren()) {
                    if (o.getId().equals(cardID)) {
                        o.lookup("#cardImageView").setEffect(new GaussianBlur());
                    }
                }
            }
        }
    }

    public void unTapCard(String cardID) {
        for (Node n : playerOneTableBox.getChildren()) {
            if (n.getId().equals(cardID)) {
                n.lookup("#cardImageView").setEffect(null);
            } else {
                for (Node o : playerTwoTableBox.getChildren()) {
                    if (o.getId().equals(cardID)) {
                        o.lookup("#cardImageView").setEffect(null);
                    }
                }
            }
        }
    }

    public void showPlayerHand(List<String> commands) {
        String player = commands.get(1);
        Circle hpCircle;
        Label top;
        Label middle;
        Label bottom;
        Label cardIdType;

        StackPane attackPane;
        StackPane manaPane;
        StackPane cardType;
        StackPane healthPane;

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
                    cardPane.setId(String.valueOf(card.getId()));
                    ((ImageView) cardPane.getChildren().get(cardPane.getChildren()
                            .indexOf(cardPane.lookup("#cardImageView"))))
                            .setImage(new Image(card.getImgURL()));

                    attackPane = (StackPane) cardPane.getChildren().get(2);
                    bottom = (Label) attackPane.getChildren().get(1);
                    bottom.setText(Integer.toString(card.getAttack()));

                    manaPane = (StackPane) cardPane.getChildren().get(3);
                    middle = (Label) manaPane.getChildren().get(1);
                    middle.setText(Integer.toString(card.getCardEnergy()));

                    cardType = (StackPane) cardPane.getChildren().get(0);
                    cardIdType = (Label) cardType.getChildren().get(0);
                    cardIdType.setText(card.getSpecialAttack().toUpperCase());
                    if(card.getSpecialAttack().equalsIgnoreCase("dualattack")) {
                        cardIdType.setFont(new Font("Gill Sans Ultra Bold Condensed", 15));
                    }

                    if (!(card instanceof MagicCard)) {
                        healthPane = (StackPane) cardPane.getChildren().get(4);
                        top = (Label) healthPane.getChildren().get(1);
                        top.setText(Integer.toString(((CreatureCard) card).getHp()));
                        top.toFront();
                    } else if (card instanceof MagicCard) {
                        healthPane = (StackPane) cardPane.getChildren().get(4);
                        top = (Label) healthPane.getChildren().get(1);
                        hpCircle = (Circle) healthPane.getChildren().get(0);
                        hpCircle.setVisible(false);
                        top.setVisible(false);
                    }

                    middle.toFront();
                    bottom.toFront();
                    cardIdType.toFront();

                    playerOneHandBox.getChildren().add(cardPane);
                    break;
                case "2":
                    try {
                        cardPane = FXMLLoader.load(getClass().getResource(cardURL));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cardPane.setId(String.valueOf(card.getId()));
                    ((ImageView) cardPane.getChildren().get(cardPane.getChildren()
                            .indexOf(cardPane.lookup("#cardImageView"))))
                            .setImage(new Image(card.getImgURL()));

                    attackPane = (StackPane) cardPane.getChildren().get(2);
                    bottom = (Label) attackPane.getChildren().get(1);
                    bottom.setText(Integer.toString(card.getAttack()));

                    manaPane = (StackPane) cardPane.getChildren().get(3);
                    middle = (Label) manaPane.getChildren().get(1);
                    middle.setText(Integer.toString(card.getCardEnergy()));

                    cardType = (StackPane) cardPane.getChildren().get(0);
                    cardIdType = (Label) cardType.getChildren().get(0);
                    cardIdType.setText(card.getSpecialAttack().toUpperCase());
                    if(card.getSpecialAttack().equalsIgnoreCase("dualattack")) {
                        cardIdType.setFont(new Font("Gill Sans Ultra Bold Condensed", 15));
                    }

                    if (!(card instanceof MagicCard)) {
                        healthPane = (StackPane) cardPane.getChildren().get(4);
                        top = (Label) healthPane.getChildren().get(1);
                        top.setText(Integer.toString(((CreatureCard) card).getHp()));
                        top.toFront();
                    } else if (card instanceof MagicCard) {
                        healthPane = (StackPane) cardPane.getChildren().get(4);
                        top = (Label) healthPane.getChildren().get(1);
                        hpCircle = (Circle) healthPane.getChildren().get(0);
                        hpCircle.setVisible(false);
                        top.setVisible(false);
                    }

                    middle.toFront();
                    bottom.toFront();
                    cardIdType.toFront();

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
        //TODO: check if selectedCurrentCard != null && opponentcards == null
        Rectangle placeHolder = (Rectangle) event.getSource();
        swapPlaceHolder(placeHolder);
    }

    @FXML
    private void getSelectedCard(Event event) {
        if (isActivePlayerSelectingHandCard(event) || isSelectingCardToAttackWith(event)) {
            selectedCurrentCard = getCardFromId(((AnchorPane) event.getSource()).getId());
            selectedPane = (AnchorPane) event.getSource();
        } else if (isSelectingCardToAttack(event)) {
            if (selectedOpponentCard1 == null) {
                selectedOpponentCard1 = getCardFromId(((AnchorPane) event.getSource()).getId());
                if (selectedCurrentCard.getSpecialAttack().equals("basic") || selectedCurrentCard.getSpecialAttack().equals("ignite")) {
                    server.msgToGameEngine("attack," + selectedCurrentCard.getId() + "," + selectedOpponentCard1.getId());
                    clearCards();
                }
            } else if (selectedCurrentCard.getSpecialAttack().equals("dualAttack")) {
                selectedOpponentCard2 = getCardFromId(((AnchorPane) event.getSource()).getId());
                server.msgToGameEngine("attack," + selectedCurrentCard.getId() + "," + selectedOpponentCard1.getId() + "," + selectedOpponentCard2.getId());
                clearCards();
            }
        }
    }

    private boolean isSelectingCardToAttackWith(Event event) {
        return (selectedCurrentCard == null && activePlayer == 1 && ((AnchorPane) event.getSource()).getParent().getId().equals("playerOneTableBox")) ||
                (selectedCurrentCard == null && activePlayer == 2 && ((AnchorPane) event.getSource()).getParent().getId().equals("playerTwoTableBox"));
    }

    private boolean isSelectingCardToAttack(Event event) {
        return (activePlayer == 1 && ((AnchorPane) event.getSource()).getParent().getId().equals("playerTwoTableBox") && selectedCurrentCard != null) ||
                (activePlayer == 2 && selectedCurrentCard != null && ((AnchorPane) event.getSource()).getParent().getId().equals("playerOneTableBox"));
    }

    private boolean isActivePlayerSelectingHandCard(Event event) {
        return (((AnchorPane) event.getSource()).getParent().getId().equals("playerOneHandBox") && activePlayer == 1) ||
                (((AnchorPane) event.getSource()).getParent().getId().equals("playerTwoHandBox") && activePlayer == 2);
    }

    @FXML
    private void swapPlaceHolder(Rectangle rect) {
        if (selectedPane != null && !(selectedCurrentCard.getClass().equals(MagicCard.class)) && isManaEnough()) {
            if (activePlayer == 1 && playerOneTableBox.getChildren().contains(rect) && playerOneHandBox.getChildren().contains(selectedPane)) {
                playerOneTableBox.getChildren().remove(playerOneTableBox.getChildren().size() - 1);
                playerOneTableBox.getChildren().add(0, selectedPane);
            } else if (activePlayer == 2 && playerTwoTableBox.getChildren().contains(rect) && playerTwoHandBox.getChildren().contains(selectedPane)) {
                playerTwoTableBox.getChildren().remove(playerTwoTableBox.getChildren().size() - 1);
                playerTwoTableBox.getChildren().add(0, selectedPane);
            } else {
                return;
            }
            server.msgToGameEngine("playcard," + selectedPane.getId());
            update();
            clearCards();
        }
    }

    private boolean isManaEnough() {
        return (activePlayer == 1 && Integer.parseInt(p1manalabel.getText()) >= selectedCurrentCard.getCardEnergy()) ||
                (activePlayer == 2 && Integer.parseInt(p2manalabel.getText()) >= selectedCurrentCard.getCardEnergy());
    }


    private void clearCards() {
        selectedPane = null;
        selectedCurrentCard = null;
        selectedOpponentCard1 = null;
        selectedOpponentCard2 = null;
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

    public void setDeckLabels(int player, int cards) {
        if (player == 1) {
            decklabel1.setText(Integer.toString(cards));
        } else if (player == 2) {
            decklabel2.setText(Integer.toString(cards));
        }
    }

    public void showMessage(String msg) {
        messagebar.setText(msg);
    }

    @FXML
    private void playerattack(MouseEvent mouseEvent) {
        if (selectedCurrentCard != null && selectedCurrentCard.getSpecialAttack().equals("playerAttack") && ( //check so player dont targets it self
                (((Circle) mouseEvent.getSource()).getId().equals("playeroneavatar") && activePlayer == 2) ||
                        (((Circle) mouseEvent.getSource()).getId().equals("playeroneavatar") && activePlayer == 1))) {
            server.msgToGameEngine("attack," + selectedCurrentCard.getId());
        }
    }

    public void setPlayerNames(String p1, String p2) {
        player1label.setText(p1);
        player2label.setText(p2);
    }

    public void setMana(int player, int mana) {
        if (player == 1) {
//            playerOneMana.setProgress(mana);
            p1manalabel.setText(Integer.toString(mana));
        } else if (player == 2) {
//            playerTwoMana.setProgress(mana);
            p2manalabel.setText(Integer.toString(mana));
        }
        update();
    }

    public void setCardHP(int cardID, int hp) {
        try {
            ((Label)((StackPane)(getAnchorPaneFromBox(cardID, playerOneTableBox).lookup("#healthPane"))).getChildren().get(1)).setText(Integer.toString(hp));
            return;
        } catch (Exception e) {
        }
        try {
            ((Label)((StackPane)(getAnchorPaneFromBox(cardID, playerTwoTableBox).lookup("#healthPane"))).getChildren().get(1)).setText(Integer.toString(hp));
            return;
        } catch (Exception e) {

        }
    }

    private AnchorPane getAnchorPaneFromBox(int cardID, HBox hBox) {
        for(Node node: hBox.getChildren()){
            if(node.getId().equals(Integer.toString(cardID))){
                return (AnchorPane) node;
            }
        }
        return null;
    }
}
