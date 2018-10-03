package sample.LoginApp.Control;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import sample.LoginApp.database.SQLiteConnection;
import sample.LoginApp.model.GiaoVienModel;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class KhoaControl implements Initializable {
    @FXML
    private TableView<GiaoVienModel> tablegv;
    @FXML
    private JFXComboBox<String> comKhoa;
    @FXML
    private JFXComboBox<String> comHienThi;
    @FXML
    private TableColumn<GiaoVienModel, String> clkhoa;
    @FXML
    private TableColumn<GiaoVienModel, String> clmakh;
    @FXML
    private TableColumn<GiaoVienModel, String> cltenkh;
    @FXML
    private JFXButton btnThemkh;
    @FXML
    private JFXTextField txtMakh;
    @FXML
    private TableColumn<GiaoVienModel, String> clhokh;
    @FXML
    private JFXTextField txtTenkh;
    @FXML
    private TableColumn<GiaoVienModel, String> clsexkh;
    @FXML
    private JFXButton btnSuakh;
    @FXML
    private TableColumn<GiaoVienModel, String> clngaysinhkh;
    @FXML
    private JFXTextField txtPhonekh;
    @FXML
    private JFXButton btnUpdateKh;
    @FXML
    private JFXTextField txtHokh;
    @FXML
    private RadioButton radNukh;
    @FXML
    private JFXButton btnXoakh;
    @FXML
    private JFXTextField txtDiachikh;
    @FXML
    private DatePicker bDatekh;
    @FXML
    private TableColumn<GiaoVienModel, String> clphonekh;
    @FXML
    private JFXTextField txtFindkh;
    @FXML
    private JFXButton btnRefKh;
    @FXML
    private JFXButton btnAddkh;
    @FXML
    private TableColumn<GiaoVienModel, String> cldiachikh;
    @FXML
    private RadioButton radNamkh;
    private String lblsex;
    private final String pattern = "dd/MM/yyyy";
    final ObservableList<String> comboxhienthisv = FXCollections.observableArrayList();
    private ObservableList<GiaoVienModel> obgv = FXCollections.observableArrayList();
    final ObservableList<String> combox = FXCollections.observableArrayList();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formatdayPicker();
        filTergv();
        tableItemClick();
        fillComboBox();
        fillComBoxKhoa();
        btnXoakh.setOnAction(e->{
            deleteGiaoVien();
        });
        btnRefKh.setOnAction(e->{
            try {
                dataBaseGVKhoa();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }


    private void fillComBoxKhoa(){
        Connection connbox = SQLiteConnection.connector();
        PreparedStatement precombox;
        ResultSet rscbox;
        String khoadb = "select DISTINCT MaKhoaGV from GIAOVIEN order by MaKhoaGV asc";
        try {
            precombox = connbox.prepareStatement(khoadb);
            rscbox = precombox.executeQuery();
            while (rscbox.next()){
                comboxhienthisv.add(rscbox.getString("MaKhoaGV"));
            }
            rscbox.close();
            precombox.close();

        }
        catch (Exception e){

        }
        comHienThi.setItems(comboxhienthisv);
    }
    private void dataBaseGVKhoa() throws SQLException {
        obgv.clear();
        Connection conngv = SQLiteConnection.connector();
        PreparedStatement preparegv =null;
        ResultSet rsi = null;
        String gvstr = "select *  from GIAOVIEN where MaKhoaGV=?";
        try{
            preparegv = conngv.prepareStatement(gvstr);
            preparegv.setString(1,comHienThi.getSelectionModel().getSelectedItem());
            rsi = preparegv.executeQuery();
            while (rsi.next()){
                obgv.add(new GiaoVienModel(rsi.getString(1),rsi.getString(2),rsi.getString(3),
                        rsi.getString(4),rsi.getString(5),rsi.getString(6),
                        rsi.getString(7),rsi.getString("MaKhoaGV")));
            }
            rsi.close();
            preparegv.close();
            conngv.close();
        }
        catch (Exception e){

        }
        clmakh.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("IdGv")); //getIdSV
        clhokh.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("HoGv")); //getHoSV
        cltenkh.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("TenGv")); //getTenSV
        clngaysinhkh.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("NgaysinhGv")); //getNgaysinhSv
        clsexkh.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("SexGv")); //getSv
        clphonekh.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("PhoneGv")); //getPhoneSv
        cldiachikh.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("DiachiGv")); //getDiachiSv
        clkhoa.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("KhoaGv"));
        tablegv.setItems(obgv);
    }
    private void filTergv(){
        FilteredList<GiaoVienModel> filtergv = new FilteredList<>(obgv, e ->true);
        txtFindkh.setOnAction(e -> {
            txtFindkh.textProperty().addListener(((observable, oldValue, newValue) -> {
                filtergv.setPredicate((Predicate<? super  GiaoVienModel>) gv->{
                    if(newValue == null||newValue.isEmpty()){
                        return true;
                    }
                    String lowerCase = newValue.toLowerCase();
                    if (gv.getIdGv().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                    else if(gv.getTenGv().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                    else if (gv.getHoGv().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                    return false;
                });
            }));
            SortedList<GiaoVienModel> sort = new SortedList<>(filtergv);
            sort.comparatorProperty().bind(tablegv.comparatorProperty());
            tablegv.setItems(sort);
        });
    }

    private void formatdayPicker(){
        bDatekh.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                bDatekh.setPromptText("Ngày sinh");
            }

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }
    private void clearText(){
        txtHokh.clear();
        txtDiachikh.clear();
        txtTenkh.clear();
        txtMakh.clear();
        txtPhonekh.clear();
        bDatekh.setValue(null);
        radNukh.setSelected(false);
        radNamkh.setSelected(false);
    }

    private void themGiaoVien() throws SQLException {
        if (emtyFeild()) {
            if (radNamkh.isSelected()) {
                lblsex = radNamkh.getText();
            } else if (radNukh.isSelected()) {
                lblsex = radNukh.getText();
            }
            Connection conngv = SQLiteConnection.connector();
            PreparedStatement preparegv1 = null;
            String gvstr1 = "insert into GIAOVIEN (MaGV,HoGV,TenGV,NgaySinhGV,GioiTinhGV,PhoneGV,DiaChiGV,MaKhoaGV) values (?,?,?,?,?,?,?,?)";
            try {
                preparegv1 = conngv.prepareStatement(gvstr1);
                preparegv1.setString(1, txtMakh.getText());
                preparegv1.setString(2, txtHokh.getText());
                preparegv1.setString(3, txtTenkh.getText());
                preparegv1.setString(4, bDatekh.getEditor().getText());
                preparegv1.setString(5, lblsex);
                preparegv1.setString(6, txtPhonekh.getText());
                preparegv1.setString(7, txtDiachikh.getText());
                preparegv1.setString(8, comKhoa.getSelectionModel().getSelectedItem());
                preparegv1.executeUpdate();
                dataBaseGVKhoa();
                clearText();

                preparegv1.close();
                conngv.close();
            } catch (Exception e) {

            }
            finally {
            }
        }
    }


    private void fillComboBox(){
        Connection connbox = SQLiteConnection.connector();
        PreparedStatement precombox;
        ResultSet rscbox;
        String khoadb = "select DISTINCT MaKhoaGV from GIAOVIEN order by MaKhoaGV asc";
        try {
            precombox = connbox.prepareStatement(khoadb);
            rscbox = precombox.executeQuery();
            while (rscbox.next()){
                combox.add(rscbox.getString("MaKhoaGV"));
            }
            rscbox.close();
            precombox.close();

        }
        catch (Exception e){

        }
        comKhoa.setItems(combox);
    }

    private boolean emtyFeild(){
        if (txtMakh.getText().isEmpty()|txtHokh.getText().isEmpty()|
                txtTenkh.getText().isEmpty()|txtPhonekh.getText().isEmpty()|txtDiachikh.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị nhập đầy đủ thông tin!!");
            alert.showAndWait();
            return false;
        }
        if ( bDatekh.getEditor().getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị nhập Ngày Sinh");
            alert.showAndWait();
            return false;
        }
        if (radioCheck()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị chọn Giới Tính!");
            alert.showAndWait();
            return false;
        }
        if (comKhoa.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị chọn Khoa!");
            alert.showAndWait();
            return false;
        }

        return true;
    }
    private boolean radioCheck(){
        if (radNamkh.isSelected() || radNukh.isSelected()){
            return false;
        }
        else
            return true;
    }

    private void tableItemClick(){
        tablegv.setOnMouseClicked(e->{
            if (tablegv.getSelectionModel().getSelectedItem()!=null){
                try{
                    GiaoVienModel gv = tablegv.getSelectionModel().getSelectedItem();
                    String qr = "select * from GIAOVIEN where MaGV=?";
                    Connection conn = SQLiteConnection.connector();
                    PreparedStatement pre;
                    ResultSet rs;
                    pre = conn.prepareStatement(qr);
                    pre.setString(1,gv.getIdGv());
                    rs = pre.executeQuery();
                    while (rs.next()){
                        txtMakh.setText(rs.getString(1));
                        txtHokh.setText(rs.getString(2));
                        txtTenkh.setText(rs.getString(3));
                        bDatekh.getEditor().setText(rs.getString(4));
                        if (rs.getString(5).equals("Nam")){
                            radNamkh.setSelected(true);
                        }
                        else radNukh.setSelected(true);
                        txtPhonekh.setText(rs.getString(6));
                        txtDiachikh.setText(rs.getString(7));
                    }
                    rs.close();
                    pre.close();
                    conn.close();

                }
                catch (Exception m){
                    m.printStackTrace();
                }
            }});
    }
    private void deleteGiaoVien(){
        if (!txtMakh.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Bạn có muốn xoá " + txtHokh.getText() + " " + txtTenkh.getText() + "\nKhỏi danh sách?");
            Optional<ButtonType> action = alert.showAndWait();
            Connection conn = SQLiteConnection.connector();
            PreparedStatement pred =null;
            if (action.get() == ButtonType.OK) {
                try {
                    String qr = "delete from GIAOVIEN where MaGV=?";
                    pred = conn.prepareStatement(qr);
                    pred.setString(1, txtMakh.getText());
                    pred.executeUpdate();
                    pred.close();
                    dataBaseGVKhoa();
                    clearText();
                    editDisableTxt();
                    conn.close();
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Chọn trong danh sách để xoá!");
            alert.showAndWait();
        }
    }

    private void editEnableTxt(){
        txtMakh.setEditable(true);
        txtHokh.setEditable(true);
        txtTenkh.setEditable(true);
        txtPhonekh.setEditable(true);
        txtDiachikh.setEditable(true);
        radNukh.setDisable(false);
        radNamkh.setDisable(false);
        bDatekh.setDisable(false);
        comKhoa.setDisable(false);
        btnUpdateKh.setDisable(false);
        btnAddkh.setDisable(false);
    }
    private void editDisableTxt(){
        txtMakh.setEditable(false);
        txtHokh.setEditable(false);
        txtTenkh.setEditable(false);
        txtPhonekh.setEditable(false);
        txtDiachikh.setEditable(false);
        radNukh.setDisable(true);
        radNamkh.setDisable(true);
        bDatekh.setDisable(true);
        comKhoa.setDisable(true);
        btnUpdateKh.setDisable(true);
        btnAddkh.setDisable(true);
    }

    private boolean maGvisCheck() throws SQLException {
        PreparedStatement preCk=null;
        ResultSet rsCk = null;
        Connection connection = SQLiteConnection.connector();
        String gvstr1 = "select ? from GIAOVIEN";
        try {
            preCk = connection.prepareStatement(gvstr1);
            preCk.setString(1,txtMakh.getText());
            rsCk = preCk.executeQuery();
            if (rsCk.next()){
                return  true;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không tìm thấy Mã Giáo Viên!");
                alert.showAndWait();
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
        finally {
            rsCk.close();
            preCk.close();
        }
    }
    private void suaGiaoVien() throws SQLException {
        if (emtyFeild()) {
            if (radNamkh.isSelected()) {
                lblsex = radNamkh.getText();
            } else if (radNukh.isSelected()) {
                lblsex = radNukh.getText();
            }
            Connection conngv = SQLiteConnection.connector();
            PreparedStatement preparegvs = null;
            String str2 = "update GIAOVIEN set HoGV = ?,TenGV = ?,NgaySinhGV = ?,GioiTinhGV = ?,PhoneGV = ?,DiaChiGV = ?,MaKhoaGV = ? where MaGV = ?";
            try {
                preparegvs = conngv.prepareStatement(str2);
                preparegvs.setString(1, txtHokh.getText());
                preparegvs.setString(2, txtTenkh.getText());
                preparegvs.setString(3, bDatekh.getEditor().getText());
                preparegvs.setString(4, lblsex);
                preparegvs.setString(5, txtPhonekh.getText());
                preparegvs.setString(6, txtDiachikh.getText());
                preparegvs.setString(7, comKhoa.getSelectionModel().getSelectedItem());
                preparegvs.setString(8, txtMakh.getText());
                preparegvs.executeUpdate();
                dataBaseGVKhoa();
                preparegvs.close();
                conngv.close();
                clearText();

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {

            }
        }
    }

    @FXML
    void onThemSuaKh(ActionEvent event) {
        if (event.getSource()==btnSuakh){
            editEnableTxt();
            btnAddkh.setDisable(true);
            btnUpdateKh.setOnAction(e->{
                try {
                    if (maGvisCheck()){
                        suaGiaoVien();
                        editDisableTxt();}
                }
                catch (SQLException s){
                    s.printStackTrace();
                }
            });
        }
        if (event.getSource()==btnThemkh){
            clearText();
            editEnableTxt();
            btnUpdateKh.setDisable(true);
            btnAddkh.setOnAction(e ->{
                try {
                    themGiaoVien();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                editDisableTxt();
            });
        }
    }
}
