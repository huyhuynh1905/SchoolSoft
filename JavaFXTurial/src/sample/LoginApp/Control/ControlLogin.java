package sample.LoginApp.Control;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.LoginApp.model.LoginModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControlLogin implements Initializable {

    public LoginModel loginModel = new LoginModel();
    @FXML
    private JFXButton btnCloselogin;
    @FXML
    private PasswordField txtPass;
    @FXML
    private JFXButton btnSignH;
    @FXML
    private TextField txtUser;
    @FXML
    private JFXButton btnAbout;
    @FXML
    private JFXButton btnLoginH;
    @FXML
    private Label lblWaring;
    private static ControlLogin instance;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void btnLoginH(ActionEvent e){
        try {
            if (loginModel.isLogin(txtUser.getText(),txtPass.getText())){
                btnLoginH.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("../xmlflile/Home.fxml"));
                Stage home = new Stage();
                Scene scene2 = new Scene(root);
                home.setScene(scene2);
                home.show();
                home.setResizable(false);
            }
            else {
                lblWaring.setText("Sai User and Password!");
                txtPass.setText("");
            }
        }
        catch (IOException e1){
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
//    @FXML
//    void btnSignUpH(ActionEvent event) {
//        btnLoginH.getScene().getWindow().hide();
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("../xmlflile/Register.fxml"));
//            Stage signup = new Stage();
//            Scene scene1 =new Scene(root);
//            signup.setScene(scene1);
//            signup.show();
//            signup.setResizable(false);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    @FXML
    void btnSetAbout(ActionEvent event) {
        btnAbout.getScene().getWindow().hide();
        try {
            Parent root1 = FXMLLoader.load(getClass().getResource("../xmlflile/Ablout.fxml"));
            Stage signup = new Stage();
            Scene scene1 =new Scene(root1);
            signup.setScene(scene1);
            signup.show();
            signup.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ControlLogin(){
        instance = this;
    }
    public static ControlLogin getInstance(){
        return instance;
    }
    public String userName(){
        return txtUser.getText();
    }
}

