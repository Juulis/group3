package models;

public class MagicCard extends Card{

    public MagicCard(int id, int attack, int cardEnergy, String cardName, String specialAttack, String imgURL){
        super(id, attack, cardEnergy, cardName, specialAttack, imgURL);
    }
//To seperate CreatureCard from MagicCard
}
