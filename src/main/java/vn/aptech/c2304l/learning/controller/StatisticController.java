package vn.aptech.c2304l.learning.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vn.aptech.c2304l.learning.Main;
import vn.aptech.c2304l.learning.dal.OrderDAO;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.FormatPriceUtil;
import vn.aptech.c2304l.learning.utils.UserSession;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticController implements Initializable {
    private OrderDAO odao = new OrderDAO();
    private FormatPriceUtil formatPriceUtil = FormatPriceUtil.getInstance();
    private User loggedInUser = UserSession.getInstance().getLoggedInUser();

    @FXML
    private VBox btnAuthentication;

    @FXML
    private VBox btnCategory;

    @FXML
    private Label btnLogout;

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
    private Label labelFullName;

    @FXML
    private Label labelTotalOrderInDay;

    @FXML
    private Label labelTotalRevenueInDay;

    @FXML
    private Label labelTotalRevenueInMonth;

    @FXML
    private LineChart<String, Number> lineChart;

    private void updateTotalOrderInDay() {
        int totalOrderInDay = odao.totalOrderInDay();
        if (totalOrderInDay != -1) {
            labelTotalOrderInDay.setText(String.valueOf(totalOrderInDay));
            System.out.println(" Tổng hóa đơn trong 1 ngày: " + totalOrderInDay);
        } else {
            System.out.println(totalOrderInDay);
        }
    }

    private void updateRevenueDisplays() {
        BigDecimal totalRevenueInDay = odao.totalRevenueInDay();
        if (totalRevenueInDay != null) {
            labelTotalRevenueInDay.setText(formatPriceUtil.formatPrice(totalRevenueInDay));
            System.out.println("Tổng doanh thu trong 1 ngày: " + totalRevenueInDay);
        } else {
            System.out.println(totalRevenueInDay);
        }

        BigDecimal totalRevenueInMonth = odao.totalRevenueInMonth();
        if (totalRevenueInMonth != null) {
            labelTotalRevenueInMonth.setText(formatPriceUtil.formatPrice(totalRevenueInMonth));
            System.out.println("Tổng doanh thu trong 1 tháng: " + totalRevenueInMonth);
        } else {
            System.out.println(totalRevenueInMonth);
        }
    }

    private void setupLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ngày");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu");

        lineChart.setTitle("Thông kê doanh thu hằng ngày");
        lineChart.getXAxis().setLabel("Ngày");
        lineChart.getYAxis().setLabel("Tổng doanh thu");
    }

    private void updateDailyRevenueChart() {
        Map<String, BigDecimal> dailyRevenueData = odao.getDailyRevenueData();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu mỗi ngày");

        for (Map.Entry<String, BigDecimal> entry : dailyRevenueData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (loggedInUser != null) {
            labelFullName.setText(loggedInUser.getFullname());
        }

        updateTotalOrderInDay();

        updateRevenueDisplays();

        setupLineChart();
        updateDailyRevenueChart();
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
