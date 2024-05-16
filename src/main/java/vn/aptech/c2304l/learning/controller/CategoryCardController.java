//package vn.aptech.c2304l.learning.controller;
//
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import vn.aptech.c2304l.learning.model.Category;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class CategoryCardController implements Initializable {
//    @FXML
//    private Label cateId;
//
//    @FXML
//    private Button cateName;
//
//    private Category category;
//
//    private MenuDetailController menuDetailController;
//
//    public void setCategoryCart(Category category) {
//        this.category = category;
//        cateId.setText(String.valueOf(category.getId()));
//        cateName.setText(category.getName());
//    }
//
//    public void setMenuDetailController(MenuDetailController menuDetailController) {
//        this.menuDetailController = menuDetailController;
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        cateName.setOnAction(event -> {
//            if (category != null && menuDetailController != null) {
//                System.out.println("Giá trị của button: " + category.getName() + " " + category.getId());
//                menuDetailController.findProductsByCategory(category.getId());
//            }
//        });
//    }
//}
