package models;


public class Card {
    int id;
    String cardName;
    int cardEnergy;
    int attack;
    String specialAttack;
    String imgURL;


    public Card(int id, int attack, int cardEnergy, String cardName, String specialAttack, String imgURL) {
        this.id = id;
        this.attack = attack;
        this.cardEnergy = cardEnergy;
        this.cardName = cardName;
        this.specialAttack = specialAttack;
        this.imgURL = imgURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
