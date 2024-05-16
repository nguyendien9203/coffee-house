package vn.aptech.c2304l.learning.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgetPasswordController implements Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnCreateNewPassword;

    @FXML
    private TextField username;

    @FXML
    public void redirectLogin() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/login.fxml"));

            Scene scene = new Scene(root);

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }catch (Exception e) {
            System.out.println("redirectLogin(): " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
