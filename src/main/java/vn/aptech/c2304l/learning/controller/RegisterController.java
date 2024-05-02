
package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;

public class RegisterController {

    @FXML
    private Hyperlink btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    public void redirectLogin() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/login.fxml"));

            Scene scene = new Scene(root);

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }catch (Exception e) {
            System.out.println("redirectLogin(): " + e.getMessage());
        }
    }

}


