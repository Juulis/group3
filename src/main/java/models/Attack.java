package models;

import java.util.ArrayList;

public class Attack {
    Player player;
    GameEngine gameEngine;

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

    public void dualAttack(CreatureCard currentPlayerCard, CreatureCard opponentCardOne, CreatureCard opponentCardTwo) {
        basicAttack(currentPlayerCard, opponentCardOne);
        if (currentPlayerCard.getHp() < 1) {
            System.out.println(currentPlayerCard.getCardName() + " was killed before launching second attack!");
            return;
        }
        basicAttack(currentPlayerCard, opponentCardTwo);
    }

    public void attackPlayer(Card selectedCard, Player opponent) {
        int dmg = selectedCard.getAttack();
        
        opponent.removeHp(dmg);

    }

    public void ignite(Card attackCard,CreatureCard opponentCard) {
        if (attackCard instanceof MagicCard){
            opponentCard.removeHp(2);
            opponentCard.increaseIgnRoundCounter();
        }else {
            System.out.println("The attacked card is not of type magic" );
        }

    }

    public void attackAll(Card selectedCard, ArrayList<Card> tableCards) {
        int dmg = selectedCard.getAttack();
        for(Card card : tableCards){
            ((CreatureCard) card).removeHp(dmg);
        }
    }
}
