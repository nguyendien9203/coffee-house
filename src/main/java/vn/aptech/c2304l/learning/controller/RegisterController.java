
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
import vn.aptech.c2304l.learning.constant.UserRole;
import vn.aptech.c2304l.learning.constant.UserStatus;
import vn.aptech.c2304l.learning.dal.UserDAO;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.BcryptUtil;
import vn.aptech.c2304l.learning.utils.AlertNotification;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private BcryptUtil bcryptUtil = BcryptUtil.getInstance();
    private UserDAO udao = new UserDAO();
    AlertNotification alert = new AlertNotification();

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtFullname;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;


    @FXML
    private Hyperlink btnLogin;

    @FXML
    private Button btnRegister;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnRegister.setOnAction(eh -> {
            String fullname = txtFullname.getText().trim();
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String confirmPassword = txtConfirmPassword.getText().trim();

            if (fullname.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                alert.showAlert("Thông báo", "Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            if (!fullname.matches("^[a-zA-Z\\s]+$")) {
                alert.showAlert("Thông báo","Tên người dùng chỉ chứa chữ cái in thường, in hoa và khoảng trắng");
                return;
            }

            if (!username.matches("^[a-z0-9]+$")) {
                alert.showAlert("Thông báo","Tên đăng nhập chỉ chứa số hoặc chữ cái in thường");
                return;
            }

            if (!password.matches("^[a-z0-9]{8,}$")) {
                alert.showAlert("Thông báo","Mật khẩu phải ít nhất 8 ký tự, có chứa ít nhất 1 số, 1 chữ cái in thường");
                return;
            }

            if (!password.equals(confirmPassword.trim())) {
                alert.showAlert("Thông báo", "Mật khẩu không khớp.");
                return;
            }

            if (udao.findByUsername(username) != null) {
                alert.showAlert("Thông báo", "Tài khoản đã tồn tại.");
                return;
            }

            String hashPassword = bcryptUtil.hashPassword(password);

            User user = new User();
            user.setRole(UserRole.EMPLOYEE);
            user.setFullname(fullname);
            user.setUsername(username);
            user.setPassword(hashPassword);
            user.setStatus(UserStatus.ACTIVE);
            if (udao.registerUser(user)) {
                AlertNotification alert = new AlertNotification();
                alert.showAlert("Thông báo", "Đăng kí thành công.");

                Parent root = null;
                try {
                    root = FXMLLoader.load(Main.class.getResource("/login.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Scene scene = new Scene(root);

                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setTitle("Login");
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            } else {
                AlertNotification alert = new AlertNotification();
                alert.showAlert("Thông báo", "Đăng kí thất bại.");
            }
        });
    }



}


