package app;

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
     * @param input
     *
     * string should be formatted ass following:
     * 1: command (attack, endturn etc)
     * 2: selected card id
     * 3: opponent card id
     * 4: more opponents if needed
     *
     * example string: "attack,4,1,2"
     * card 4 will attack opponent cards 1 and 2
     *
     */
    public void msgToGameEngine(String input) {
        List<String> commands = Arrays.asList(input.split(","));
        Card selectedCard = gameEngine.getDeck().getCards().get(Integer.parseInt(commands.get(1)));
        List<CreatureCard> opponents = new ArrayList<>();

        if (commands.get(0).equals("attack")) {
            int opponentAmount = commands.size() - 2; //ignoring the first two spots
            for (int i = 0; i < opponentAmount; i++) {
                opponents.add((CreatureCard)gameEngine.getDeck().getCards().get(Integer.parseInt(commands.get(i + 2)))); //ignoring first two spots
            }
            gameEngine.chooseAttack(selectedCard, opponents);
        }
        else if(commands.get(0).equals("endturn")){
            gameEngine.endTurn();
        }else if(commands.get(0).equals("playcard")){
            gameEngine.getCurrentPlayer().playCard(selectedCard,gameEngine.getRound());
        }
        System.out.println(commands);
        System.out.println("\""+commands.get(0)+"\"");
    }

    public void msgToFX(){
        //TODO: implement some awesome stuff to get msg from gameEngine to FX controllers
    }

    public List<Card> getCurrentPlayerHand(){
        return gameEngine.getCurrentPlayer().getPlayerHand();
    }

    public List<Card> getOpponentPlayerHand(){
        return gameEngine.getOpponentPlayer().getPlayerHand();
    }
}
