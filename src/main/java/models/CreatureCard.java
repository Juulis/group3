package models;

public class CreatureCard extends Card {

    private int hp;
    private int defence;
    private int power;
    private boolean tapped;
    private int playedOnRound;
    private int ignRoundCounter;

    public CreatureCard(int id, int attack, int cardEnergy, String cardName, String specialAttack, int hp, int defence, int power, String imgURL) {
        super(id, attack, cardEnergy, cardName, specialAttack, imgURL);
        this.hp = hp;
        this.defence = defence;
        this.power = power;
        ignRoundCounter = 0;
    }

    public int getHp() {
        return hp;
    }

    public int getDefence() {
        return defence;
    }

    public int getPower() {
        return power;
    }

    public void removeHp(int dmg) {
        this.hp -= dmg;
        System.out.println("id:" + id + " hp:" + hp + " atk:" + attack);
    }

    public void increaseIgnRoundCounter() {
        ignRoundCounter += 1;
    }

    public int getIgnRoundCounter() {
        return ignRoundCounter;
    }

    public void setIgniteCounter(int ignRoundCounter) {
        this.ignRoundCounter = ignRoundCounter;
    }

    public boolean isTapped() {
        return tapped;
    }

    public void tap() {
        this.tapped = true;
    }

    public void unTap() {
        this.tapped = false;
    }

    public int getPlayedOnRound() {
        return playedOnRound;
    }

    public void setPlayedOnRound(int playedOnRound) {
        this.playedOnRound = playedOnRound;
    }
}
