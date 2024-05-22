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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.dal.TableDAO;
import vn.aptech.c2304l.learning.model.Table;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.AlertConfirmation;
import vn.aptech.c2304l.learning.utils.AlertNotification;
import vn.aptech.c2304l.learning.utils.UserSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class TableController implements Initializable {

    private TableDAO tdao = new TableDAO();
    private AlertNotification alertNotification = new AlertNotification();
    private AlertConfirmation alertConfirmation = new AlertConfirmation();

    private User loggedInUser = UserSession.getInstance().getLoggedInUser();

    @FXML
    private VBox btnAuthentication;

    @FXML
    private VBox btnCategory;

    @FXML
    private ComboBox<Integer> filter;

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
    private TableColumn<Table, Integer> colId;

    @FXML
    private TableColumn<Table, Integer> colNumOfSeats;

    @FXML
    private TableColumn<Table, Integer> colTableNumber;

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

    @FXML
    private Label labelFullName;
    private int[] seats = {2, 4, 6};

    public void numOfSeats() {
        List<Integer> numOfSeats = new ArrayList<>();

        for(int i : seats) {
            numOfSeats.add(i);
        }

        ObservableList listData = FXCollections.observableArrayList(numOfSeats);
        txtNumOfSeats.setItems(listData);
        filter.setItems(listData);
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

    private int id;
    private int tableNumber;
    private int numOfSeats;

    private boolean checkField() {
        String tableNumberStr = txtTableNumbers.getText().trim();
        Object numOfSeatsObj = txtNumOfSeats.getValue();

        if(tableNumberStr.isBlank() || numOfSeatsObj == null) {
            alertNotification.showAlert("Thông báo", "Vui lòng nhập đầy đủ thông tin.");
            return false;
        }

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
    public void insert() {
        if(!checkField()) {
            return;
        }

        if(tdao.findByTableNumber(tableNumber)) {
            alertNotification.showAlert("Thông báo", "Bàn số " + tableNumber + " đã tồn tại");
            return;
        }

        Table table = new Table();
        table.setTableNumber(tableNumber);
        table.setNumOfSeats(numOfSeats);


        if(tdao.insert(table)) {
            alertNotification.showAlert("Thông báo", "Thêm bàn số " + tableNumber + " thành công");

            txtTableNumbers.clear();
            txtNumOfSeats.getSelectionModel().clearSelection();

            findAll();
        }else {
            alertNotification.showAlert("Thông báo", "Thêm bàn số " + tableNumber + " thất bại");
        }
    }

    @FXML
    public void update() {
        if(!checkField()) {
            return;
        }

        if(tdao.findByTableNumberAndSeats(tableNumber, numOfSeats)) {
            alertNotification.showAlert("Thông báo", "Bàn số " + tableNumber + " đã tồn tại");
            return;
        }

        if(txtId.getText() != null) {
            id = Integer.parseInt(txtId.getText().trim());
        }

        Table table = new Table();

        table.setId(id);
        table.setTableNumber(tableNumber);
        table.setNumOfSeats(numOfSeats);

        if(tdao.update(table)) {
            alertNotification.showAlert("Thông báo", "Cập nhật bàn số " + tableNumber + " thành công");

            txtTableNumbers.clear();
            txtNumOfSeats.getSelectionModel().clearSelection();

            findAll();
        }else {
            alertNotification.showAlert("Thông báo", "Cập nhật bàn số " + tableNumber + " thất bại");
        }
    }

    @FXML
    public void delete() {
        if(!checkField()) {
            return;
        }

        if(txtId.getText() != null) {
            id = Integer.parseInt(txtId.getText().trim());
        }

        Table table = new Table();

        table.setId(id);

        boolean alertConfirm = alertConfirmation.showAlert("Thông báo", "Bạn muốn xóa bàn số " + tableNumber + "?");

        if(alertConfirm) {
            if(tdao.delete(table.getId())) {
                alertNotification.showAlert("Thông báo", "Xóa bàn số " + tableNumber + " thành công");

                txtTableNumbers.clear();
                txtNumOfSeats.getSelectionModel().clearSelection();

                findAll();
            }else {
                alertNotification.showAlert("Thông báo", "Xóa bàn số " + tableNumber + " thất bại");
            }
        }
    }

    @FXML
    public void search() {
        String keyWord = txtSearch.getText().trim();
        ObservableList<Table> searchResult = tdao.search(keyWord);
        tableView.setItems(searchResult);
    }

    @FXML
    public void exportToExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Workbook workbook = new HSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Table");



                // Write header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Table Number");
                headerRow.createCell(1).setCellValue("Number of Seats");


                sheet.setColumnWidth(0, 20 * 256);
                sheet.setColumnWidth(1, 20 * 256);

                // Write data rows
                ObservableList<Table> tableData = tableView.getItems();
                for (int i = 0; i < tableData.size(); i++) {
                    Table table = tableData.get(i);
                    Row dataRow = sheet.createRow(i + 1);
                    dataRow.createCell(0).setCellValue(table.getTableNumber());
                    dataRow.createCell(1).setCellValue(table.getNumOfSeats());
                }

                // Write to file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    try {
                        workbook.write(fileOut);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    alertNotification.showAlert("Thông báo", "Dữ liệu đã được xuất ra file Excel thành công.");
                } catch (IOException e) {
                    alertNotification.showAlert("Thông báo", "Xuất dữ liệu ra file Excel thất bại: " + e.getMessage());
                }
            } catch (IOException e) {
                alertNotification.showAlert("Thông báo", "Xuất dữ liệu ra file Excel thất bại: " + e.getMessage());
            }
        }
    }

    @FXML
    public void importFromExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try (FileInputStream fileIn = new FileInputStream(selectedFile)) {
                Workbook workbook = new HSSFWorkbook(fileIn);
                Sheet sheet = workbook.getSheetAt(0);

                boolean flag = false;


                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Skip header row

                    int tableNumber = (int) row.getCell(0).getNumericCellValue();
                    int numOfSeats = (int) row.getCell(1).getNumericCellValue();

                    if(tdao.findByTableNumber(tableNumber)) {
                        alertNotification.showAlert("Thông báo", "Bàn số " + tableNumber + " đã tồn tại");
                        return;
                    }

                    Table table = new Table();
                    table.setTableNumber(tableNumber);
                    table.setNumOfSeats(numOfSeats);

                    if(tdao.insert(table)) {
                        flag = true;
                    }
                }

                if(flag) {
                    alertNotification.showAlert("Thông báo", "Dữ liệu đã được nhập từ file Excel thành công.");
                    findAll();
                } else {
                    alertNotification.showAlert("Thông báo", "Không có dữ liệu nào được nhập từ file Excel.");
                }

            } catch (IOException e) {
                alertNotification.showAlert("Thông báo", "Nhập dữ liệu từ file Excel thất bại: " + e.getMessage());
            }
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (loggedInUser != null) {
            labelFullName.setText(loggedInUser.getFullname());
            this.setRole(loggedInUser.getRole().toString());
        }

        numOfSeats();
        findAll();
        addTableViewSelectionListener();

        filter.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ObservableList<Table> filteredTables = tdao.filter(newSelection);
                tableView.setItems(filteredTables);
            }
        });

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
