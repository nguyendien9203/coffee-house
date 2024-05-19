package vn.aptech.c2304l.learning.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.dal.OrderDAO;
import vn.aptech.c2304l.learning.model.Order;
import vn.aptech.c2304l.learning.model.OrderItem;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.FormatPriceUtil;
import vn.aptech.c2304l.learning.utils.UserSession;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {
    private OrderDAO odao = new OrderDAO();
    private FormatPriceUtil formatPriceUtil = FormatPriceUtil.getInstance();
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
    private TableColumn<Order, String> colCodeBill;

    @FXML
    private TableColumn<Order, String> colOrderStatus;

    @FXML
    private TableColumn<Order, String> colTotalBill;

    @FXML
    private ComboBox<String> filter;

    @FXML
    private Label labelBillStaff;

    @FXML
    private Label labelCodeBill;

    @FXML
    private Label labelFullName;

    @FXML
    private Label labelOrderEndTime;

    @FXML
    private Label labelOrderStartTime;

    @FXML
    private Label labelPaymentMethod;

    @FXML
    private Button btnTableNumber;

    @FXML
    private Label labelTotalBill;

    @FXML
    private TableView<Order> tableView;

    @FXML
    private TextArea txtOrderNote;

    @FXML
    private TextField txtSearch;

    @FXML
    private VBox vBoxListOrder;

    @FXML
    private VBox vBoxEmpty;

    @FXML
    private VBox vBoxBillDetail;

    ObservableList<Order> filteredProducts;
    ObservableList<Order> searchResult;


    private String[] listOptions = {"Xóa bộ lọc", "PAID", "UNPAID"};

    public void listFilter() {
        List<String> list = new ArrayList<>();

        for(String s : listOptions) {
            list.add(s);
        }

        ObservableList listData = FXCollections.observableArrayList(list);
        filter.setItems(listData);
    }

    public void findAll() {
        ObservableList<Order> listData = odao.findAll();

        colCodeBill.setCellValueFactory(new PropertyValueFactory<>("code"));
        colTotalBill.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            return new SimpleStringProperty(formatPriceUtil.formatPrice(order.calculateTotalAmount()));
        });

        tableView.setItems(listData);
        colOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(listData);
    }

    private void addTableViewSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateOrderDetails(newSelection);
                displayOrderItems(newSelection.getOrderItems());
                vBoxEmpty.setVisible(false);
                vBoxBillDetail.setVisible(true);
            } else {
                vBoxEmpty.setVisible(true);
                vBoxBillDetail.setVisible(false);
            }
        });
    }

    private void updateOrderDetails(Order order) {
        labelCodeBill.setText(order.getCode());
        labelOrderStartTime.setText(order.getOrderStartTime().toString());
        labelOrderEndTime.setText(order.getOrderEndTime().toString());
        labelBillStaff.setText(order.getUser().getFullname());
        labelPaymentMethod.setText(order.getPaymentMethod());
        txtOrderNote.setText(order.getNote());
        btnTableNumber.setText(String.valueOf(order.getTable().getTableNumber()));
        labelTotalBill.setText(formatPriceUtil.formatPrice(order.calculateTotalAmount()));
    }

    private void displayOrderItems(List<OrderItem> orderItems) {
        vBoxListOrder.getChildren().clear();
        for (OrderItem item : orderItems) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/cardBill.fxml"));
                Parent cardNode = loader.load();
                CardBillController controller = loader.getController();
                controller.setOrderItem(item);
                vBoxListOrder.getChildren().add(cardNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void search() {
        String keyWord = txtSearch.getText().trim();
        String selectedFilter = filter.getValue();

        if (selectedFilter == null) {
            searchResult = odao.search(keyWord);
        } else {
            if(selectedFilter.equals("Xóa bộ lọc")) {
                searchResult = odao.search(keyWord);
            }else {
                searchResult = odao.search(keyWord, selectedFilter, filteredProducts);
            }

        }

        tableView.setItems(searchResult);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (loggedInUser != null) {
            labelFullName.setText(loggedInUser.getFullname());
        }

        listFilter();


        vBoxEmpty.setVisible(true);
        vBoxBillDetail.setVisible(false);

        filter.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (txtSearch.getText().isEmpty()) {
                    if(newSelection.equals("Xóa bộ lọc")) {
                        findAll();
                    }else {
                        filteredProducts = odao.filter(newSelection);
                        tableView.setItems(filteredProducts);
                    }
                } else {
                    if (newSelection.equals("Xóa bộ lọc")) {
                        tableView.setItems(searchResult);
                    } else {
                        filteredProducts = odao.filter(newSelection, searchResult);
                        tableView.setItems(filteredProducts);
                    }
                }
            }
        });

        findAll();
        addTableViewSelectionListener();

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
    public void redirectProduct() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/product.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnProduct.getScene().getWindow();
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
