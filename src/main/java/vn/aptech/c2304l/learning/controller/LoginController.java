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

public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Hyperlink btnForgetPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink btnRegister;

    @FXML
    public void redirectRegister() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/register.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.setTitle("Register");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        }  catch (Exception e) {
            System.out.println("redirectRegister(): " + e.getMessage());
        }
    }

    public void redirectForgetPassword() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/forgetPassword.fxml"));

            Scene scene = new Scene(root);

            Stage stage = (Stage) btnForgetPassword.getScene().getWindow();
            stage.setTitle("Forget Password");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e) {
            System.out.println("redirectForgetPassword(): " + e.getMessage());
        }
    }

}
