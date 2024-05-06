package vn.aptech.c2304l.learning;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/table.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setResizable(false); // Đặt cửa sổ không thể thay đổi kích thước
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}