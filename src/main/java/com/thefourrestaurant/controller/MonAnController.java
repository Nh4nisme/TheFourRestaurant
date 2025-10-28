package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.DAO.MonAnDAO;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.monan.MonAnDialog;
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

public class MonAnController {

    private final MonAnDAO monAnDAO;
    private final LoaiMonDAO loaiMonDAO;

    public MonAnController() {
        this.monAnDAO = new MonAnDAO();
        this.loaiMonDAO = new LoaiMonDAO();
    }

    public List<MonAn> layMonAnTheoLoai(String maLoaiMon) {
        return monAnDAO.layMonAnTheoLoai(maLoaiMon);
    }

    public List<LoaiMon> layTatCaLoaiMonAn() {
        return loaiMonDAO.layTatCaLoaiMon();
    }

    public boolean themMoiMonAn(Stage owner, String maLoaiMonDefault) {
        List<LoaiMon> allLoaiMon = layTatCaLoaiMonAn();

        if (allLoaiMon.isEmpty()) {
            showAlert(owner, Alert.AlertType.ERROR, "Không có loại món ăn nào trong CSDL. Vui lòng thêm loại món ăn trước.");
            return false;
        }

        LoaiMon defaultLoaiMon = allLoaiMon.stream()
                .filter(lm -> lm.getMaLoaiMon().equals(maLoaiMonDefault))
                .findFirst()
                .orElse(null);

        MonAnDialog dialog = new MonAnDialog(null, allLoaiMon, defaultLoaiMon, this);
        dialog.initOwner(owner);
        dialog.showAndWait();

        MonAn ketQua = dialog.layKetQua();
        if (ketQua != null) {
            ketQua.setMaMonAn(monAnDAO.taoMaMonAnMoi());
            if (monAnDAO.themMonAn(ketQua)) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Thêm món ăn thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Thêm món ăn thất bại.");
                return false;
            }
        }
        return false;
    }

    public boolean tuyChinhMonAn(Stage owner, MonAn monAn) {
        List<LoaiMon> allLoaiMon = layTatCaLoaiMonAn();

        MonAnDialog dialog = new MonAnDialog(monAn, allLoaiMon, monAn.getLoaiMon(), this);
        dialog.initOwner(owner);
        dialog.showAndWait();

        MonAn ketQua = dialog.layKetQua();
        if (ketQua != null) {
            if (monAnDAO.capNhatMonAn(ketQua)) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Cập nhật món ăn thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Cập nhật món ăn thất bại.");
                return false;
            }
        }
        return false;
    }

    public boolean xoaMonAn(Stage owner, MonAn monAn) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa món: " + monAn.getTenMon() + "?");
        confirmAlert.setContentText("Hành động này không thể hoàn tác.");
        confirmAlert.initOwner(owner);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (monAnDAO.xoaMonAn(monAn.getMaMonAn())) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Xóa món ăn thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Xóa món ăn thất bại.");
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
            String classpathRelativePath = "/com/thefourrestaurant/images/MonAn/" + newFileName;

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
