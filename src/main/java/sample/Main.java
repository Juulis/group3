package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/menu/menu.fxml"));
        primaryStage.setTitle("This is not a chess inspired game!!");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.show();
    }

   public static void main(String[] args) {
        launch(args);
    }
}

/*import models.*;

public class Main {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.startGame();
    }
}*/
