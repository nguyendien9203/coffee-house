
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
import vn.aptech.c2304l.learning.context.UserDAO;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.BcryptUtil;
import vn.aptech.c2304l.learning.utils.ShowAlert;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController implements Initializable{

    private BcryptUtil bcryptUtil = BcryptUtil.getInstance();

    @FXML
    private PasswordField txt_password;

    @FXML
    private TextField txt_username;

    @FXML
    private Hyperlink btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private PasswordField txt_confirmPassword;



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
            String username = txt_username.getText();
            String password = txt_password.getText();

            String repassword = txt_confirmPassword.getText();

            if (username.isBlank() || password.isBlank() || repassword.isBlank()) {
                ShowAlert alert = new ShowAlert();
                alert.showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            if (!password.equals(repassword)) {
                ShowAlert alert = new ShowAlert();
                alert.showAlert("Lỗi", "Mật khẩu không khớp.");
                return;
            }

            if (!username.matches("^[a-z0-9]+$")) {
                ShowAlert alert = new ShowAlert();
                alert.showAlert("Lỗi","Tên đăng nhập chỉ chứa số hoặc chữ cái in thường");
                return;
            }

            if (!password.matches("^[a-z0-9]{8,}$")) {
                ShowAlert alert = new ShowAlert();
                alert.showAlert("Lỗi","Mật khẩu phải ít nhất 8 ký tự, có chứa ít nhất 1 số, 1 chữ cái in thường");
                return;
            }

            UserDAO userDAO = new UserDAO();
            if (userDAO.checkUsernameExists(username)) {
                ShowAlert alert = new ShowAlert();
                alert.showAlert("Lỗi", "Tài khoản đã tồn tại.");
                return;
            }

            String hashPassword = bcryptUtil.hashPassword(password);

            User user = new User();
            user.setUsername(username);
            user.setPassword(hashPassword);
            user.setRole(UserRole.EMPLOYEE);
            user.setStatus(UserStatus.ACTIVE);
            boolean checker = userDAO.registerUser(user);
            if (checker) {
                ShowAlert alert = new ShowAlert();
                alert.showAlert("Success", "Đăng kí thành công.");

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
                ShowAlert alert = new ShowAlert();
                alert.showAlert("Error", "Đăng kí thất bại.");
            }
        });
    }

    
}


