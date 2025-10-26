package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.view.loaimonan.LoaiMonAnDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public boolean themMoiLoaiMonAn() {
        LoaiMonAnDialog dialog = new LoaiMonAnDialog(null, this);
        dialog.showAndWait();

        LoaiMon ketQua = dialog.layKetQua();
        if (ketQua != null) {
            ketQua.setMaLoaiMon(loaiMonDAO.taoMaLoaiMonMoi());
            return loaiMonDAO.themLoaiMon(ketQua);
        }
        return false;
    }

    public boolean tuyChinhLoaiMonAn(LoaiMon loaiMon) {
        LoaiMonAnDialog dialog = new LoaiMonAnDialog(loaiMon, this);
        dialog.showAndWait();

        LoaiMon ketQua = dialog.layKetQua();
        if (ketQua != null) {
            return loaiMonDAO.capNhatLoaiMon(ketQua);
        }
        return false;
    }

    public boolean xoaLoaiMonAn(LoaiMon loaiMon) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa loại món ăn: " + loaiMon.getTenLoaiMon() + "?");
        confirmAlert.setContentText("Hành động này không thể hoàn tác.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return loaiMonDAO.xoaLoaiMon(loaiMon.getMaLoaiMon());
        }
        return false;
    }

    /**
     * Sao chép một tệp hình ảnh từ bên ngoài vào thư mục tài nguyên của dự án.
     *
     * @param sourceImagePath Đường dẫn tuyệt đối đến tệp ảnh gốc.
     * @return Đường dẫn tương đối (relative path) bên trong classpath để lưu vào CSDL,
     *         hoặc null nếu có lỗi.
     */
    public String saoChepHinhAnhVaoProject(String sourceImagePath) {
        try {
            // 1. Xác định thư mục đích trong resources
            String relativeDestDir = "/com/thefourrestaurant/images/LoaiMonAn/";
            URL resourceDirUrl = getClass().getResource(relativeDestDir);

            File destDir;
            if (resourceDirUrl == null) {
                // Nếu thư mục chưa tồn tại, tạo nó trong thư mục build (target/classes)
                URL rootUrl = getClass().getResource("/");
                if (rootUrl == null) {
                    throw new IOException("Không thể tìm thấy thư mục gốc của resources.");
                }
                destDir = new File(new File(rootUrl.toURI()).getAbsolutePath() + relativeDestDir.replace("/", File.separator));
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
            } else {
                destDir = new File(resourceDirUrl.toURI());
            }


            // 2. Tạo tên tệp mới để tránh trùng lặp
            File sourceFile = new File(sourceImagePath);
            String originalFileName = sourceFile.getName();
            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i);
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // 3. Thực hiện sao chép
            Path sourcePath = sourceFile.toPath();
            Path destPath = new File(destDir, newFileName).toPath();
            Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);

            // 4. Trả về đường dẫn tương đối để lưu vào DB
            return relativeDestDir + newFileName;

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }
}
