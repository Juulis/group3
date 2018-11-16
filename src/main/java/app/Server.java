package app;

import controllers.*;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.util.*;

public class Server {
    private GameEngine gameEngine;
    private static Server instance = null;
    private TableViewController tvc;

    private Server() throws IOException {
        instance = this;
        gameEngine = new GameEngine();
    }

    public static Server getInstance() throws IOException {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void startGame() throws IOException {
        gameEngine.startGame("fx");
    }

    public void setTvc(TableViewController tvc, Stage stage) {
        this.tvc = tvc;
        tvc.setStage(stage);
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
                tvc.showPlayerHand(commands);
                break;
            case "gameover":
                tvc.showWinner();
                break;
            case "player1":
                tvc.showPlayerTurn(1);
                break;
            case "player2":
                tvc.showPlayerTurn(2);
                break;
            case "playerhp":
                tvc.setPlayerHP(Integer.parseInt(commands.get(1)));
                break;
            case "sendtograveyard":
                tvc.sendToGraveYard(Integer.parseInt(commands.get(1)));
                break;
            case "attackedtosoon":
                tvc.toSoonWarning();
                break;
            case "playcard":
                tvc.playCard(Integer.parseInt(commands.get(1)));
                break;
            case "tapped":
                tvc.isTappedWarning();
                break;
        }
    }

    public void setPlayerNames(String name1, String name2){
        gameEngine.getP1().setName(name1);
        gameEngine.getP2().setName(name2);
    }
}
