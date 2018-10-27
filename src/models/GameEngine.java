package models;

import java.util.ArrayList;

public class GameEngine {

    private Player p1, p2;
    private ArrayList<Card> gameCards;

    public void setP1(Player p){
        this.p1=p;
    }

    public void setP2(Player p){
        this.p2=p;
    }

    public void setGameCards(ArrayList<Card> gameCards){
        this.gameCards=gameCards;
    }

    public Player getP1(){
        return p1;
    }

    public Player getP2(){
        return p2;
    }

    /**
     * creates two player objects and
     * sets the players decks and cards in hands
     */
    public void initPlayer(){

    }
}
