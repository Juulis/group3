package models;

import java.util.Random;

public class Card {
    private int hp;
    private Random rand = new Random();

    public Card() {
        this.hp = randomizeHp();
    }

    /**
     * Determines hp of card
     * @return random nr between 1-20
     */
    public int randomizeHp() {
        return rand.nextInt(20) + 1;
    }

    public int getHp() {
        return hp;
    }

    /**
     * When a card gets attacked, remove the amount of attack from the card
     * @param amount of attack
     * @return healthPoints after attack
     */
    public void removeHp(int hpToRemove) {
         hp -= hpToRemove;
    }
}
