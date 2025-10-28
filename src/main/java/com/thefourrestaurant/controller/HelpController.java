package com.thefourrestaurant.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class HelpController {

    public void openHelpFile(Stage owner) {
        String resourcePath = "/com/thefourrestaurant/help/help.pdf";
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                showAlert(owner, Alert.AlertType.ERROR, "Không tìm thấy tệp trợ giúp!", "Vui lòng đảm bảo tệp 'help.pdf' tồn tại trong thư mục tài nguyên.");
                return;
            }

            // Tạo một tệp tạm thời để sao chép nội dung PDF vào
            File tempFile = File.createTempFile("restaurant-help-", ".pdf");
            tempFile.deleteOnExit(); // Đảm bảo tệp tạm sẽ bị xóa khi ứng dụng tắt

            // Sao chép nội dung từ resource stream vào tệp tạm
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Mở tệp tạm bằng ứng dụng mặc định của hệ điều hành
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Lỗi hệ thống", "Không thể mở tệp trên hệ điều hành này.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(owner, Alert.AlertType.ERROR, "Đã xảy ra lỗi", "Không thể mở tệp trợ giúp: " + e.getMessage());
        }
    }

    private void showAlert(Stage owner, Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
