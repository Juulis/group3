package models;

import java.util.Random;

public class Card {
    private int hp;
    private boolean tapped;
    private Random rand = new Random();

    public Card() {
        this.hp = randomizeHp();
        this.tapped = false;
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
     * @param hpToRemove of attack
     * @return healthPoints after attack
     */
    public void removeHp(int hpToRemove) {
         hp -= hpToRemove;
    }

    public void tap() {
        this.tapped = true;
    }

    public void unTap() {
        this.tapped = false;
    };

    public boolean getTapped() {
        return this.tapped;
    }

    public int attack(){

        return  rand.nextInt(6)+1;
    }
}
