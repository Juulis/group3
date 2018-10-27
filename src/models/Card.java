package models;

import java.util.Random;

public class Card {
    private int hp;
    Random rand = new Random();

    public Card() {
        this.hp = randomizeHp();
    }

    /**
     * Determines hp of card
     * @return random nr between 1-20
     */
    public int randomizeHp() {
        int min = 1;
        int max = 20;
        return rand.nextInt(max) + min;
    }


    public int getHp() {
        return hp;
    }

    /**
     * When a card gets attacked, remove the amount of attack from the card
     * @param amount of attack
     * @return healthPoints after attack
     */
    public int removeHp(int hpToRemove) {
        return hp -= hpToRemove;
    }
}
