package sample.LoginApp.Control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import sample.LoginApp.database.SQLiteConnection;
import sample.LoginApp.model.SinhVienModel;
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

import com.jfoenix.controls.*;


public class TableSinhVienControl implements Initializable {

    @FXML
    private TableColumn<SinhVienModel, String> phonesv;
    @FXML
    private TableView<SinhVienModel> tablesv;
    @FXML
    private TableColumn<SinhVienModel, String> tensv;
    @FXML
    private TableColumn<SinhVienModel, String> diachisv;
    @FXML
    private TableColumn<SinhVienModel, String> ngaysinhsv;
    @FXML
    private TableColumn<SinhVienModel, String> hosv;
    @FXML
    private TableColumn<SinhVienModel, String> sexsv;
    @FXML
    private TableColumn<SinhVienModel, String> idsv;
    @FXML
    private TableColumn<SinhVienModel, String> lopsv;
    @FXML
    private JFXTextField txtFind;
    @FXML
    private JFXButton btnUpdatesv;
    @FXML
    private JFXTextField txtTensv;
    @FXML
    private JFXButton btnThemsv;
    @FXML
    private JFXTextField txtMasv;
    @FXML
    private JFXTextField txtPhonesv;
    @FXML
    private JFXTextField txtDiachisv;
    @FXML
    private JFXButton btnSuasv;
    @FXML
    private JFXTextField txtHosv;
    @FXML
    private ToggleGroup sex;
    @FXML
    private DatePicker bDatesv;
    @FXML
    private RadioButton radNusv;
    @FXML
    private JFXComboBox<String> comLop;
    @FXML
    private JFXButton btnXoasv;
    @FXML
    private JFXButton btnAddsv;
    @FXML
    private RadioButton radNamsv;
    private String lblsex;

    private final String pattern = "dd/MM/yyyy";

    final ObservableList<String> comboxsv = FXCollections.observableArrayList();



    private ObservableList<SinhVienModel> obsv = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formatdayPicker();
        try {
            dataBaseInput();
            fillComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        filTer();
        tableItemClick();
        btnXoasv.setOnAction(e->{
            deleteSinhVien();
        });
    }

    private void dataBaseInput() throws SQLException {
        obsv.clear();
        Connection conn = sample.LoginApp.database.SQLiteConnection.connector();
        ResultSet rs=null;
        PreparedStatement preparedStatement=null;
        String qr ="select * from SINHVIEN";
        try {
            preparedStatement = conn.prepareStatement(qr);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                obsv.add(new SinhVienModel(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7),rs.getString(8)));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            rs.close();
            preparedStatement.close();
        }
        //svr.add(tb);
        idsv.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("IdSv")); //getIdSV
        hosv.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("HoSv")); //getHoSV
        tensv.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("TenSv")); //getTenSV
        ngaysinhsv.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("NgaysinhSv")); //getNgaysinhSv
        sexsv.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("SexSv")); //getSv
        phonesv.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("PhoneSv")); //getPhoneSv
        diachisv.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("DiachiSv")); //getDiachiSv
        lopsv.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("Lopsv"));
        tablesv.setItems(obsv);

    }

    private void filTer(){
        FilteredList<SinhVienModel> filter = new FilteredList<>(obsv,e ->true);
        txtFind.setOnAction(e -> {
            txtFind.textProperty().addListener(((observable, oldValue, newValue) -> {
                filter.setPredicate((Predicate<? super  SinhVienModel>) sv->{
                    if(newValue == null||newValue.isEmpty()){
                        return true;
                    }
                    String lowerCase = newValue.toLowerCase();
                    if (sv.getIdSv().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                    else if(sv.getTenSv().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                    else if (sv.getHoSv().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                    return false;
                });
            }));
            SortedList<SinhVienModel> sort = new SortedList<>(filter);
            sort.comparatorProperty().bind(tablesv.comparatorProperty());
            tablesv.setItems(sort);
        });
    }

    private boolean radioCheck(){
        if (radNamsv.isSelected() || radNusv.isSelected()){
            return false;
        }
        else
            return true;
    }

    private void formatdayPicker(){
        bDatesv.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                bDatesv.setPromptText("Ngày sinh");
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
    private boolean emtyFeild(){
        if (txtMasv.getText().isEmpty()|txtHosv.getText().isEmpty()|
                txtTensv.getText().isEmpty()|txtPhonesv.getText().isEmpty()|txtDiachisv.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị nhập đầy đủ thông tin!!");
            alert.showAndWait();
            return false;
        }
        if ( bDatesv.getEditor().getText().isEmpty()){
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
        if (comLop.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị chọn Lớp!");
            alert.showAndWait();
            return false;
        }

        return true;
    }
    private void tableItemClick(){
        tablesv.setOnMouseClicked(e->{
            if (tablesv.getSelectionModel().getSelectedItem()!=null){
            try{
                SinhVienModel sv = tablesv.getSelectionModel().getSelectedItem();
                String qr = "select * from SINHVIEN where ID=?";
                Connection conn = SQLiteConnection.connector();
                PreparedStatement pre;
                ResultSet rs;
                pre = conn.prepareStatement(qr);
                pre.setString(1,sv.getIdSv());
                rs = pre.executeQuery();
                while (rs.next()){
                    txtMasv.setText(rs.getString(1));
                    txtHosv.setText(rs.getString(2));
                    txtTensv.setText(rs.getString(3));
                    bDatesv.getEditor().setText(rs.getString(4));
                    if (rs.getString(5).equals("Nam")){
                        radNamsv.setSelected(true);
                    }
                    else radNusv.setSelected(true);
                    txtPhonesv.setText(rs.getString(6));
                    txtDiachisv.setText(rs.getString(7));
                }
                rs.close();
                pre.close();
                conn.close();

            }
            catch (Exception m){
                m.printStackTrace();
            }}
        });
    }
    private void fillComboBox(){
        Connection connbox = SQLiteConnection.connector();
        PreparedStatement precombox;
        ResultSet rscbox;
        String khoadb = "select DISTINCT MaLop from SINHVIEN order by MaLop asc";
        try {
            precombox = connbox.prepareStatement(khoadb);
            rscbox = precombox.executeQuery();
            while (rscbox.next()){
                comboxsv.add(rscbox.getString("MaLop"));
            }
            rscbox.close();
            precombox.close();

        }
        catch (Exception e){

        }
        comLop.setItems(comboxsv);
    }
    private void clearText(){
        txtHosv.clear();
        txtDiachisv.clear();
        txtTensv.clear();
        txtMasv.clear();
        txtPhonesv.clear();
        bDatesv.setValue(null);
        radNusv.setSelected(false);
        radNamsv.setSelected(false);
    }
    private void editEnableTxt(){
        txtMasv.setEditable(true);
        txtHosv.setEditable(true);
        txtTensv.setEditable(true);
        txtPhonesv.setEditable(true);
        txtDiachisv.setEditable(true);
        radNusv.setDisable(false);
        radNamsv.setDisable(false);
        bDatesv.setDisable(false);
        comLop.setDisable(false);
        btnUpdatesv.setDisable(false);
        btnAddsv.setDisable(false);
    }
    private void editDisableTxt(){
        txtMasv.setEditable(false);
        txtHosv.setEditable(false);
        txtTensv.setEditable(false);
        txtPhonesv.setEditable(false);
        txtDiachisv.setEditable(false);
        radNusv.setDisable(true);
        radNamsv.setDisable(true);
        bDatesv.setDisable(true);
        comLop.setDisable(true);
        btnUpdatesv.setDisable(true);
        btnAddsv.setDisable(true);
    }
    private boolean maSvisCheck() throws SQLException {
        PreparedStatement preCk=null;
        ResultSet rsCk = null;
        Connection connection = SQLiteConnection.connector();
        String gvstr1 = "select ? from SINHVIEN";
        try {
            preCk = connection.prepareStatement(gvstr1);
            preCk.setString(1,txtMasv.getText());
            rsCk = preCk.executeQuery();
            if (rsCk.next()){
                return  true;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không tìm thấy Mã Sinh Viên!");
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
    private void themSinhVien() throws SQLException{
        if (emtyFeild()) {
            if (radNamsv.isSelected()) {
                lblsex = radNamsv.getText();
            } else if (radNusv.isSelected()) {
                lblsex = radNusv.getText();
            }
            Connection conngv = SQLiteConnection.connector();
            PreparedStatement preparegv1 = null;
            String gvstr1 = "insert into SINHVIEN (ID,HoSV,TenSV,NgaySinhSV,GioiTinhSV,PhoneSV,DiaChiSV,MaLop) values (?,?,?,?,?,?,?,?)";
            try {
                preparegv1 = conngv.prepareStatement(gvstr1);
                preparegv1.setString(1, txtMasv.getText());
                preparegv1.setString(2, txtHosv.getText());
                preparegv1.setString(3, txtTensv.getText());
                preparegv1.setString(4, bDatesv.getEditor().getText());
                preparegv1.setString(5, lblsex);
                preparegv1.setString(6, txtPhonesv.getText());
                preparegv1.setString(7, txtDiachisv.getText());
                preparegv1.setString(8, comLop.getSelectionModel().getSelectedItem());
                preparegv1.executeUpdate();
                dataBaseInput();
                clearText();

                preparegv1.close();
                conngv.close();
            } catch (Exception e) {

            }
            finally {
            }
        }
    }
    private void deleteSinhVien(){
        if (!txtMasv.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Bạn có muốn xoá " + txtHosv.getText() + " " + txtTensv.getText() + "\nKhỏi danh sách?");
            Optional<ButtonType> action = alert.showAndWait();
            Connection conn = SQLiteConnection.connector();
            PreparedStatement pred =null;
            if (action.get() == ButtonType.OK) {
                try {
                    String qr = "delete from SINHVIEN where ID=?";
                    pred = conn.prepareStatement(qr);
                    pred.setString(1, txtMasv.getText());
                    pred.executeUpdate();
                    pred.close();
                    dataBaseInput();
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

    private void suaSinhVien() throws SQLException {
        if (emtyFeild()) {
            if (radNamsv.isSelected()) {
                lblsex = radNamsv.getText();
            } else if (radNusv.isSelected()) {
                lblsex = radNusv.getText();
            }
            Connection conngv = SQLiteConnection.connector();
            PreparedStatement preparegvs = null;
            String str2 = "update SINHVIEN set HoSV = ?,TenSV = ?,NgaySinhSV = ?,GioiTinhSV = ?,PhoneSV = ?,DiaChiSV = ?,MaLop = ? where ID = ?";
            try {
                preparegvs = conngv.prepareStatement(str2);
                preparegvs.setString(1, txtHosv.getText());
                preparegvs.setString(2, txtTensv.getText());
                preparegvs.setString(3, bDatesv.getEditor().getText());
                preparegvs.setString(4, lblsex);
                preparegvs.setString(5, txtPhonesv.getText());
                preparegvs.setString(6, txtDiachisv.getText());
                preparegvs.setString(7, comLop.getSelectionModel().getSelectedItem());
                preparegvs.setString(8, txtMasv.getText());
                preparegvs.executeUpdate();
                dataBaseInput();
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
    void onThemSuaSv(ActionEvent event) {
        if (event.getSource()==btnSuasv){
            editEnableTxt();
            btnAddsv.setDisable(true);
            btnUpdatesv.setOnAction(e->{
                try {
                    if (maSvisCheck()){
                        suaSinhVien();
                        editDisableTxt();}
                }
                catch (SQLException s){
                    s.printStackTrace();
                }
            });
        }
        if (event.getSource()==btnThemsv){
            clearText();
            editEnableTxt();
            btnUpdatesv.setDisable(true);
            btnAddsv.setOnAction(e ->{
                try {
                    themSinhVien();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                editDisableTxt();
            });
        }
    }

}
