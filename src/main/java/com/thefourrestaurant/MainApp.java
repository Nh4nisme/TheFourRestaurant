package com.thefourrestaurant;

import java.sql.Connection;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.view.GiaoDienDangNhap;
import com.thefourrestaurant.view.ban.GiaoDienDatBan;
import com.thefourrestaurant.view.GiaoDienChinh;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Nạp font
        Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Bold.ttf"), 14);

        // Hiển thị giao diện chính ngay lập tức
        GiaoDienChinh giaoDienChinh = new GiaoDienChinh();
        GiaoDienDangNhap gddn = new  GiaoDienDangNhap();

        gddn.show(primaryStage);
        // Tạo Scene với DatBan
//        Scene scene = new javafx.scene.Scene(datBan, 1400, 800);
//        primaryStage.setTitle("Quản lý Nhà hàng - Đặt bàn");
//        primaryStage.setScene(scene);
//        primaryStage.show();

        // Sau khi giao diện đã mở, chạy kết nối DB ở thread riêng
        Task<Connection> ketNoiTask = new Task<>() {
            @Override
            protected Connection call() throws Exception {
                return ConnectSQL.getConnection();
            }
        };

        ketNoiTask.setOnSucceeded(e -> {
            Connection conn = ketNoiTask.getValue();
            if (conn != null) {
                System.out.println("✅ Kết nối SQL Server thành công!");
            } else {
                hienThongBaoLoi("❌ Kết nối thất bại.\nVui lòng kiểm tra lại thông tin kết nối.");
            }
        });

        ketNoiTask.setOnFailed(e -> {
            hienThongBaoLoi("❌ Không thể kết nối đến SQL Server.\nChi tiết: " + ketNoiTask.getException().getMessage());
        });

        new Thread(ketNoiTask).start();
    }

    private void hienThongBaoLoi(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi kết nối");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}