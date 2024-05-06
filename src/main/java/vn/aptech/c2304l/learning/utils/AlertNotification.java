package vn.aptech.c2304l.learning.utils;
import javafx.scene.control.Alert;

public class AlertNotification {
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
