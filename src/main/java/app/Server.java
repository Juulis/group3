package app;

import controllers.TableViewController;
import javafx.fxml.FXMLLoader;
import models.Card;
import models.CreatureCard;
import models.GameEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Server {
    private GameEngine gameEngine;
    private TableViewController tableViewController;

    private static Server instance = null;

    private Server() throws IOException {
        instance = this;
        gameEngine = new GameEngine();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tableview/tableview.fxml"));
        loader.load();
        tableViewController = loader.getController();
        gameEngine.startGame("fx");
    }

    public static Server getInstance() throws IOException {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }


    /**
     * @param msg string should be formatted ass following:
     *            1: command (attack, endturn etc)
     *            2: selected card id
     *            3: opponent card id
     *            4: more opponents if needed
     *            <p>
     *            example string: "attack,4,1,2"
     *            card 4 will attack opponent cards 1 and 2
     */
    public void msgToGameEngine(String msg) throws IOException {
        List<String> commands = Arrays.asList(msg.split(","));
        Card selectedCard = gameEngine.getDeck().getCards().get(Integer.parseInt(commands.get(1)));
        List<CreatureCard> opponents = new ArrayList<>();

        switch (commands.get(0)) {
            case "attack":
                int opponentAmount = commands.size() - 2; //ignoring the first two spots

                for (int i = 0; i < opponentAmount; i++) {
                    opponents.add((CreatureCard) gameEngine.getDeck().getCards().get(Integer.parseInt(commands.get(i + 2)))); //ignoring first two spots
                }
                gameEngine.chooseAttack(selectedCard, opponents);
                break;
            case "endturn":
                gameEngine.endTurn();
                break;
            case "playcard":
                gameEngine.getCurrentPlayer().playCard(selectedCard, gameEngine.getRound());
                break;
        }
        System.out.println(commands); //TODO: Remove, testingpurpose
    }

    public void msgToFX(String msg) throws IOException {
        List<String> commands = Arrays.asList(msg.split(","));
        switch (commands.get(0)) {
            case "showplayerhand":
                tableViewController.showPlayerHand(commands);
                break;
            case "gameover":
                tableViewController.showWinner();
                break;
            case "player1":
                tableViewController.showPlayerTurn(1);
                break;
            case "player2":
                tableViewController.showPlayerTurn(2);
                break;
            case "player1hp":
                tableViewController.setPlayer1HP(Integer.parseInt(commands.get(1)));
                break;
            case "player2hp":
                tableViewController.setPlayer2HP(Integer.parseInt(commands.get(1)));
                break;
            case "sendtograveyard":
                tableViewController.sendToGraveYard(Integer.parseInt(commands.get(1)));
                break;
            case "attackedtosoon":
                tableViewController.toSoonWarning();
                break;
            case "playcard":
                tableViewController.playCard(Integer.parseInt(commands.get(1)));
                break;
            case "tapped":
                tableViewController.isTappedWarning();
                break;
        }
    }
}
