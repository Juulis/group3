package controllers;

import app.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CardController {
    @FXML
    private ImageView cardImageView;

    public void initialize() {
    }

    public ImageView getCardImage() {
        return cardImageView;
    }

    public void setCardImage(String url) {
        cardImageView.setImage(new Image(url));
    }

    public void getSelectedCard(MouseEvent e) {
        System.out.println("Selected card: " + ((AnchorPane)e.getSource()).getId());
       // Server.getInstance().msgToGameEngine("attack,2,1");
    }
}
