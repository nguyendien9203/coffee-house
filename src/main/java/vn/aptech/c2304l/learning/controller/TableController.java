package vn.aptech.c2304l.learning.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.dal.TableDAO;
import vn.aptech.c2304l.learning.model.Table;
import vn.aptech.c2304l.learning.utils.AlertConfirmation;
import vn.aptech.c2304l.learning.utils.AlertNotification;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TableController implements Initializable {

    private TableDAO tdao = new TableDAO();
    private AlertNotification alertNotification = new AlertNotification();
    private AlertConfirmation alertConfirmation = new AlertConfirmation();

    @FXML
    private Button btnAdd;

    @FXML
    private VBox btnAuthentication;

    @FXML
    private VBox btnCategory;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnExport;

    @FXML
    private ComboBox<?> btnFilter;

    @FXML
    private Button btnImport;

    @FXML
    private VBox btnLogout;

    @FXML
    private VBox btnMenu;

    @FXML
    private VBox btnOrder;

    @FXML
    private VBox btnProduct;

    @FXML
    private Button btnSearch;

    @FXML
    private VBox btnStatistic;

    @FXML
    private VBox btnTable;

    @FXML
    private TableColumn<Table, Integer> colId;

    @FXML
    private TableColumn<Table, Integer> colNumOfSeats;

    @FXML
    private TableColumn<Table, Integer> colTableNumber;

    @FXML
    private AnchorPane tableForm;

    @FXML
    private ComboBox<Integer> txtNumOfSeats;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<Table> tableView;

    @FXML
    private TextField txtTableNumbers;

    @FXML
    private TextField txtId;
    private int[] seats = {2, 4, 6};

    public void numOfSeats() {
        List<Integer> numOfSeats = new ArrayList<>();

        for(int i : seats) {
            numOfSeats.add(i);
        }

        ObservableList listData = FXCollections.observableArrayList(numOfSeats);
        txtNumOfSeats.setItems(listData);
    }

    private int id;
    private int tableNumber;
    private int numOfSeats;

    private boolean checkField() {
        String idStr = txtId.getText();
        String tableNumberStr = txtTableNumbers.getText();
        Object numOfSeatsObj = txtNumOfSeats.getValue();

        if(tableNumberStr.isBlank() || numOfSeatsObj == null) {
            alertNotification.showAlert("Thông báo", "Vui lòng nhập đầy đủ thông tin.");
            return false;
        }

        id = Integer.parseInt(idStr);
        tableNumber = Integer.parseInt(tableNumberStr);
        numOfSeats = Integer.parseInt(numOfSeatsObj.toString());
        return true;
    }

    public void findAll() {
        ObservableList<Table> listData = tdao.findAll();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTableNumber.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        colNumOfSeats.setCellValueFactory(new PropertyValueFactory<>("numOfSeats"));

        tableView.setItems(listData);
    }

    @FXML
    public void insert() {
        if(!checkField()) {
            return;
        }

        if(tdao.findByTableNumber(tableNumber)) {
            alertNotification.showAlert("Thông báo", "Số bàn đã tồn tại");
            return;
        }

        Table table = new Table();
        table.setTableNumber(tableNumber);
        table.setNumOfSeats(numOfSeats);


        if(tdao.insert(table)) {
            alertNotification.showAlert("Thông báo", "Thêm thành công");

            txtTableNumbers.clear();
            txtNumOfSeats.getSelectionModel().clearSelection();

            findAll();
        }else {
            alertNotification.showAlert("Thông báo", "Thêm thất bại");
        }
    }

    // Phương thức để thêm sự kiện lắng nghe vào TableView và hiển thị giá trị của hàng được chọn vào các trường tương ứng
    private void addTableViewSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(String.valueOf(newSelection.getId()));
                txtTableNumbers.setText(String.valueOf(newSelection.getTableNumber()));
                txtNumOfSeats.setValue(newSelection.getNumOfSeats());
            }
        });
    }

    @FXML
    public void update() {
        if(!checkField()) {
            return;
        }

        if(tdao.findByTableNumberAndSeats(tableNumber, numOfSeats)) {
            alertNotification.showAlert("Thông báo", "Bàn này đã tồn tại");
            return;
        }

        Table table = new Table();

        table.setId(id);
        table.setTableNumber(tableNumber);
        table.setNumOfSeats(numOfSeats);

        if(tdao.update(table)) {
            alertNotification.showAlert("Thông báo", "Cập nhật thành công");

            txtTableNumbers.clear();
            txtNumOfSeats.getSelectionModel().clearSelection();

            findAll();
        }else {
            alertNotification.showAlert("Thông báo", "Cập nhật thất bại");
        }
    }

    @FXML
    public void delete() {
        if(!checkField()) {
            return;
        }

        Table table = new Table();

        table.setId(id);

        boolean alertConfirm = alertConfirmation.showAlert("Thông báo", "Bạn muốn xóa bàn này?");

        if(alertConfirm) {
            if(tdao.delete(table.getId())) {
                alertNotification.showAlert("Thông báo", "Xóa thành công");

                txtTableNumbers.clear();
                txtNumOfSeats.getSelectionModel().clearSelection();

                findAll();
            }else {
                alertNotification.showAlert("Thông báo", "Xóa thất bại");
            }
        }
    }









    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnTable.requestFocus();
        btnTable.setFocusTraversable(true);


        numOfSeats();
        findAll();
        addTableViewSelectionListener();

    }

    @FXML
    public void redirectMenu() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/menu.fxml"));

            Scene scene = new Scene(root);


            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setTitle("Menu");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        }  catch (Exception e) {
            System.out.println("redirectMenu(): " + e.getMessage());
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
