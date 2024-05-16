package vn.aptech.c2304l.learning.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.dal.CategoryDAO;
import vn.aptech.c2304l.learning.dal.ProductDAO;
import vn.aptech.c2304l.learning.model.Category;
import vn.aptech.c2304l.learning.model.Order;
import vn.aptech.c2304l.learning.model.Product;
import vn.aptech.c2304l.learning.model.Table;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuDetailController implements Initializable {
    private ProductDAO pdao = new ProductDAO();

    private CategoryDAO cdao = new CategoryDAO();
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
    private GridPane productGridPane;

    private Table table;

    @FXML
    private Button txtTableNumber;

    @FXML
    private VBox vBoxListOrder;

    private Order currentOrder = new Order();

    public void setTable(Table table) {
        this.table = table;
        System.out.println("setTableNumber: " + table);
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

    public void findAllProduct() {
        ObservableList<Product> listData = pdao.findAll();
        displayProducts(listData);
    }

    public void findProductsByCategory(int categoryId) {
        ObservableList<Product> listData = pdao.findAllByCategory(categoryId);
        displayProducts(listData);
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

    public void addProductToOrder(Product product) {
        if (currentOrder.getProducts().containsKey(product)) {
            int currentQuantity = currentOrder.getProducts().get(product);
            currentOrder.getProducts().put(product, currentQuantity + 1);
        } else {
            currentOrder.getProducts().put(product, 1);
        }

        updateOrderListView();
        System.out.println(currentOrder);
    }

    private void updateOrderListView() {
        vBoxListOrder.getChildren().clear();

        for (Product product : currentOrder.getProducts().keySet()) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/cardOrder.fxml"));
                HBox orderItem = loader.load();

                orderItem.setPrefWidth(348);
                orderItem.setPrefHeight(100);
                orderItem.setAlignment(Pos.CENTER);
                orderItem.setPadding(new Insets(6));
                orderItem.setStyle("-fx-background-color: #fff; -fx-background-radius: 5px");

                OrderCardController controller = loader.getController();
                controller.setOrderItem(product, currentOrder, vBoxListOrder);
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
    }

    @FXML
    public void listProductClick() {
        try {
            findAllProduct();
        } catch (Exception e) {
            System.out.println("listProductClick(): " + e.getMessage());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        findAllCategory();
        findAllProduct();

    }




    @FXML
    public void redirectMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/menu.fxml"));
            Parent root = loader.load();

//            MenuDetailController controller = loader.getController();
//            controller.setTableNumber(this.tableNumber);

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

    // Các phần khác của controller ở đây
}

