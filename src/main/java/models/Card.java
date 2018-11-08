package models;

import java.util.Random;

public class Card {

    String cardName;
    int cardEnergy;
    int attack;
    String specialAttack;


    public Card() {

        this.attack = attack;
        this.cardEnergy = cardEnergy;
        this.cardName = cardName;
        this.specialAttack = specialAttack;
    }


    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardEnergy() {
        return cardEnergy;
    }

    public void setCardEnergy(int cardEnergy) {
        this.cardEnergy = cardEnergy;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public String getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(String specialAttack) {
        this.specialAttack = specialAttack;
    }

}
