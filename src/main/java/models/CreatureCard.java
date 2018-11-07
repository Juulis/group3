package models;

public class CreatureCard extends Card {

    private int hp;
    private int attack;
    private int defence;
    private int power;

    public CreatureCard(int hp, int attack, int defence, int power){

        super();
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
        this.power = power;
    }

    public int getHp() {
        return hp;
    }


    public int getAttack() {
        return attack;
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
}
