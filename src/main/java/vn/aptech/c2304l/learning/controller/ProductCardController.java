package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import vn.aptech.c2304l.learning.model.Product;
import vn.aptech.c2304l.learning.utils.FormatPriceUtil;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductCardController implements Initializable {
    private FormatPriceUtil formatPriceUtil = FormatPriceUtil.getInstance();


    @FXML
    private Label productDescription;

    @FXML
    private Label productId;

    @FXML
    private AnchorPane productImage;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    private Product product;
    private MenuDetailController menuDetailController;

    public void setProductCart(Product product) {
        this.product = product;
        productId.setText(String.valueOf(product.getId()));
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText(formatPriceUtil.formatPrice(product.getPrice()));
        displayImagePreview(product.getImage());
    }

    private void displayImagePreview(String imagePath) {
        String uploadsFolderPath = "uploads/";
        String fullPath = uploadsFolderPath + imagePath;

        File file = new File(fullPath);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(productImage.getPrefWidth());
            imageView.setFitHeight(productImage.getPrefHeight());
            productImage.getChildren().clear();
            productImage.getChildren().add(imageView);
        }
    }

    public void setMenuDetailController(MenuDetailController menuDetailController) {
        this.menuDetailController = menuDetailController;
    }

    @FXML
    private void handleAddToBill() {
        if (menuDetailController != null) {
            menuDetailController.addProductToOrder(product);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
