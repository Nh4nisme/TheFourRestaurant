package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.view.loaimonan.LoaiMonAnDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoaiMonAnController {

    private final LoaiMonDAO loaiMonDAO;

    public LoaiMonAnController() {
        this.loaiMonDAO = new LoaiMonDAO();
    }

    public List<LoaiMon> layTatCaLoaiMonAn() {
        return loaiMonDAO.layTatCaLoaiMon();
    }

    public boolean themMoiLoaiMonAn(Stage owner) {
        LoaiMonAnDialog dialog = new LoaiMonAnDialog(null, this);
        dialog.initOwner(owner);
        dialog.showAndWait();

        LoaiMon ketQua = dialog.layKetQua();
        if (ketQua != null) {
            ketQua.setMaLoaiMon(loaiMonDAO.taoMaLoaiMonMoi());
            if (loaiMonDAO.themLoaiMon(ketQua)) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Thêm loại món ăn thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Thêm loại món ăn thất bại.");
                return false;
            }
        }
        return false;
    }

    public boolean tuyChinhLoaiMonAn(Stage owner, LoaiMon loaiMon) {
        LoaiMonAnDialog dialog = new LoaiMonAnDialog(loaiMon, this);
        dialog.initOwner(owner);
        dialog.showAndWait();

        LoaiMon ketQua = dialog.layKetQua();
        if (ketQua != null) {
            if (loaiMonDAO.capNhatLoaiMon(ketQua)) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Cập nhật loại món ăn thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Cập nhật loại món ăn thất bại.");
                return false;
            }
        }
        return false;
    }

    public boolean xoaLoaiMonAn(Stage owner, LoaiMon loaiMon) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa loại món ăn: " + loaiMon.getTenLoaiMon() + "?");
        confirmAlert.setContentText("Hành động này không thể hoàn tác.");
        confirmAlert.initOwner(owner);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (loaiMonDAO.xoaLoaiMon(loaiMon.getMaLoaiMon())) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Xóa loại món ăn thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Xóa loại món ăn thất bại.");
                return false;
            }
        }
        return false;
    }

    public String saoChepHinhAnhVaoProject(String sourceImagePath) {
        try {
            // 1. Tạo tên tệp mới để tránh trùng lặp
            File sourceFile = new File(sourceImagePath);
            String originalFileName = sourceFile.getName();
            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i);
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // 2. Xác định đường dẫn tương đối trong classpath
            String classpathRelativePath = "/com/thefourrestaurant/images/LoaiMonAn/" + newFileName;

            // 3. Sao chép vào thư mục `src/main/resources`
            String projectDir = System.getProperty("user.dir");
            Path srcDestPath = Paths.get(projectDir, "src/main/resources", classpathRelativePath);
            Files.createDirectories(srcDestPath.getParent());
            Files.copy(sourceFile.toPath(), srcDestPath, StandardCopyOption.REPLACE_EXISTING);

            // 4. Sao chép vào thư mục `target/classes` để ứng dụng đang chạy có thể thấy ngay
            URL targetRootUrl = getClass().getResource("/");
            if (targetRootUrl != null) {
                Path targetDestPath = Paths.get(targetRootUrl.toURI()).resolve(classpathRelativePath.substring(1));
                Files.createDirectories(targetDestPath.getParent());
                Files.copy(sourceFile.toPath(), targetDestPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 5. Trả về đường dẫn tương đối (classpath resource path) để lưu vào DB
            return classpathRelativePath;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(Stage owner, Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
