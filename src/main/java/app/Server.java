package app;

import controllers.TableViewController;
import models.Card;
import models.CreatureCard;
import models.GameEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Server {
    private GameEngine gameEngine;

    private static Server instance;

    private Server() {
        gameEngine = new GameEngine();
        try {
            gameEngine.startGame("fx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Server getInstance() {
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
    public void msgToGameEngine(String msg) {
        List<String> commands = Arrays.asList(msg.split(","));
        Card selectedCard = gameEngine.getDeck().getCards().get(Integer.parseInt(commands.get(1)));
        List<CreatureCard> opponents = new ArrayList<>();

        if (commands.get(0).equals("attack")) {
            int opponentAmount = commands.size() - 2; //ignoring the first two spots
            for (int i = 0; i < opponentAmount; i++) {
                opponents.add((CreatureCard) gameEngine.getDeck().getCards().get(Integer.parseInt(commands.get(i + 2)))); //ignoring first two spots
            }
            gameEngine.chooseAttack(selectedCard, opponents);
        } else if (commands.get(0).equals("endturn")) {
            gameEngine.endTurn();
        } else if (commands.get(0).equals("playcard")) {
            gameEngine.getCurrentPlayer().playCard(selectedCard, gameEngine.getTurn());
        }
        System.out.println(commands); //TODO: Remove, testingpurpose
    }

    public void msgToFX(String msg) {
        List<String> commands = Arrays.asList(msg.split(","));
        switch (commands.get(0)) {
            case "gameover":
                TableViewController.showWinner();
                break;
            case "player1":
                TableViewController.showPlayerTurn(1);
                break;
            case "player2":
                TableViewController.showPlayerTurn(2);
                break;
            case "player1hp":
                TableViewController.setPlayer1HP(Integer.parseInt(commands.get(1)));
                break;
            case "player2hp":
                TableViewController.setPlayer2HP(Integer.parseInt(commands.get(1)));
                break;
            case "sendtograveyard":
                TableViewController.sendToGraveYard(Integer.parseInt(commands.get(1)));
                break;
            case "attackedtosoon":
                TableViewController.toSoonWarning();
                break;
            case "playcard":
                TableViewController.playCard(Integer.parseInt(commands.get(1)));
                break;
            case "tapped":
                TableViewController.isTappedWarning();
        }
    }
}
