package vn.aptech.c2304l.learning.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.dal.OrderDAO;
import vn.aptech.c2304l.learning.model.Order;
import vn.aptech.c2304l.learning.model.Table;
import vn.aptech.c2304l.learning.utils.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

public class NumOfSeatsController implements Initializable {
    private OrderDAO odao = new OrderDAO();
    @FXML
    private VBox table2Seats;

    @FXML
    private VBox table4Seats;

    @FXML
    private VBox table6Seats;

    @FXML
    private Button txtTable2Seats;

    @FXML
    private Button txtTable4Seats;

    @FXML
    private Button txtTable6Seats;

    private Table table;

    //private ObservableList<Order> orders;

    public void setData(Table table) {
        //this.orders = odao.findAll();
        this.table = table;
        if (table.getNumOfSeats() == 2) {
            txtTable2Seats.setText(String.valueOf(table.getTableNumber()));
            table2Seats.setVisible(true);
            table4Seats.setVisible(false);
            table6Seats.setVisible(false);
            checkAndSetButtonColor(txtTable2Seats);
        } else if (table.getNumOfSeats() == 4) {
            txtTable4Seats.setText(String.valueOf(table.getTableNumber()));
            table2Seats.setVisible(false);
            table4Seats.setVisible(true);
            table6Seats.setVisible(false);
            checkAndSetButtonColor(txtTable4Seats);
        } else if (table.getNumOfSeats() == 6) {
            txtTable6Seats.setText(String.valueOf(table.getTableNumber()));
            table2Seats.setVisible(false);
            table4Seats.setVisible(false);
            table6Seats.setVisible(true);
            checkAndSetButtonColor(txtTable6Seats);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void checkAndSetButtonColor(Button button) {
        int tableNumber = Integer.parseInt(button.getText());
        ObservableList<Order> unpaidOrders = odao.findUnpaidOrdersByTable(tableNumber);
        if (!unpaidOrders.isEmpty()) {
            button.setStyle("-fx-background-color: #966C3A; -fx-text-fill: #ffffff;");
        }
    }

    private int selectedTableNumber;


    @FXML
    private void handleTable2SeatsButton(ActionEvent event) {
        txtTable2Seats = (Button) event.getSource();
        selectedTableNumber = Integer.parseInt(txtTable2Seats.getText());
        loadMenuItemScene(table, txtTable2Seats);
    }

    @FXML
    private void handleTable4SeatsButton(ActionEvent event) {
        txtTable4Seats = (Button) event.getSource();
        selectedTableNumber = Integer.parseInt(txtTable4Seats.getText());
        loadMenuItemScene(table, txtTable4Seats);
    }

    @FXML
    private void handleTable6SeatsButton(ActionEvent event) {
        txtTable6Seats = (Button) event.getSource();
        selectedTableNumber = Integer.parseInt(txtTable6Seats.getText());
        loadMenuItemScene(table, txtTable6Seats);
    }

    private void loadMenuItemScene(Table table, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/menuDetail.fxml"));
            Parent root = loader.load();
            MenuDetailController controller = loader.getController();
            controller.setTable(table);

            ObservableList<Order> orders = odao.findUnpaidOrdersByTable(table.getTableNumber());
            if (!orders.isEmpty()) {
                Order order = orders.get(0);
                controller.loadOrderItems(order);
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setTitle("Order");
            stage.setResizable(false);
            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            System.out.println("loadMenuItemScene(): " + e.getMessage());
            e.printStackTrace();
        }
    }
}
