package models;

public class Attack {

    public Attack() {
    }

    public void basicAttack(CreatureCard currentPlayerCard, CreatureCard opponentCard) {
        int currentPlayerAttack = currentPlayerCard.getAttack();
        int opponentPlayerAttack;
        try {
            opponentPlayerAttack = opponentCard.getAttack();
        } catch (NullPointerException e) {
            System.out.println("Attacking Opponent!");
            playerAttack = true;
            opponentPlayerAttack = 0;
        }

        int damage = currentPlayerAttack - opponentPlayerAttack;
        damage = Math.abs(damage);

        if (currentPlayerAttack > opponentPlayerAttack) {
            int amountOfAttack = currentPlayerAttack - opponentPlayerAttack;
            System.out.println("You get to attack your opponent with: " + amountOfAttack + " dmg");
        } else {
            int amountOfAttack = opponentPlayerAttack - currentPlayerAttack;
            System.out.println("Your opponent gets to attack you with : " + amountOfAttack + " dmg");
        }



            if (isCardKilled((CreatureCard) currentPlayerCard)) {
                currentPlayer.sendToGraveyard(currentPlayerCard);
                System.out.println("You lost a card\n");
                System.out.println("---------------------------------------------------------------------");
            }
        }

        currentPlayerCard.tap();
        checkPlayerHealth();
    }


    public void dualAttack() {

    }

    public void attackPlayer() {

    }

    public void ignite() {

    }

    public void attackAllMC() {

    }

}
