package sample.LoginApp.Control;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpControl {

    @FXML
    private JFXButton btnLoginR;

    @FXML
    private TextField txtPhoneR;

    @FXML
    private PasswordField txtPassR;

    @FXML
    private TextField txtUserR;

    @FXML
    private JFXButton btnSignUpR;

    @FXML
    public void setBtnSignUpR(ActionEvent e){
        System.out.println("Đã đăng kí thành công!");
    }
    @FXML
    public void setBtnLoginR(ActionEvent e){
        btnSignUpR.getScene().getWindow().hide();
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
