package models;

import app.Server;

import java.util.ArrayList;

public class Player {

    private int health;
    private String name;
    private int score;
    private int playerEnergy;
    private int player;

    private ArrayList<Card> currentDeck;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> tableCards;



//////////////////////////////////// CONSTRUCTOR \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


        public Player() {
        this.health = 20;
        this.currentDeck = new ArrayList<Card>();
        this.playerHand = new ArrayList<Card>();
        this.tableCards = new ArrayList<Card>();
    }


////////////////////////////////////// METHODS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * takes one card from players deck and
     * puts it in players hand
     */
    public void pickupCard() {
        if (playerHand.size() >= 5)
            return;
        int index = currentDeck.size() - 1;
        Card card = currentDeck.remove(index);
        playerHand.add(card);
        String playerHandString = null;
        playerHandString = Server.getInstance().getStringFromList(playerHand);

        try {
            Server.getInstance().msgToFX("showplayerhand," + player + "," + playerHandString);
            Server.getInstance().msgToFX("updatedeck," + player + "," + currentDeck.size());
        } catch (Exception e) {
        }
    }

    /**
     * removes the card from tableCards
     *
     * @param card
     */
    public void sendToGraveyard(Card card) {
        if (card instanceof MagicCard) {
            playerHand.remove(card);
        }
        tableCards.remove(card);
        try {
            Server.getInstance().msgToFX("sendtograveyard," + card.getId() + "," + player);
        } catch (Exception e) {
        }
    }

    /**
     * @param card takes an int showing what card to play
     *             play the chosen card:
     *             remove it from hand
     *             add it to table
     */
    public void playCard(Card card, int round) {
        if (tableCards.size() >= 7)
            return;

        try {
            if (card instanceof CreatureCard) {
                if (card.getCardEnergy() <= playerEnergy) {
                    tableCards.add(card);
                    playerHand.remove(card);
                    ((CreatureCard) card).tap();
                    ((CreatureCard) card).setPlayedOnRound(round);
                    setPlayerEnergy(playerEnergy - card.getCardEnergy());
                } else {
                    System.out.println("You don't have enough energy to play this card");

                    tryCatchNotEnoughEnergy("showmessage not enough energy");
                }
            } else {
                System.out.println("magic card cant be played");
            }
        } catch (Exception e) {
            System.out.println("That card does not exist");
        }
    }

    /**
     * When a player gets attacked, remove the amount of attack from the health
     *
     * @param healthToRemove of attack
     */
    public void removeHp(int healthToRemove) {
        this.health -= healthToRemove;
      tryCatchRemoveHp("playerhp,");
    }



///////////////////////////////////// TRY CATCH METHODS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


    public void tryCatchPlayerEnergy(String msg){
        try {
            Server.getInstance().msgToFX(msg + player+"," + playerEnergy);
        }catch (Exception e){

        }
    }
    public void tryCatchRemoveHp(String msg){
        try {
            Server.getInstance().msgToFX(msg + player + "," + Integer.toString(health));
        }catch (Exception e){

        }
    }

    public void tryCatchNotEnoughEnergy(String msg){
        try {
            Server.getInstance().msgToFX(msg);
        }catch (Exception e){

        }
    }



/////////////////////////////////////// GETTERS AND SETTERS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPlayer(int i) {
        player = i;
    }

    public int getPlayerEnergy() {
        return playerEnergy;
    }

    public void setPlayerEnergy(int playerEnergy) {
        this.playerEnergy = playerEnergy;

        tryCatchPlayerEnergy("updatemana,");
    }
    public int getHealth() {
        return health;
    }

    public void setCurrentDeck(ArrayList<Card> gameCards) {
        this.currentDeck = gameCards;
    }

    public ArrayList<Card> getCurrentDeck() {

        return currentDeck;
    }

    public ArrayList<Card> getPlayerHand() {

        return playerHand;
    }

    public ArrayList<Card> getTableCards() {

        return tableCards;
    }



}
