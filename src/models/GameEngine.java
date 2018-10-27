package models;

import java.util.Scanner;

public class GameEngine {

    Player p1 = new Player();
    Player p2 = new Player();

    public void startGame() {
        playerInput(p1);
    }

    private void playerInput(Player p) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Here is your choices: \n" +
                "1. Show table \n" +
                "2. Play card on hand \n" +
                "3. Attack a card \n" +
                "4. End Turn");
        int input = sc.nextInt();
        switch (input) {
            case 1:
                break;
            case 2:
                System.out.println("what card do you want to play out?");
                int playCard = sc.nextInt();
                p.playCard(playCard);
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
}
