package vn.aptech.c2304l.learning.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertConfirmation {

    public boolean showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().clear(); // Xóa các nút hiện có để tạo ra vị trí mới
        alert.getButtonTypes().addAll(buttonTypeCancel, buttonTypeOK); // Thêm nút Cancel trước, sau đó là nút OK

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == buttonTypeOK;
    }
}
