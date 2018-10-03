package sample.LoginApp.Control;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeControl implements Initializable {
    @FXML
    private JFXButton btnGiaoVien;
    @FXML
    private JFXToolbar toolBarR;
    @FXML
    private JFXButton btnLop;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private AnchorPane homePane;
    @FXML
    private Label lblWelcome;
    @FXML
    private Pane panLogo;
    @FXML
    private JFXButton btnSinhVien;
    @FXML
    private JFXButton btnAccount;
    @FXML
    private JFXButton btnKhoa;
    @FXML
    private Pane panStatus;
    @FXML
    private Label lblStatus;
    @FXML
    private AnchorPane panScence;
    AnchorPane homeP;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLblWelcome(ControlLogin.getInstance().userName());
        try {
            this.createPage(homeP,"../xmlflile/TableSinhVien.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void setLblWelcome(String str){
        this.lblWelcome.setText("Welcome, "+str+"!  ");
    }

    @FXML
    private void handClick(ActionEvent event){
        if(event.getSource()==btnSinhVien){
            lblStatus.setText("Sinh Viên");
            panStatus.setBackground(new Background(new BackgroundFill(Color.rgb(196,82,110),CornerRadii.EMPTY,Insets.EMPTY)));
            try {
                this.createPage(homeP,"../xmlflile/TableSinhVien.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(event.getSource()==btnGiaoVien){
            lblStatus.setText("Giáo Viên");
            panStatus.setBackground(new Background(new BackgroundFill(Color.rgb(160,70,193),CornerRadii.EMPTY,Insets.EMPTY)));
            try {
                this.createPage(homeP,"../xmlflile/TableGiaoVien.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(event.getSource()==btnLop){
            lblStatus.setText("Lớp");
            panStatus.setBackground(new Background(new BackgroundFill(Color.rgb(48,121,150),CornerRadii.EMPTY,Insets.EMPTY)));
            try {
                this.createPage(homeP,"../xmlflile/TableLop.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(event.getSource()==btnKhoa){
            lblStatus.setText("Khoa");
            panStatus.setBackground(new Background(new BackgroundFill(Color.rgb(191,137,76),CornerRadii.EMPTY,Insets.EMPTY)));
            try {
                this.createPage(homeP,"../xmlflile/TableKhoa.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (event.getSource()==btnAccount){
            lblStatus.setText("Tài Khoản");
            panStatus.setBackground(new Background(new BackgroundFill(Color.rgb(76,150,61),CornerRadii.EMPTY,Insets.EMPTY)));
            try {
                this.createPage(homeP,"../xmlflile/Account.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void setNode(Node node){
        panScence.getChildren().clear();
        panScence.getChildren().add(node);
    }
    private void createPage(AnchorPane home,String loc) throws IOException {
        home = FXMLLoader.load(getClass().getResource(loc));
        setNode(home);
    }

    @FXML
    void logOutAccount(ActionEvent event) {
        btnLogout.getScene().getWindow().hide();
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
