package vn.aptech.c2304l.learning.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.constant.OrderStatus;
import vn.aptech.c2304l.learning.dal.CategoryDAO;
import vn.aptech.c2304l.learning.dal.OrderDAO;
import vn.aptech.c2304l.learning.dal.ProductDAO;
import vn.aptech.c2304l.learning.model.*;
import vn.aptech.c2304l.learning.utils.AlertNotification;
import vn.aptech.c2304l.learning.utils.FormatPriceUtil;
import vn.aptech.c2304l.learning.utils.UniqueCodeBillGenerator;
import vn.aptech.c2304l.learning.utils.UserSession;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class MenuDetailController implements Initializable {
    private ProductDAO pdao = new ProductDAO();

    private CategoryDAO cdao = new CategoryDAO();

    private OrderDAO odao = new OrderDAO();

    private UniqueCodeBillGenerator uniqueCodeBillGenerator = UniqueCodeBillGenerator.getInstance();
    private FormatPriceUtil formatPriceUtil = FormatPriceUtil.getInstance();

    private AlertNotification alertNotification = new AlertNotification();

    private User loggedInUser = UserSession.getInstance().getLoggedInUser();
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

    @FXML
    private Button btnCheckOut;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnPrint;

    @FXML
    private HBox hBoxCate;

    @FXML
    private HBox hBoxItem;

    @FXML
    private TextArea orderNote;

    @FXML
    private RadioButton radioButtonBankTransfer;

    @FXML
    private RadioButton radioButtonCash;

    @FXML
    private RadioButton radioButtonEWallet;

    @FXML
    private ToggleGroup paymentMethodToggleGroup;


    @FXML
    private GridPane productGridPane;

    private Table table;

    @FXML
    private Button txtTableNumber;

    @FXML
    private Label labelFullName;

    @FXML
    private VBox vBoxListOrder;

    @FXML
    private Label labelTotalOrder;

    private NumOfSeatsController numOfSeatsController;
    private Button tableOrderedButton;

    public void setNumOfSeatsController(NumOfSeatsController numOfSeatsController, Button tableOrderedButton) {
        this.numOfSeatsController = numOfSeatsController;
        this.tableOrderedButton = tableOrderedButton;
    }

    private String role;

    public void setRole(String role) {
        this.role = role;
        updateUI();
    }

    private void updateUI() {
        if(Objects.equals(role, "ADMIN")) {
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
            btnMenu.setVisible(true);
            btnTable.setVisible(false);
            btnCategory.setVisible(false);
        }
    }

    public void setTable(Table table) {
        this.table = table;
        txtTableNumber.setText(String.valueOf(table.getTableNumber()));
    }

    public void findAllCategory() {
        ObservableList<Category> listData = cdao.findAll();

        for (Category category : listData) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/cardCategory.fxml"));
                hBoxItem = loader.load();

                hBoxItem.setPrefHeight(45);
                hBoxItem.setPrefWidth(130);
                hBoxItem.setAlignment(Pos.CENTER);
                hBoxItem.setStyle("-fx-background-color: transparent; -fx-border-color: #d1d1d1; -fx-border-radius: 50px");
                HBox.setMargin(hBoxItem, new Insets(0, 0, 0, 15));


                CategoryCardController controller = loader.getController();
                controller.setCategoryCart(category);
                controller.setMenuDetailController(this);

                hBoxCate.getChildren().add(hBoxItem);

            } catch (IOException e) {
                e.printStackTrace();
            }

            double totalWidth = 0;
            for (Node node : hBoxCate.getChildren()) {
                totalWidth += node.prefWidth(1);
            }
            hBoxCate.setPrefWidth(totalWidth);
        }
    }

    public void findAllProductActive() {
        ObservableList<Product> listData = pdao.findAllProductActive();
        displayProducts(listData);
    }

    public void findProductsByCategory(int categoryId) {
        ObservableList<Product> listData = pdao.findAllByCategory(categoryId);
        displayProducts(listData);
    }

    @FXML
    public void listProductClick() {
        try {
            findAllProductActive();
        } catch (Exception e) {
            System.out.println("listProductClick(): " + e.getMessage());
        }
    }


    private void displayProducts(ObservableList<Product> products) {
        productGridPane.getChildren().clear();
        int row = 1;
        int column = 0;
        for (Product product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/cardProduct.fxml"));
                HBox hBox = loader.load();
                hBox.setPrefHeight(180);
                hBox.setPrefWidth(352);
                hBox.setAlignment(Pos.CENTER);
                hBox.setStyle("-fx-background-color: #FFF");

                ProductCardController controller = loader.getController();
                controller.setProductCart(product);
                controller.setMenuDetailController(this);

                productGridPane.add(hBox, column++, row);
                if (column == 2) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Map<Product, Integer> products = new HashMap<>();

    public void addProductToOrder(Product product) {
        System.out.println("Product dc add: " + product);
        if (products.containsKey(product)) {
            int currentQuantity = products.get(product);
            products.put(product, currentQuantity + 1);
        } else {
            products.put(product, 1);
        }

        updateOrderListView();
    }

    public void loadOrderItems(Order order) {
        products.clear();
        for (OrderItem item : order.getOrderItems()) {
            products.put(item.getProduct(), item.getQuantity());
        }
        updateOrderListView();
    }

    private void updateOrderListView() {
        vBoxListOrder.getChildren().clear();

        for (Product product : products.keySet()) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/cardOrder.fxml"));
                HBox orderItem = loader.load();

                orderItem.setPrefWidth(334);
                orderItem.setPrefHeight(100);
                orderItem.setAlignment(Pos.CENTER);
                orderItem.setPadding(new Insets(6));
                orderItem.setStyle("-fx-background-color: #fff; -fx-background-radius: 5px");

                OrderCardController controller = loader.getController();
                controller.setOrderItem(product, products.get(product), vBoxListOrder, products);
                controller.setMenuDetailController(this);
                orderItem.setUserData(controller);


                vBoxListOrder.getChildren().add(orderItem);
            } catch (IOException e) {
                e.printStackTrace();
            }

            double totalHeight = 0;
            for (Node node : vBoxListOrder.getChildren()) {
                totalHeight += node.prefHeight(1);
            }
            vBoxListOrder.setPrefHeight(totalHeight);
        }
        calculateTotalOrder();
    }

    private void calculateTotalOrder() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            BigDecimal productPrice = product.getPrice();
            BigDecimal productTotalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(productTotalPrice);
        }
        labelTotalOrder.setText(formatPriceUtil.formatPrice(totalPrice));
    }

    public void updateTotalOrder(BigDecimal totalOrder) {
        labelTotalOrder.setText(formatPriceUtil.formatPrice(totalOrder));
    }



    String orderNoteStr = null;

    String radioPaymentMethod = null;



    @FXML
    private void saveOrder() {
        if (products.isEmpty()) {
            alertNotification.showAlert("Thông báo", "Giỏ hàng trống. Không thể lưu đơn hàng.");
            return;
        }

        orderNoteStr = orderNote.getText().trim();

        Order existingOrder = odao.findUnpaidOrderByTable(table.getTableNumber());

        if (existingOrder == null) {
            Order newOrder = new Order();
            newOrder.setCode(uniqueCodeBillGenerator.generateCodeBill());
            newOrder.setUser(loggedInUser);
            newOrder.setTable(table);
            newOrder.setPaymentMethod(radioPaymentMethod);
            newOrder.setNote(orderNoteStr);
            newOrder.setStatus(OrderStatus.UNPAID);

            List<OrderItem> orderItemList = new ArrayList<>();
            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(entry.getKey());
                orderItem.setQuantity(entry.getValue());
                orderItemList.add(orderItem);
            }
            newOrder.setOrderItems(orderItemList);

            if (odao.saveOrder(newOrder)) {
                alertNotification.showAlert("Thông báo", "Hóa đơn ở bàn số " + table.getTableNumber() + " đã được tạo.");
                clearOrderDetails();
            } else {
                alertNotification.showAlert("Thông báo", "Không thể tạo hóa đơn.");
            }
        } else {
            existingOrder.setUser(loggedInUser);
            existingOrder.setPaymentMethod(radioPaymentMethod);
            existingOrder.setNote(orderNoteStr);

            List<OrderItem> orderItemList = new ArrayList<>();
            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);

                if (existingOrder.containsProduct(product)) {
                    existingOrder.updateProductQuantity(product, quantity);
                } else {
                    orderItemList.add(orderItem);
                }
            }


            existingOrder.addOrderItems(orderItemList);

            if (odao.updateSavaOrder(table.getTableNumber(), existingOrder)) {
                alertNotification.showAlert("Thông báo", "Đã cập nhật đơn hàng.");
                clearOrderDetails();

            } else {
                alertNotification.showAlert("Thông báo", "Không thể cập nhật đơn hàng.");
            }

        }
        System.out.println(products);
    }

    @FXML
    public void checkoutSavedOrder() {

        Order order = odao.findUnpaidOrderByTable(table.getTableNumber());

        Toggle selectedToggle = paymentMethodToggleGroup.getSelectedToggle();

        if (selectedToggle == null) {
            alertNotification.showAlert("Thông báo", "Vui lòng chọn phương thức thanh toán.");
            return;
        }


        orderNoteStr = orderNote.getText().trim();

        if (order != null) {
            order.setUser(loggedInUser);
            order.setPaymentMethod(radioPaymentMethod);
            order.setNote(orderNoteStr);
            order.setStatus(OrderStatus.PAID);
            List<OrderItem> orderItemList = new ArrayList<>();
            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);

                if (order.containsProduct(product)) {
                    order.updateProductQuantity(product, quantity);
                } else {
                    orderItemList.add(orderItem);
                }
            }


            order.addOrderItems(orderItemList);

            if (odao.updateSavaOrder(table.getTableNumber(), order)) {
                if (odao.checkoutSavedOrder(table.getTableNumber(), order)) {
                    alertNotification.showAlert("Thông báo", "Đã thanh toán thành công.");
                    clearOrderDetails();
                } else {
                    alertNotification.showAlert("Thông báo", "Không thể thanh toán. Vui lòng thử lại sau.");
                }

            } else {
                alertNotification.showAlert("Thông báo", "Không thể thanh toán. Vui lòng thử lại sau.");
            }

        } else {
            Order newOrder = new Order();
            newOrder.setCode(uniqueCodeBillGenerator.generateCodeBill());
            newOrder.setUser(loggedInUser);
            newOrder.setTable(table);
            newOrder.setPaymentMethod(radioPaymentMethod);
            newOrder.setNote(orderNoteStr);
            newOrder.setStatus(OrderStatus.PAID);

            List<OrderItem> orderItemList = new ArrayList<>();
            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(entry.getKey());
                orderItem.setQuantity(entry.getValue());
                orderItemList.add(orderItem);
            }
            newOrder.setOrderItems(orderItemList);

            if (odao.checkoutUnsavedOrder(newOrder)) {
                alertNotification.showAlert("Thông báo", "Đã thanh toán thành công.");
                clearOrderDetails();
            } else {
                alertNotification.showAlert("Thông báo", "Không thể thanh toán. Vui lòng thử lại sau.");
            }
        }
    }

    private void clearOrderDetails() {
        products.clear();
        updateOrderListView();
        orderNote.clear();
        paymentMethodToggleGroup.selectToggle(null);
        redirectMenu();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelTotalOrder.setText("0");

        if (loggedInUser != null) {
            labelFullName.setText(loggedInUser.getFullname());
            this.setRole(loggedInUser.getRole().toString());
        }

        paymentMethodToggleGroup = new ToggleGroup();
        radioButtonCash.setToggleGroup(paymentMethodToggleGroup);
        radioButtonBankTransfer.setToggleGroup(paymentMethodToggleGroup);
        radioButtonEWallet.setToggleGroup(paymentMethodToggleGroup);

        paymentMethodToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                radioPaymentMethod = selectedRadioButton.getText();
            }
        });


        findAllCategory();
        findAllProductActive();

    }


    @FXML
    public void redirectMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/menu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setTitle("Bàn");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        }  catch (Exception e) {
            System.out.println("redirectTable(): " + e.getMessage());
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
            Parent root = FXMLLoader.load(Main.class.getResource("/order.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnOrder.getScene().getWindow();
            stage.setTitle("Hóa đơn");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
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
    public void redirectAuthentication() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/authentication.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnAuthentication.getScene().getWindow();
            stage.setTitle("Phân quyền");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        }  catch (Exception e) {
            System.out.println("redirectAuthentication(): " + e.getMessage());
        }
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

