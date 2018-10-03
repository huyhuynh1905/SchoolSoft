package sample.LoginApp.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConnection {
    public static Connection connector(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:QUANLITT.sqlite");
            return conn;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
