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
import vn.aptech.c2304l.learning.utils.FormatPriceUtil;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class OrderCardController implements Initializable {
    private FormatPriceUtil formatPriceUtil = FormatPriceUtil.getInstance();
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
    private VBox vBoxListOrder;

    private Map<Product, Integer> products;
    BigDecimal totalOrder = BigDecimal.ZERO;



    private MenuDetailController menuDetailController;

    public void setMenuDetailController(MenuDetailController menuDetailController) {
        this.menuDetailController = menuDetailController;
    }

    public void setOrderItem(Product product, int quantity, VBox vBoxListOrder, Map<Product, Integer> products) {
        this.product = product;
        this.vBoxListOrder = vBoxListOrder;
        this.products= products;

        orderProductId.setText(String.valueOf(product.getId()));
        orderProductName.setText(product.getName());
        orderProductDescription.setText(product.getDescription());
        orderProductPrice.setText(formatPriceUtil.formatPrice(product.getPrice()));
        txtQuantity.setText(String.valueOf(quantity));
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
        int quantity = products.getOrDefault(product, 0);
        quantity++;
        txtQuantity.setText(String.valueOf(quantity));
        products.put(product, quantity);
        calculateTotalOrder();
        menuDetailController.updateTotalOrder(totalOrder);
    }

    @FXML
    private void decrementQuantity() {
        int quantity = products.getOrDefault(product, 0);
        if (quantity > 1) {
            quantity--;
            txtQuantity.setText(String.valueOf(quantity));
            products.put(product, quantity);
        } else {
            removeOrderItem();
        }
        calculateTotalOrder();
        menuDetailController.updateTotalOrder(totalOrder);
    }

    @FXML
    private void removeOrderItem() {
        products.remove(product);
        vBoxListOrder.getChildren().removeIf(node -> {
            OrderCardController controller = (OrderCardController) node.getUserData();
            return controller.product.equals(this.product);
        });
        calculateTotalOrder();
        menuDetailController.updateTotalOrder(totalOrder);
    }



    private void calculateTotalOrder() {
        totalOrder = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            BigDecimal productPrice = product.getPrice();
            BigDecimal subtotal = productPrice.multiply(BigDecimal.valueOf(quantity));
            totalOrder = totalOrder.add(subtotal);
        }
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
