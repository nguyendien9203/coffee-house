package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.dal.UserDAO;

import java.io.IOException;
import java.util.Objects;

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
    private Button btnTable;

    @FXML
    private Hyperlink btnRegister;

    private UserDAO userDAO;

    @FXML
    private void initialize() {
        this.userDAO = new UserDAO();
    }

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
    @FXML
    public void btnLoginClicked() {
        String inputUsername = username.getText();
        String inputPassword = password.getText();

        String role = userDAO.checkLogin(inputUsername, inputPassword);

        if (Objects.equals(role, "admin") || Objects.equals(role, "employee")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
                Parent root = loader.load();
                MenuController menuController = loader.getController();
                menuController.setRole(role);
                loader.setController(menuController);
                Stage stage = (Stage) username.getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể mở trang Menu", "Vui lòng thử lại sau.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Lỗi đăng nhập", "Đăng nhập không thành công", "Vui lòng kiểm tra lại tài khoản và mật khẩu.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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
