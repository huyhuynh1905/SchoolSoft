package sample.LoginApp.model;


import sample.LoginApp.database.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    Connection connection;
    public LoginModel(){
        connection = SQLiteConnection.connector();
        if (connection==null) System.out.println("Lỗi");
    }
    public boolean isDbConnected(){
        try {
            return !connection.isClosed();
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean isLogin(String user, String pass) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =null;
        String q = "select * from ADMIN where User = ? and Pass = ?";
        try {
            preparedStatement = connection.prepareStatement(q);
            preparedStatement.setString(1,user);
            preparedStatement.setString(2,pass);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return  true;
            }
            else return false;
        }
        catch (Exception e){
            return false;
        }
        finally {
            preparedStatement.close();
            resultSet.close();
        }
    }
}
