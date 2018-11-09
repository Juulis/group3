package models;

import java.util.ArrayList;

public class Attack {

    public Attack() {
    }

    public void basicAttack(Card currentPlayerCard, CreatureCard opponentCard) {
        int giveDmg = currentPlayerCard.getAttack();
        int takeDmg = opponentCard.getAttack();
        opponentCard.removeHp(giveDmg);
        if(currentPlayerCard.getClass() == CreatureCard.class){
            ((CreatureCard)currentPlayerCard).removeHp(takeDmg);
        }
    }

    public void dualAttack() {

    }

    public void attackPlayer() {

    }

    public void ignite() {

    }

    public void attackAll(Card selectedCard, ArrayList<Card> tableCards) {

    }

}
