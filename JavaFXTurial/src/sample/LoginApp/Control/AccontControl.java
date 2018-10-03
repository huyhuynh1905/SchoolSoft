package sample.LoginApp.Control;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import sample.LoginApp.database.SQLiteConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccontControl implements Initializable {
    @FXML
    private JFXTextField txtPhone;
    @FXML
    private JFXButton btnOk;
    @FXML
    private JFXPasswordField txtNewPass;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnChange;
    @FXML
    private JFXTextField txtUser;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXPasswordField txtOldPass;
    @FXML
    private JFXPasswordField txtConfirm;
    @FXML
    private JFXButton btnHuy;
    @FXML
    private JFXButton btnXoa;
    @FXML
    private ListView<String> lvUser;
    private String oldPass;
    final ObservableList<String> listuser = FXCollections.observableArrayList();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataInfo();
        setAdmin();
        if (ControlLogin.getInstance().userName().equals("Admin")){
            setListUser();
            delAccount();
        }
        changePass();
        addUser();
        huyThaoTac();

    }


    private void setListUser(){
        listuser.clear();
        Connection conn = SQLiteConnection.connector();
        PreparedStatement pre;
        ResultSet rs;
        String u = "select User from ADMIN where User not in (select User from ADMIN where User = 'Admin')";
        try{
            pre = conn.prepareStatement(u);
            rs = pre.executeQuery();
            while (rs.next()){
                listuser.add(rs.getString("User"));
            }
            lvUser.setItems(listuser);
            pre.close();
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void clear(){
        txtUser.clear();
        txtEmail.clear();
        txtConfirm.clear();
        txtPhone.clear();
        txtNewPass.clear();
        txtOldPass.clear();
    }
    private void enableTextBut(){
        txtUser.setEditable(true);
        txtEmail.setEditable(true);
        txtConfirm.setEditable(true);
        txtPhone.setEditable(true);
        txtNewPass.setEditable(true);
        txtOldPass.setEditable(true);
        btnOk.setDisable(false);
    }
    private void enablePassBut(){
        txtConfirm.setEditable(true);
        txtNewPass.setEditable(true);
        txtOldPass.setEditable(true);
        btnOk.setDisable(false);
    }
    private void disableTextBut(){
        txtUser.setEditable(false);
        txtEmail.setEditable(false);
        txtConfirm.setEditable(false);
        txtPhone.setEditable(false);
        txtNewPass.setEditable(false);
        txtOldPass.setEditable(false);
        btnOk.setDisable(true);
    }

    private void dataInfo(){
        Connection conn = SQLiteConnection.connector();
        PreparedStatement pre;
        ResultSet rs;
        String EmailPhone = "select Email,Phone from ADMIN where User = ?";
        try{
            pre = conn.prepareStatement(EmailPhone);
            pre.setString(1,ControlLogin.getInstance().userName());
            rs = pre.executeQuery();
            txtUser.setText(ControlLogin.getInstance().userName());
            txtEmail.setText(rs.getString(1));
            txtPhone.setText(rs.getString(2));
            pre.close();
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setAdmin(){
        if(ControlLogin.getInstance().userName().equals("Admin")){
            btnAdd.setDisable(false);
            btnXoa.setDisable(false);
        }
    }
    private String selectOldPass(){
        Connection conn = SQLiteConnection.connector();
        PreparedStatement pre;
        ResultSet rs;
        String EmailPhone = "select Pass from ADMIN where User = ?";
        try{
            pre = conn.prepareStatement(EmailPhone);
            pre.setString(1,ControlLogin.getInstance().userName());
            rs = pre.executeQuery();
            txtUser.setText(ControlLogin.getInstance().userName());
            oldPass = rs.getString(1);
            //return oldPass;
            pre.close();
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return oldPass;

    }
    private void clearPass(){
        txtNewPass.clear();
        txtOldPass.clear();
        txtConfirm.clear();
    }
    private void changePass(){
        btnChange.setOnAction(e->{
            enablePassBut();
            btnHuy.setDisable(false);
            btnOk.setOnAction(event->{
                if (selectOldPass().equals(txtOldPass.getText())&&txtNewPass.getText().equals(txtConfirm.getText())){
                    Connection connection = SQLiteConnection.connector();
                    PreparedStatement pre1;
                    String change = "update ADMIN set Pass = ? where User = ?";
                    try{
                        pre1 = connection.prepareStatement(change);
                        pre1.setString(1,txtNewPass.getText());
                        pre1.setString(2,txtUser.getText());
                        pre1.executeUpdate();
                        dataInfo();
                        pre1.close();
                        clearPass();
                        btnHuy.setDisable(true);
                        Alert alertp = new Alert(Alert.AlertType.INFORMATION);
                        alertp.setHeaderText(null);
                        alertp.setContentText("Đổi Thành Công!");
                        alertp.showAndWait();
                    }catch (Exception m){
                        m.printStackTrace();
                    }
                }
                else if (!selectOldPass().equals(txtOldPass.getText())){
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setHeaderText(null);
                    alert1.setContentText("Sai Old Pass!");
                    alert1.showAndWait();
                }
                else if (!txtNewPass.getText().equals(txtConfirm.getText())){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setHeaderText(null);
                    alert2.setContentText("Pass mới phải giống nhau!");
                    alert2.showAndWait();
                }
            });
        });
    }
    private void addUser(){
        btnAdd.setOnAction(e->{
            clear();
            enableTextBut();
            btnHuy.setDisable(false);
            txtOldPass.setDisable(true);
            btnOk.setOnAction(event -> {
                if (checkNull()&&checkUser()) {
                    if (txtNewPass.getText().equals(txtConfirm.getText())) {
                        Connection conn = SQLiteConnection.connector();
                        PreparedStatement pre;
                        String in = "insert into ADMIN (User,Pass,Email,Phone) values (?,?,?,?)";
                        try {
                            pre = conn.prepareStatement(in);
                            pre.setString(1, txtUser.getText());
                            pre.setString(2, txtNewPass.getText());
                            pre.setString(3, txtEmail.getText());
                            pre.setString(4, txtPhone.getText());
                            pre.executeUpdate();
                            pre.close();
                            disableTextBut();
                            dataInfo();
                            clear();
                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setHeaderText(null);
                            alert2.setContentText("Thêm thành công!");
                            alert2.showAndWait();
                            btnHuy.setDisable(true);
                            setListUser();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setHeaderText(null);
                        alert2.setContentText("Pass phải giống nhau!");
                        alert2.showAndWait();
                    }
                }
            });
        });
    }
    private void huyThaoTac(){
        btnHuy.setOnAction(e->{
            disableTextBut();
            txtNewPass.clear();
            txtOldPass.clear();
            txtConfirm.clear();
            dataInfo();
            btnHuy.setDisable(true);
        });
    }
    private boolean checkNull(){
        if (txtUser.getText().isEmpty()|txtEmail.getText().isEmpty()|txtPhone.getText().isEmpty()){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setHeaderText(null);
            alert2.setContentText("Đệ nghị nhập đầy đủ thông tin!");
            alert2.showAndWait();
            return false;
        }
        if (txtNewPass.getText().isEmpty()|txtConfirm.getText().isEmpty()){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setHeaderText(null);
            alert2.setContentText("Nhập Pass!");
            alert2.showAndWait();
            return false;
        }
        return true;
    }
    private boolean checkUser(){
        Connection conn = SQLiteConnection.connector();
        PreparedStatement pre;
        ResultSet rs;
        String user = "select User from ADMIN";
        try{
            pre = conn.prepareStatement(user);
            rs = pre.executeQuery();
            while (rs.next()){
                if(txtUser.getText().equals(rs.getString(1))){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setHeaderText(null);
                    alert2.setContentText("Username đã tốn tại!");
                    alert2.showAndWait();
                    return false;
                }
            }
            pre.close();
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;

    }


    private void delAccount(){
        btnXoa.setOnAction(e->{
            if (lvUser.getSelectionModel().getSelectedItems()==null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Bạn chưa chọn user muốn xoá");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Bạn có muốn xoá tài khoản "+lvUser.getSelectionModel().getSelectedItems()+" ?");
                ButtonType Yes = new ButtonType("Yes",ButtonBar.ButtonData.YES);
                ButtonType No = new ButtonType("No",ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(Yes,No);
                Optional<ButtonType> res = alert.showAndWait();
                if (res.get()==Yes){
                    Connection conn = SQLiteConnection.connector();
                    PreparedStatement pre;
                    String del="delete from ADMIN where User = ?";
                    try{
                        pre = conn.prepareStatement(del);
                        pre.setString(1,lvUser.getSelectionModel().getSelectedItem());
                        pre.executeUpdate();
                        pre.close();
                        setListUser();
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setHeaderText(null);
                        alert1.setContentText("Đã xoá thành công!");
                        alert1.showAndWait();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
