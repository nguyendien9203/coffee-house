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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.constant.ProductStatus;
import vn.aptech.c2304l.learning.dal.CategoryDAO;
import vn.aptech.c2304l.learning.dal.ProductDAO;
import vn.aptech.c2304l.learning.model.Category;
import vn.aptech.c2304l.learning.model.Product;
import vn.aptech.c2304l.learning.model.Table;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.AlertConfirmation;
import vn.aptech.c2304l.learning.utils.AlertNotification;
import vn.aptech.c2304l.learning.utils.UserSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ProductController implements Initializable {

    private CategoryDAO cdao = new CategoryDAO();
    private ProductDAO pdao = new ProductDAO();
    private AlertNotification alertNotification = new AlertNotification();
    private AlertConfirmation alertConfirmation = new AlertConfirmation();

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
    private TableColumn<Product, Integer> colId;

    @FXML
    private TableColumn<Product, String> colCategory;

    @FXML
    private TableColumn<Product, String> colDescription;

    @FXML
    private TableColumn<Product, String> colName;

    @FXML
    private TableColumn<Product, String> colImage;

    @FXML
    private TableColumn<Product, BigDecimal> colPrice;

    @FXML
    private TableColumn<Product, String> colStatus;

    @FXML
    private ComboBox<String> filter;

    @FXML
    private ComboBox<String> txtCategory;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtImage;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<String> txtStatus;

    @FXML
    private AnchorPane imgPreview;

    @FXML
    private TableView<Product> tableView;

    @FXML
    private Label labelFullName;
    private String[] listOptions = {"Xóa bộ lọc", "Giá tăng dần", "Giá giảm dần"};

    public void listFilter() {
        List<String> list = new ArrayList<>();

        for(String s : listOptions) {
            list.add(s);
        }

        ObservableList listData = FXCollections.observableArrayList(list);
        filter.setItems(listData);
    }


    public void categories() {
        List<Category> categoryList = cdao.findAll();
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }
        txtCategory.setItems(categoryNames);
    }


    ObservableList<String> statusList = FXCollections.observableArrayList();
    public void statusList() {


        for (ProductStatus status : ProductStatus.values()) {
            if (status == ProductStatus.AVAILABLE) {
                statusList.add("Có sẵn");
            } else if (status == ProductStatus.UNAVAILABLE) {
                statusList.add("Không có sẵn");
            }
        }

        txtStatus.setItems(statusList);
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

    private  int id;
    private int categoryId;
    private String name;
    private BigDecimal price;
    private String image;
    private String description;
    private String status;

    ObservableList<Product> filteredProducts;
    ObservableList<Product> searchResult;


    private boolean checkField() {
        if(txtName.getText().isBlank()) {
            alertNotification.showAlert("Thông báo", "Vui lòng nhập tên sản phẩm.");
            return false;
        }else {
            name = txtName.getText().trim();
        }

        try {
            if (txtPrice.getText().isBlank()) {
                alertNotification.showAlert("Thông báo", "Vui lòng nhập giá sản phẩm.");
                return false;
            } else {
                price = new BigDecimal(txtPrice.getText().trim());
            }
        } catch (NumberFormatException e) {
            alertNotification.showAlert("Thông báo", "Giá sản phẩm không hợp lệ.");
            return false;
        }

        if(txtCategory.getValue() ==  null) {
            alertNotification.showAlert("Thông báo", "Vui lòng chọn danh mục sản phẩm sản phẩm.");
            return false;
        }else {
            status = txtStatus.getValue().toString().trim();
        }

        if(txtStatus.getValue() ==  null) {
            alertNotification.showAlert("Thông báo", "Vui lòng chọn trạng thái sản phẩm sản phẩm.");
            return false;
        }else {
            status = txtStatus.getValue().toString().trim();
        }

        image = txtImage.getText().trim();
        description = txtDescription.getText().trim();


        return true;
    }

    public void uploadFile() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Chọn tệp để tải lên");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Hình ảnh (*.png, *.jpg)", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            long fileSizeInBytes = selectedFile.length();
            long fileSizeInKB = fileSizeInBytes / 1024;
            long fileSizeInMB = fileSizeInKB / 1024;

            String fileName = selectedFile.getName();
            boolean isPngOrJpg = fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg");

            boolean isSquare = false;
            if (isPngOrJpg) {
                Image image = new Image(selectedFile.toURI().toString());

                double width = image.getWidth();
                double height = image.getHeight();
                isSquare = (width == height);
            }

            if(fileSizeInMB > 2) {
                alertNotification.showAlert("Thông báo", "Tệp không vượt quá 2MB.");
                return;
            }

            if(!isPngOrJpg) {
                alertNotification.showAlert("Thông báo", "Tệp phải có định dạng .png hoặc .jpg.");
                return;
            }

            if(!isSquare) {
                alertNotification.showAlert("Thông báo", "Tệp phải có tỉ lệ 1:1.");
                return;
            }


            String fileNameOnly = selectedFile.getName();
            txtImage.setText(fileNameOnly);

            ImageView imageView = new ImageView(new Image(selectedFile.toURI().toString()));

            imageView.setFitWidth(imgPreview.getPrefWidth());
            imageView.setFitHeight(imgPreview.getPrefHeight());

            imgPreview.getChildren().clear();

            imgPreview.getChildren().add(imageView);

            try {
                String destinationFolder = "uploads";
                Path destinationPath = Paths.get(destinationFolder);
                if (!Files.exists(destinationPath)) {
                    Files.createDirectories(destinationPath);
                }
                String fileNameUpload = selectedFile.getName();
                Path destinationFile = Paths.get(destinationFolder, fileNameUpload);
                Files.copy(selectedFile.toPath(), destinationFile);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public void findAll() {
        ObservableList<Product> listData = pdao.findAll();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colCategory.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            return new SimpleStringProperty(product.getCategory().getName());
        });


        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));



        tableView.setItems(listData);
    }

    private void displayImagePreview(String imagePath) {
        String uploadsFolderPath = "uploads/";
        String fullPath = uploadsFolderPath + imagePath;

        File file = new File(fullPath);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(imgPreview.getPrefWidth());
            imageView.setFitHeight(imgPreview.getPrefHeight());
            imgPreview.getChildren().clear();
            imgPreview.getChildren().add(imageView);
        }
    }

    private void addTableViewSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(String.valueOf(newSelection.getId()));
                txtName.setText(String.valueOf(newSelection.getName()));
                txtPrice.setText(String.valueOf(newSelection.getPrice()));
                txtDescription.setText(String.valueOf(newSelection.getDescription()));
                txtCategory.setValue(newSelection.getCategory().getName());
                txtStatus.setValue(newSelection.getStatus());
                txtImage.setText(String.valueOf(newSelection.getImage()));
                displayImagePreview(txtImage.getText());
            }
        });
    }

    @FXML
    public void insert() {
        if(!checkField()) {
            return;
        }

        if(pdao.findByName(name)) {
            alertNotification.showAlert("Thông báo", "Sản phẩm " + name + " đã tồn tại");
            return;
        }

        Product product = new Product();

        Category category = new Category();
        category.setId(categoryId);

        product.setCategory(category);
        product.setName(name);
        product.setPrice(price);
        product.setImage(image);
        product.setDescription(description);
        product.setStatus(status);

        if (pdao.insert(product)) {
           alertNotification.showAlert("Thông báo", "Thêm sản phẩm " + name + " thành công");

           txtName.clear();
           txtPrice.clear();
           txtCategory.getSelectionModel().clearSelection();
           txtStatus.getSelectionModel().clearSelection();
           txtImage.clear();
           imgPreview.getChildren().clear();
           txtDescription.clear();

           findAll();

        } else {
            alertNotification.showAlert("Thông báo", "Thêm sản phẩm " + name + " thất bại");
        }
    }

    @FXML
    public void update() {
        if(!checkField()) {
            return;
        }

        if(txtId.getText() != null) {
            id = Integer.parseInt(txtId.getText());
        }

        Product product = new Product();
        product.setId(id);

        Category category = new Category();
        category.setId(categoryId);

        product.setCategory(category);
        product.setName(name);
        product.setPrice(price);
        product.setImage(image);
        product.setDescription(description);
        product.setStatus(status);

        if(pdao.update(product)) {
            alertNotification.showAlert("Thông báo", "Cập nhật sản phẩm " + name + " thành công");

            txtName.clear();
            txtPrice.clear();
            txtCategory.getSelectionModel().clearSelection();
            txtStatus.getSelectionModel().clearSelection();
            txtImage.clear();
            imgPreview.getChildren().clear();
            txtDescription.clear();

            findAll();
        }else {
            alertNotification.showAlert("Thông báo", "Cập nhật sản phẩm " + name + " thất bại");
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

        Product product = new Product();

        product.setId(id);

        boolean alertConfirm = alertConfirmation.showAlert("Thông báo", "Bạn muốn xóa sản phẩm " + name + "?");

        if(alertConfirm) {
            if(pdao.delete(product.getId())) {
                alertNotification.showAlert("Thông báo", "Xóa sản phẩm " + name + " thành công");

                txtName.clear();
                txtPrice.clear();
                txtCategory.getSelectionModel().clearSelection();
                txtStatus.getSelectionModel().clearSelection();
                txtImage.clear();
                imgPreview.getChildren().clear();
                txtDescription.clear();

                findAll();
            }else {
                alertNotification.showAlert("Thông báo", "Xóa sản phẩm " + name + " thất bại");
            }
        }
    }

    @FXML
    public void search() {
        String keyWord = txtSearch.getText().trim();
        String selectedFilter = filter.getValue();

        if (selectedFilter == null) {
            searchResult = pdao.search(keyWord);
        } else {
            if(selectedFilter.equals("Xóa bộ lọc")) {
                searchResult = pdao.search(keyWord);
            }else {
                searchResult = pdao.search(keyWord, selectedFilter, filteredProducts);
            }

        }

        tableView.setItems(searchResult);
    }


    @FXML
    public void exportToExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Workbook workbook = new HSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Product");

                // Write header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Name");
                headerRow.createCell(1).setCellValue("Price");
                headerRow.createCell(2).setCellValue("Category");
                headerRow.createCell(3).setCellValue("Description");

                sheet.setColumnWidth(0, 20 * 256);
                sheet.setColumnWidth(1, 20 * 256);
                sheet.setColumnWidth(2, 20 * 256);
                sheet.setColumnWidth(3, 20 * 256);

                // Write data rows
                ObservableList<Product> productData = tableView.getItems();
                for (int i = 0; i < productData.size(); i++) {
                    Product product = productData.get(i);
                    Row dataRow = sheet.createRow(i + 1);
                    dataRow.createCell(0).setCellValue(product.getName());
                    dataRow.createCell(1).setCellValue(String.valueOf(product.getPrice()));
                    dataRow.createCell(2).setCellValue(product.getCategory().getName());
                    dataRow.createCell(3).setCellValue(product.getDescription());
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

                    String name = row.getCell(0).getStringCellValue();
                    Cell priceCell = row.getCell(1);
                    BigDecimal price = null;
                    String category = row.getCell(2).getStringCellValue();
                    String description = row.getCell(3).getStringCellValue();


                    if(pdao.findByName(name)) {
                        alertNotification.showAlert("Thông báo", "Sản phẩm " + name + " đã tồn tại");
                        return;
                    }

                    Product product = new Product();
                    product.setName(name);

                    if (priceCell.getCellType() == CellType.NUMERIC) {
                        price = BigDecimal.valueOf(priceCell.getNumericCellValue());
                        product.setPrice(price);
                    } else if (priceCell.getCellType() == CellType.STRING) {
                        alertNotification.showAlert("Thông báo", "Giá của sản phẩm " + name + " không hợp lệ");
                        return;
                    }

                    int categoryId = cdao.findIdByName(category);
                    if (categoryId != -1) {
                        Category oldCategory = new Category();
                        oldCategory.setId(categoryId);
                        product.setCategory(oldCategory);
                    } else {

                        Category newCategory = new Category();
                        newCategory.setName(category);
                        if(cdao.insert(newCategory)) {
                            int newCategoryId = cdao.findIdByName(newCategory.getName());
                            newCategory.setId(newCategoryId);
                            product.setCategory(newCategory);
                        }

                    }


                    product.setDescription(description);
                    product.setStatus(statusList.get(0));


                    if(pdao.insert(product)) {
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

        categories();
        statusList();
        listFilter();

        filter.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (txtSearch.getText().isEmpty()) {
                    if(newSelection.equals("Xóa bộ lọc")) {
                        findAll();
                    }else {
                        filteredProducts = pdao.filter(newSelection);
                        tableView.setItems(filteredProducts);
                    }
                } else {
                    if (newSelection.equals("Xóa bộ lọc")) {
                        tableView.setItems(searchResult);
                    } else {
                        filteredProducts = pdao.filter(newSelection, searchResult);
                        tableView.setItems(filteredProducts);
                    }
                }
            }
        });



        txtCategory.setOnAction(event -> {
            String selectedCategoryName = txtCategory.getValue();
            if (selectedCategoryName != null) {
                categoryId = cdao.findIdByName(selectedCategoryName);
            }
        });

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
