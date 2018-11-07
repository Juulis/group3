package models;

public class CreatureCard extends Card {

    private int hp;
    private int defence;
    private int power;
    private boolean tapped;

    public CreatureCard(String name, int energy, int attack, String specialAttack, int hp, int defence, int power){

        super(name, energy, attack, specialAttack);
        this.hp = hp;
        this.defence = defence;
        this.power = power;
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

    public void removeHp( int dmg){

        this.hp -= dmg;
    }

    public boolean isTapped() {
        return tapped;
    }

    public void tap(){
        this.tapped = true;
    }

    public void unTap(){
        this.tapped = false;
    }
}
