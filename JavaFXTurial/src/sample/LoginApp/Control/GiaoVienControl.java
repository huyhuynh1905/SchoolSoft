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

public class GiaoVienControl implements Initializable {
    @FXML
    private JFXButton btnXoagv;
    @FXML
    private JFXTextField txtHogv;
    @FXML
    private JFXTextField txtDiachigv;
    @FXML
    private RadioButton radNugv;
    @FXML
    private DatePicker bDategv;
    private final String pattern = "dd/MM/yyyy";
    @FXML
    private JFXComboBox<String> comKhoa;
    private String lblsex;
    @FXML
    private JFXTextField txtFindgv;
    @FXML
    private JFXTextField txtTengv;
    @FXML
    private RadioButton radNamgv;
    @FXML
    private JFXTextField txtMagv;
    @FXML
    private JFXButton btnThemgv;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnOKgv;
    @FXML
    private JFXTextField txtPhonegv;
    @FXML
    private JFXButton btnSuagv;
    @FXML
    private TableView<GiaoVienModel> tablegv;
    @FXML
    private TableColumn<GiaoVienModel, String> diachigv;
    @FXML
    private TableColumn<GiaoVienModel, String> tengv;
    @FXML
    private TableColumn<GiaoVienModel, String> khoagv;
    @FXML
    private TableColumn<GiaoVienModel, String> ngaysinhgv;
    @FXML
    private TableColumn<GiaoVienModel, String> hogv;
    @FXML
    private TableColumn<GiaoVienModel, String> sexgv;
    @FXML
    private TableColumn<GiaoVienModel, String> idgv;
    @FXML
    private TableColumn<GiaoVienModel, String> phonegv;
    private ObservableList<GiaoVienModel> obgv = FXCollections.observableArrayList();
    final ObservableList<String> combox = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formatdayPicker();
        try{
            dataBaseGVinput();
            fillComboBox();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        filTergv();
        tableItemClick();
        btnXoagv.setOnAction(e->{
                deleteGiaoVien();
        });

    }

    private void dataBaseGVinput() throws SQLException {
        obgv.clear();
        Connection conngv = SQLiteConnection.connector();
        PreparedStatement preparegv =null;
        ResultSet rsi = null;
        String gvstr = "select *  from GIAOVIEN";
        try{
            preparegv = conngv.prepareStatement(gvstr);
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
        idgv.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("IdGv")); //getIdSV
        hogv.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("HoGv")); //getHoSV
        tengv.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("TenGv")); //getTenSV
        ngaysinhgv.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("NgaysinhGv")); //getNgaysinhSv
        sexgv.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("SexGv")); //getSv
        phonegv.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("PhoneGv")); //getPhoneSv
        diachigv.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("DiachiGv")); //getDiachiSv
        khoagv.setCellValueFactory(new PropertyValueFactory<GiaoVienModel,String>("KhoaGv"));
        tablegv.setItems(obgv);
    }

    private void filTergv(){
        FilteredList<GiaoVienModel> filtergv = new FilteredList<>(obgv, e ->true);
        txtFindgv.setOnAction(e -> {
            txtFindgv.textProperty().addListener(((observable, oldValue, newValue) -> {
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
        bDategv.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                bDategv.setPromptText("Ngày sinh");
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
        txtHogv.clear();
        txtDiachigv.clear();
        txtTengv.clear();
        txtMagv.clear();
        txtPhonegv.clear();
        bDategv.setValue(null);
        radNugv.setSelected(false);
        radNamgv.setSelected(false);
    }

    private void themGiaoVien() throws SQLException{
        if (emtyFeild()) {
            if (radNamgv.isSelected()) {
                lblsex = radNamgv.getText();
            } else if (radNugv.isSelected()) {
                lblsex = radNugv.getText();
            }
            Connection conngv = SQLiteConnection.connector();
            PreparedStatement preparegv1 = null;
            String gvstr1 = "insert into GIAOVIEN (MaGV,HoGV,TenGV,NgaySinhGV,GioiTinhGV,PhoneGV,DiaChiGV,MaKhoaGV) values (?,?,?,?,?,?,?,?)";
            try {
                preparegv1 = conngv.prepareStatement(gvstr1);
                preparegv1.setString(1, txtMagv.getText());
                preparegv1.setString(2, txtHogv.getText());
                preparegv1.setString(3, txtTengv.getText());
                preparegv1.setString(4, bDategv.getEditor().getText());
                preparegv1.setString(5, lblsex);
                preparegv1.setString(6, txtPhonegv.getText());
                preparegv1.setString(7, txtDiachigv.getText());
                preparegv1.setString(8, comKhoa.getSelectionModel().getSelectedItem());
                preparegv1.executeUpdate();
                dataBaseGVinput();
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
        if (txtMagv.getText().isEmpty()|txtHogv.getText().isEmpty()|
                txtTengv.getText().isEmpty()|txtPhonegv.getText().isEmpty()|txtDiachigv.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị nhập đầy đủ thông tin!!");
            alert.showAndWait();
            return false;
        }
        if ( bDategv.getEditor().getText().isEmpty()){
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
        if (radNamgv.isSelected() || radNugv.isSelected()){
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
                    txtMagv.setText(rs.getString(1));
                    txtHogv.setText(rs.getString(2));
                    txtTengv.setText(rs.getString(3));
                    bDategv.getEditor().setText(rs.getString(4));
                    if (rs.getString(5).equals("Nam")){
                        radNamgv.setSelected(true);
                    }
                    else radNugv.setSelected(true);
                    txtPhonegv.setText(rs.getString(6));
                    txtDiachigv.setText(rs.getString(7));
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
            if (!txtMagv.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Bạn có muốn xoá " + txtHogv.getText() + " " + txtTengv.getText() + "\nKhỏi danh sách?");
                Optional<ButtonType> action = alert.showAndWait();
                Connection conn = SQLiteConnection.connector();
                PreparedStatement pred =null;
                if (action.get() == ButtonType.OK) {
                    try {
                        String qr = "delete from GIAOVIEN where MaGV=?";
                        pred = conn.prepareStatement(qr);
                        pred.setString(1, txtMagv.getText());
                        pred.executeUpdate();
                        pred.close();
                        dataBaseGVinput();
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
        txtMagv.setEditable(true);
        txtHogv.setEditable(true);
        txtTengv.setEditable(true);
        txtPhonegv.setEditable(true);
        txtDiachigv.setEditable(true);
        radNugv.setDisable(false);
        radNamgv.setDisable(false);
        bDategv.setDisable(false);
        comKhoa.setDisable(false);
        btnOKgv.setDisable(false);
        btnAdd.setDisable(false);
    }
    private void editDisableTxt(){
        txtMagv.setEditable(false);
        txtHogv.setEditable(false);
        txtTengv.setEditable(false);
        txtPhonegv.setEditable(false);
        txtDiachigv.setEditable(false);
        radNugv.setDisable(true);
        radNamgv.setDisable(true);
        bDategv.setDisable(true);
        comKhoa.setDisable(true);
        btnOKgv.setDisable(true);
        btnAdd.setDisable(true);
    }

    private boolean maGvisCheck() throws SQLException {
        PreparedStatement preCk=null;
        ResultSet rsCk = null;
        Connection connection = SQLiteConnection.connector();
        String gvstr1 = "select ? from GIAOVIEN";
        try {
            preCk = connection.prepareStatement(gvstr1);
            preCk.setString(1,txtMagv.getText());
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
            if (radNamgv.isSelected()) {
                lblsex = radNamgv.getText();
            } else if (radNugv.isSelected()) {
                lblsex = radNugv.getText();
            }
            Connection conngv = SQLiteConnection.connector();
            PreparedStatement preparegvs = null;
            String str2 = "update GIAOVIEN set HoGV = ?,TenGV = ?,NgaySinhGV = ?,GioiTinhGV = ?,PhoneGV = ?,DiaChiGV = ?,MaKhoaGV = ? where MaGV = ?";
            try {
                preparegvs = conngv.prepareStatement(str2);
                preparegvs.setString(1, txtHogv.getText());
                preparegvs.setString(2, txtTengv.getText());
                preparegvs.setString(3, bDategv.getEditor().getText());
                preparegvs.setString(4, lblsex);
                preparegvs.setString(5, txtPhonegv.getText());
                preparegvs.setString(6, txtDiachigv.getText());
                preparegvs.setString(7, comKhoa.getSelectionModel().getSelectedItem());
                preparegvs.setString(8, txtMagv.getText());
                preparegvs.executeUpdate();
                dataBaseGVinput();
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
    void onThemSua(ActionEvent event) {
        if (event.getSource()==btnSuagv){
            editEnableTxt();
            btnAdd.setDisable(true);
            btnOKgv.setOnAction(e->{
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
        if (event.getSource()==btnThemgv){
            clearText();
            editEnableTxt();
            btnOKgv.setDisable(true);
            btnAdd.setOnAction(e ->{
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
