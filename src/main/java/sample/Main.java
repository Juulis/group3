package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
   public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
       primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
       primaryStage.show();
   }

<<<<<<< HEAD:src/main/java/sample/Main.java
public class Main {
=======

>>>>>>> 8a090f8e0307df7cfc25cdd2daee2c23d525f9ed:src/sample/Main.java
    public static void main(String[] args) {
        //  launch(args);
    }
}

