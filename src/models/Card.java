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
        int min = 1;
        int max = 20;
        return rand.nextInt(max) + min;
    }

    public void setHp(int hp) {
        this.hp = hp;
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
        System.out.println(hpToRemove);
         hp -= hpToRemove;
    }
}
