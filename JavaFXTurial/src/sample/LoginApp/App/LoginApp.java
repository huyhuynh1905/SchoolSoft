package sample.LoginApp.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../xmlflile/Login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            //primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
            primaryStage.setResizable(false);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
