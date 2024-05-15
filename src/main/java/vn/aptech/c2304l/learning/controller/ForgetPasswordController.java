package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.dal.UserDAO;
import vn.aptech.c2304l.learning.utils.AlertNotification;
import vn.aptech.c2304l.learning.utils.BcryptUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgetPasswordController implements Initializable {

    UserDAO userDAO = new UserDAO();
    AlertNotification alert = new AlertNotification();
    private BcryptUtil bcryptUtil = BcryptUtil.getInstance();
    @FXML
    private AnchorPane forgetPasswordPane;

    @FXML
    private Button btnCreateNewPassword;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnBack;

    @FXML
    private TextField txt_username;

    @FXML
    private AnchorPane confirmNewPasswordPane;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField confirmNewPassword;

    @FXML
    private Button btnForgetPassword;


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

    private String username;
    private String pass;
    private String repass;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnCreateNewPassword.setOnAction(eh -> {
            // Lấy giá trị từ trường txt_username khi người dùng nhấn nút
            username = txt_username.getText().trim();

            // Kiểm tra xem username có giá trị không trước khi sử dụng
            if (!username.isEmpty()) {
                if (userDAO.checkUsernameExists(username)) {
                        alert.showAlert("Thông báo","Đã tìm thấy tài khoản");
                        confirmNewPasswordPane.setVisible(true);
                        forgetPasswordPane.setVisible(false);
                } else {

                    alert.showAlert("Lỗi", "Tài khoản '" + username + "' không tồn tại");
                }
            } else {
                AlertNotification alert = new AlertNotification();
                alert.showAlert("Lỗi", "Vui lòng nhập tên tài khoản");
            }
        });

        btnForgetPassword.setOnAction(eh -> {
            confirmNewPasswordPane.setVisible(false);
            forgetPasswordPane.setVisible(true);
        });



        btnConfirm.setOnAction(eh -> {
             pass = newPassword.getText().trim();
             repass = confirmNewPassword.getText().trim();

            if(pass.equals(repass)){
                alert.showAlert("Thông báo","Đổi mật khẩu thành công");
                String hashPassword = bcryptUtil.hashPassword(pass);
                userDAO.changePassword(username,hashPassword);
            }else{
                alert.showAlert("Lỗi","Mật khẩu không khớp");
            }
        });
    }

}
