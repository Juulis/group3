package models;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Scanner;

public class GameEngine {

    private Player p1, p2;
    private ArrayList<Card> gameCards;

    public void setP1(Player p) {
        this.p1 = p;
    }

    public void setP2(Player p) {
        this.p2 = p;
    }

    public void setGameCards(ArrayList<Card> gameCards) {
        this.gameCards = gameCards;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    /**
     * initializes the players by
     * setting the players decks and cards in hands
     */
    public void initPlayer() {
        p1.setCurrentDeck(gameCards);
        p2.setCurrentDeck(gameCards);
        for (int i = 0; i < 5; i++) {
            p1.pickupCard();
            p2.pickupCard();
        }

    }

    public void endGame(){
        if (p1.getCurrentDeck().size() == 0) {
            System.out.println("Congratulations!" + p1 + " is the Winner");
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                e.printStackTrace();
            }
            System.exit(0);

        }else if (p2.getCurrentDeck().size() == 0){
            System.out.println("Congratulations!" + p2 + " is the Winner");
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                e.printStackTrace();
            }
            System.exit(0);
        }

        if (p1.getHealth() == 0) {
            System.out.println("Congratulations!" + p1 + " is the Winner");
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                e.printStackTrace();
            }
            System.exit(0);

        }else if (p2.getHealth() == 0){
            System.out.println("Congratulations!" + p2 + " is the Winner");
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                e.printStackTrace();
            }
            System.exit(0);
        }

    }

    public void startGame() {
        playerInput(p1);
    }

    private void playerInput(Player p) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Here is your choices: \n" +
                "1. Show table \n" +
                "2. Play card on hand \n" +
                "3. Attack a card \n" +
                "4. End Turn");
        int input = sc.nextInt();
        switch (input) {
            case 1:
                break;
            case 2:
                System.out.println("what card do you want to play out?");
                int playCard = sc.nextInt();
                p.playCard(playCard);
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
}
