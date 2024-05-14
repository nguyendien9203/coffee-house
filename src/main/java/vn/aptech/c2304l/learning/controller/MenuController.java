package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.model.Order;

import java.io.IOException;
import java.util.Objects;

public class MenuController{

    @FXML
    private VBox btnAuthentication;

    @FXML
    private VBox btnCategory;

    @FXML
    private VBox btnLogout;

    @FXML
    private VBox btnMenu;

    @FXML
    private VBox btnOrder;

    @FXML
    private VBox btnProduct;

    @FXML
    private VBox btnStatistic;

    @FXML
    private VBox btnTable;

    private String role;

    public void setRole(String role) {
        this.role = role;
        updateUI();
    }

    private void updateUI() {
        if(Objects.equals(role, "admin")) {
            btnProduct.setVisible(true);
            btnOrder.setVisible(true);
            btnStatistic.setVisible(true);
            btnAuthentication.setVisible(true);
            btnLogout.setVisible(true);
            btnMenu.setVisible(true);
            btnTable.setVisible(true);
            btnCategory.setVisible(true);
        } else {
            btnProduct.setVisible(false);
            btnOrder.setVisible(true);
            btnStatistic.setVisible(false);
            btnAuthentication.setVisible(false);
            btnLogout.setVisible(true);
            btnMenu.setVisible(false);
            btnTable.setVisible(false);
            btnCategory.setVisible(false);
        }
    }


    @FXML
    public void initialize() {

        btnMenu.requestFocus();
        btnMenu.setFocusTraversable(true);
    }

    @FXML
    public void redirectProduct() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/product.fxml"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) btnProduct.getScene().getWindow();
            stage.setTitle("Sản phẩm");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        }  catch (Exception e) {
            System.out.println("redirectProduct(): " + e.getMessage());
        }
    }

    @FXML
    public void redirectTable() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/table.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnTable.getScene().getWindow();
            stage.setTitle("Bàn");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        }  catch (Exception e) {
            System.out.println("redirectTable(): " + e.getMessage());
        }
    }

    @FXML
    public void redirectCategory() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/category.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnCategory.getScene().getWindow();
            stage.setTitle("Danh mục");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        }  catch (Exception e) {
            System.out.println("redirectCategory(): " + e.getMessage());
        }
    }

    @FXML
    public void redirectOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/order.fxml"));
            Parent root = loader.load();
            OrderController orderController = loader.getController();
            orderController.setRole(role);
            loader.setController(orderController);
            Stage stage = (Stage) btnOrder.getScene().getWindow();
            stage.setTitle("Hóa đơn");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
        }  catch (Exception e) {
            System.out.println("redirectOrder(): " + e.getMessage());
        }
    }

    @FXML
    public void redirectStatistic() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/statistic.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnStatistic.getScene().getWindow();
            stage.setTitle("Thông kê");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        }  catch (Exception e) {
            System.out.println("redirectStatistic(): " + e.getMessage());
        }
    }

    @FXML
    public void redirectAuthentication() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/authentication.fxml"));

        Scene scene = new Scene(root);


        Stage stage = (Stage) btnAuthentication.getScene().getWindow();
        stage.setTitle("Phân quyền");
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void redirectLogin() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/login.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setTitle("Đăng nhập");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("redirectLogin(): " + e.getMessage());
        }
    }


}
