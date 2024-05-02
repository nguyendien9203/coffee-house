package vn.aptech.c2304l.learning.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {
    protected Connection connection;

    public DBContext() {

        try {
            String user = "root";
            String pass = "abcd12345";
            String url = "jdbc:mysql://localhost:3306/coffeemgt";

            connection = DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}