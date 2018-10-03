package sample.LoginApp.Control;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutControll {

    @FXML
    private JFXButton btnOK;

    @FXML
    void btnOK(ActionEvent event) {
        btnOK.getScene().getWindow().hide();
        try {
            Stage login = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../xmlflile/Login.fxml"));
            Scene scene = new Scene(root);
            login.setScene(scene);
            login.show();
            login.setResizable(false);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
