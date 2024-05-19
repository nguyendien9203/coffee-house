package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import vn.aptech.c2304l.learning.model.Order;
import vn.aptech.c2304l.learning.model.OrderItem;
import vn.aptech.c2304l.learning.model.Product;
import vn.aptech.c2304l.learning.utils.FormatPriceUtil;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class CardBillController implements Initializable {
    private FormatPriceUtil formatPriceUtil = FormatPriceUtil.getInstance();

    @FXML
    private AnchorPane billProductImage;

    @FXML
    private Label billProductName;

    @FXML
    private Label billProductPrice;

    @FXML
    private Label billQuantity;

    @FXML
    private HBox hBoxBillCard;

    @FXML
    private Label billProductId;

    @FXML
    private Label billProductDescription;

    private OrderItem orderItem;

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;

        billProductId.setText(String.valueOf(orderItem.getProduct().getId()));
        billProductName.setText(orderItem.getProduct().getName());
        billProductDescription.setText(orderItem.getProduct().getDescription());
        billProductPrice.setText(formatPriceUtil.formatPrice(orderItem.getProduct().getPrice()));
        billQuantity.setText(String.valueOf(orderItem.getQuantity()));
        displayImagePreview(orderItem.getProduct().getImage());
    }

    private void displayImagePreview(String imagePath) {
        String uploadsFolderPath = "uploads/";
        String fullPath = uploadsFolderPath + imagePath;

        File file = new File(fullPath);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(billProductImage.getPrefWidth());
            imageView.setFitHeight(billProductImage.getPrefHeight());
            billProductImage.getChildren().clear();
            billProductImage.getChildren().add(imageView);
            billProductImage.setPrefHeight(68);
            billProductImage.setPrefWidth(68);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
