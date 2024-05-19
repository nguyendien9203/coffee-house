package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.constant.UserStatus;
import vn.aptech.c2304l.learning.dal.UserDAO;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.AlertNotification;
import vn.aptech.c2304l.learning.utils.BcryptUtil;
import vn.aptech.c2304l.learning.utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private BcryptUtil bcryptUtil = BcryptUtil.getInstance();
    AlertNotification alert = new AlertNotification();

    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_password;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnLogin.setOnAction(eh -> {
            String username = txt_username.getText().trim();
            String password = txt_password.getText().trim();
            UserDAO userDAO = new UserDAO();

            if (username.isBlank() || password.isBlank()) {
                AlertNotification alert = new AlertNotification();
                alert.showAlert("Lỗi", "Vui lòng nhập tài khoản và mật khẩu.");
                return;
            }

            if(userDAO.checkPassword(username, password, UserStatus.INACTIVE.toString())) {
                alert.showAlert("Thông báo", "Tài khoản đã bị vô hiệu hoá.");
                return;
            } else if (userDAO.checkPassword(username, password, UserStatus.ACTIVE.toString())){

                alert.showAlert("Thành công", "Đăng nhập thành công.");

                try {

                    User loggedInUser = userDAO.findUserByUsername(username);
                    UserSession.getInstance().setLoggedInUser(loggedInUser);

                    Parent root = FXMLLoader.load(Main.class.getResource("/menu.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setTitle("Menu");
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertNotification alert = new AlertNotification();
                alert.showAlert("Lỗi", "Tài khoản hoặc mật khẩu không đúng.");
            }
        });
    }
}
