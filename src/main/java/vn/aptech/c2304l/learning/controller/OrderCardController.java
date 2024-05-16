package vn.aptech.c2304l.learning.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import vn.aptech.c2304l.learning.model.Order;
import vn.aptech.c2304l.learning.model.Product;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderCardController implements Initializable {
    @FXML
    private Button btnDecrement;

    @FXML
    private Label btnDeleteItem;

    @FXML
    private Button btnIncrement;

    @FXML
    private AnchorPane orderProductImage;

    @FXML
    private Label orderProductName;

    @FXML
    private Label orderProductId;

    @FXML
    private Label orderProductDescription;

    @FXML
    private Label orderProductPrice;

    @FXML
    private TextField txtQuantity;

    private Product product;
    private Order order;
    private VBox vBoxListOrder;


    public void setOrderItem(Product product, Order order, VBox vBoxListOrder) {
        this.product = product;
        this.order = order;
        this.vBoxListOrder = vBoxListOrder;

        orderProductId.setText(String.valueOf(product.getId()));
        orderProductName.setText(product.getName());
        orderProductDescription.setText(product.getDescription());
        orderProductPrice.setText(String.valueOf(product.getPrice()));
        txtQuantity.setText(String.valueOf(order.getProducts().getOrDefault(product, 0)));
        displayImagePreview(product.getImage());
    }

    private void displayImagePreview(String imagePath) {
        String uploadsFolderPath = "uploads/";
        String fullPath = uploadsFolderPath + imagePath;

        File file = new File(fullPath);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(orderProductImage.getPrefWidth());
            imageView.setFitHeight(orderProductImage.getPrefHeight());
            orderProductImage.getChildren().clear();
            orderProductImage.getChildren().add(imageView);
            orderProductImage.setPrefHeight(68);
            orderProductImage.setPrefWidth(68);
        }
    }

    @FXML
    private void incrementQuantity() {
        int quantity = order.getProducts().getOrDefault(product, 0);
        quantity++;
        txtQuantity.setText(String.valueOf(quantity));
        order.getProducts().put(product, quantity);
    }

    @FXML
    private void decrementQuantity() {
        int quantity = order.getProducts().getOrDefault(product, 0);
        if (quantity > 1) {
            quantity--;
            txtQuantity.setText(String.valueOf(quantity));
            order.getProducts().put(product, quantity);
        } else {
            removeOrderItem();
        }
    }

    @FXML
    private void removeOrderItem() {
        order.getProducts().remove(product);
        vBoxListOrder.getChildren().removeIf(node -> {
            OrderCardController controller = (OrderCardController) node.getUserData();
            return controller.product.equals(this.product);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
