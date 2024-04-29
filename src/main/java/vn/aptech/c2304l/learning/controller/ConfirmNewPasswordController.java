package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;

public class ConfirmNewPasswordController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnConfirm;

    @FXML
    private PasswordField confirmNewPassword;

    @FXML
    private PasswordField newPassword;

    public void redirectForgetPassword() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/forgetPassword.fxml"));

            Scene scene = new Scene(root);

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setTitle("Forget Password");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e) {
            System.out.println("redirectForgetPassword(): " + e.getMessage());
        }
    }

}
