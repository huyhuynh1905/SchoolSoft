package sample.LoginApp.Control;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.*;
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

public class LopControl implements Initializable {
    @FXML
    private TableView<SinhVienModel> tablesv;
    @FXML
    private JFXButton btnUpdateLp;
    @FXML
    private JFXTextField txtDiachilp;
    @FXML
    private TableColumn<SinhVienModel, String> cldiachilp;
    @FXML
    private JFXTextField txtHolp;
    @FXML
    private TableColumn<SinhVienModel, String> clngaysinhlp;
    @FXML
    private DatePicker bDatelp;
    @FXML
    private JFXButton btnThemLp;
    @FXML
    private TableColumn<SinhVienModel, String> clmalp;
    @FXML
    private JFXButton btnRefLp;
    @FXML
    private RadioButton radNamlp;
    @FXML
    private JFXTextField txtFindlp;
    @FXML
    private TableColumn<SinhVienModel, String> clphonelp;
    @FXML
    private JFXButton btnSuaLp;
    @FXML
    private JFXTextField txtTenlp;
    @FXML
    private TableColumn<SinhVienModel, String> clholp;
    @FXML
    private JFXButton btnAddLop;
    @FXML
    private RadioButton radNulp;
    @FXML
    private JFXButton btnXoaLp;
    @FXML
    private JFXTextField txtMalp;
    @FXML
    private JFXComboBox<String> comLopl;
    @FXML
    private TableColumn<SinhVienModel, String> clsexlp;
    @FXML
    private JFXComboBox<String> comHienThilp;
    @FXML
    private TableColumn<SinhVienModel, String> cllopl;
    @FXML
    private TableColumn<SinhVienModel, String> cltenlp;
    @FXML
    private JFXTextField txtPhonelp;
    private String lblsex;

    private final String pattern = "dd/MM/yyyy";

    final ObservableList<String> comboxsv = FXCollections.observableArrayList();
    final ObservableList<String> comboxhienthisv = FXCollections.observableArrayList();

    private ObservableList<SinhVienModel> obsv = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formatdayPicker();
        try{
            fillComboxLop();
            fillComboBox();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        filTer();
        tableItemClick();
        btnRefLp.setOnAction(e->{
            try {
                dataInputLop();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        btnXoaLp.setOnAction(e->{
            deleteSinhVien();
        });
    }

    private void fillComboxLop(){
        Connection connbox = SQLiteConnection.connector();
        PreparedStatement precombox;
        ResultSet rscbox;
        String khoadb = "select DISTINCT MaLop from SINHVIEN order by MaLop asc";
        try {
            precombox = connbox.prepareStatement(khoadb);
            rscbox = precombox.executeQuery();
            while (rscbox.next()){
                comboxhienthisv.add(rscbox.getString("MaLop"));
            }
            rscbox.close();
            precombox.close();

        }
        catch (Exception e){

        }
        comHienThilp.setItems(comboxhienthisv);
    }
    private void dataInputLop() throws SQLException {
        obsv.clear();
        Connection conn = sample.LoginApp.database.SQLiteConnection.connector();
        ResultSet rs=null;
        PreparedStatement preparedStatement=null;
        String qr ="select * from SINHVIEN where MaLop=?";
        try {
            preparedStatement = conn.prepareStatement(qr);
            preparedStatement.setString(1,comHienThilp.getSelectionModel().getSelectedItem());
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                obsv.add(new SinhVienModel(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7),rs.getString(8)));
            }
        }
        catch (Exception n){
                n.printStackTrace();
        }
        finally {
            try {
                rs.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        //svr.add(tb);
        clmalp.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("IdSv")); //getIdSV
        clholp.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("HoSv")); //getHoSV
        cltenlp.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("TenSv")); //getTenSV
        clngaysinhlp.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("NgaysinhSv")); //getNgaysinhSv
        clsexlp.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("SexSv")); //getSv
        clphonelp.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("PhoneSv")); //getPhoneSv
        cldiachilp.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("DiachiSv")); //getDiachiSv
        cllopl.setCellValueFactory(new PropertyValueFactory<SinhVienModel,String>("Lopsv"));
        tablesv.setItems(obsv);
    }

    public void filTer(){
        FilteredList<SinhVienModel> filter = new FilteredList<>(obsv, e ->true);
        txtFindlp.setOnAction(e -> {
            txtFindlp.textProperty().addListener(((observable, oldValue, newValue) -> {
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

    public boolean radioCheck(){
        if (radNamlp.isSelected() || radNulp.isSelected()){
            return false;
        }
        else
            return true;
    }

    public void formatdayPicker(){
        bDatelp.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                bDatelp.setPromptText("Ngày sinh");
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
    public boolean emtyFeild(){
        if (txtMalp.getText().isEmpty()|txtHolp.getText().isEmpty()|
                txtTenlp.getText().isEmpty()|txtPhonelp.getText().isEmpty()|txtDiachilp.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị nhập đầy đủ thông tin!!");
            alert.showAndWait();
            return false;
        }
        if ( bDatelp.getEditor().getText().isEmpty()){
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
        if (comLopl.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring!");
            alert.setHeaderText(null);
            alert.setContentText("Đề nghị chọn Lớp!");
            alert.showAndWait();
            return false;
        }

        return true;
    }
    public void tableItemClick(){
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
                        txtMalp.setText(rs.getString(1));
                        txtHolp.setText(rs.getString(2));
                        txtTenlp.setText(rs.getString(3));
                        bDatelp.getEditor().setText(rs.getString(4));
                        if (rs.getString(5).equals("Nam")){
                            radNamlp.setSelected(true);
                        }
                        else radNulp.setSelected(true);
                        txtPhonelp.setText(rs.getString(6));
                        txtDiachilp.setText(rs.getString(7));
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
    public void fillComboBox(){
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
        comLopl.setItems(comboxsv);
    }
    public void clearText(){
        txtHolp.clear();
        txtDiachilp.clear();
        txtTenlp.clear();
        txtMalp.clear();
        txtPhonelp.clear();
        bDatelp.setValue(null);
        radNulp.setSelected(false);
        radNamlp.setSelected(false);
    }
    public void editEnableTxt(){
        txtMalp.setEditable(true);
        txtHolp.setEditable(true);
        txtTenlp.setEditable(true);
        txtPhonelp.setEditable(true);
        txtDiachilp.setEditable(true);
        radNulp.setDisable(false);
        radNamlp.setDisable(false);
        bDatelp.setDisable(false);
        comLopl.setDisable(false);
        btnUpdateLp.setDisable(false);
        btnAddLop.setDisable(false);
    }
    public void editDisableTxt(){
        txtMalp.setEditable(false);
        txtHolp.setEditable(false);
        txtTenlp.setEditable(false);
        txtPhonelp.setEditable(false);
        txtDiachilp.setEditable(false);
        radNulp.setDisable(true);
        radNamlp.setDisable(true);
        bDatelp.setDisable(true);
        comLopl.setDisable(true);
        btnUpdateLp.setDisable(true);
        btnAddLop.setDisable(true);
    }
    public boolean maSvisCheck() throws SQLException {
        PreparedStatement preCk=null;
        ResultSet rsCk = null;
        Connection connection = SQLiteConnection.connector();
        String gvstr1 = "select ? from SINHVIEN";
        try {
            preCk = connection.prepareStatement(gvstr1);
            preCk.setString(1,txtMalp.getText());
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
    public void themSinhVien() throws SQLException{
        if (emtyFeild()) {
            if (radNamlp.isSelected()) {
                lblsex = radNamlp.getText();
            } else if (radNulp.isSelected()) {
                lblsex = radNulp.getText();
            }
            Connection conngv = SQLiteConnection.connector();
            PreparedStatement preparegv1 = null;
            String gvstr1 = "insert into SINHVIEN (ID,HoSV,TenSV,NgaySinhSV,GioiTinhSV,PhoneSV,DiaChiSV,MaLop) values (?,?,?,?,?,?,?,?)";
            try {
                preparegv1 = conngv.prepareStatement(gvstr1);
                preparegv1.setString(1, txtMalp.getText());
                preparegv1.setString(2, txtHolp.getText());
                preparegv1.setString(3, txtTenlp.getText());
                preparegv1.setString(4, bDatelp.getEditor().getText());
                preparegv1.setString(5, lblsex);
                preparegv1.setString(6, txtPhonelp.getText());
                preparegv1.setString(7, txtDiachilp.getText());
                preparegv1.setString(8, comLopl.getSelectionModel().getSelectedItem());
                preparegv1.executeUpdate();
                dataInputLop();
                clearText();

                preparegv1.close();
                conngv.close();
            } catch (Exception e) {

            }
            finally {
            }
        }
    }
    public void deleteSinhVien(){
        if (!txtMalp.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Bạn có muốn xoá " + txtHolp.getText() + " " + txtTenlp.getText() + "\nKhỏi danh sách?");
            Optional<ButtonType> action = alert.showAndWait();
            Connection conn = SQLiteConnection.connector();
            PreparedStatement pred =null;
            if (action.get() == ButtonType.OK) {
                try {
                    String qr = "delete from SINHVIEN where ID=?";
                    pred = conn.prepareStatement(qr);
                    pred.setString(1, txtMalp.getText());
                    pred.executeUpdate();
                    pred.close();
                    dataInputLop();
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

    public void suaSinhVien() throws SQLException {
        if (emtyFeild()) {
            if (radNamlp.isSelected()) {
                lblsex = radNamlp.getText();
            } else if (radNulp.isSelected()) {
                lblsex = radNulp.getText();
            }
            Connection conngv = SQLiteConnection.connector();
            PreparedStatement preparegvs = null;
            String str2 = "update SINHVIEN set HoSV = ?,TenSV = ?,NgaySinhSV = ?,GioiTinhSV = ?,PhoneSV = ?,DiaChiSV = ?,MaLop = ? where ID = ?";
            try {
                preparegvs = conngv.prepareStatement(str2);
                preparegvs.setString(1, txtHolp.getText());
                preparegvs.setString(2, txtTenlp.getText());
                preparegvs.setString(3, bDatelp.getEditor().getText());
                preparegvs.setString(4, lblsex);
                preparegvs.setString(5, txtPhonelp.getText());
                preparegvs.setString(6, txtDiachilp.getText());
                preparegvs.setString(7, comLopl.getSelectionModel().getSelectedItem());
                preparegvs.setString(8, txtMalp.getText());
                preparegvs.executeUpdate();
                dataInputLop();
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
    void onThemSuaSvlp(ActionEvent event) {
        if (event.getSource()==btnSuaLp){
            editEnableTxt();
            btnAddLop.setDisable(true);
            btnUpdateLp.setOnAction(e->{
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
        if (event.getSource()==btnThemLp){
            clearText();
            editEnableTxt();
            btnUpdateLp.setDisable(true);
            btnAddLop.setOnAction(e ->{
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
