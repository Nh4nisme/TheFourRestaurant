package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.NhanVienDAO;
import com.thefourrestaurant.model.NhanVien;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

public class NhanVienController {

    private final NhanVienDAO dao = new NhanVienDAO();

    public List<NhanVien> layDanhSachNhanVien() {
        return dao.layDanhSachNhanVien();
    }

    public NhanVien layNhanVienTheoMa(String maNV) {
        return dao.layNhanVienTheoMa(maNV);
    }
    
    public String saoChepHinhAnhVaoProject(String sourceImagePath) {
        try {
            File sourceFile = new File(sourceImagePath);
            String originalFileName = sourceFile.getName();
            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i);
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            String classpathRelativePath = "/com/thefourrestaurant/images/NhanVien/" + newFileName;

            String projectDir = System.getProperty("user.dir");
            Path srcDestPath = Paths.get(projectDir, "src/main/resources", classpathRelativePath);
            Files.createDirectories(srcDestPath.getParent());
            Files.copy(sourceFile.toPath(), srcDestPath, StandardCopyOption.REPLACE_EXISTING);

            URL targetRootUrl = getClass().getResource("/");
            if (targetRootUrl != null) {
                Path targetDestPath = Paths.get(targetRootUrl.toURI()).resolve(classpathRelativePath.substring(1));
                Files.createDirectories(targetDestPath.getParent());
                Files.copy(sourceFile.toPath(), targetDestPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return classpathRelativePath;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showAlert(Stage owner, Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        if (owner != null) alert.initOwner(owner);
        alert.showAndWait();
    }

    public boolean capNhatNhanVien(com.thefourrestaurant.model.NhanVien nv, java.io.File imageFile) {
        if (imageFile != null) {
            try {
                saoChepHinhAnhVaoProject(imageFile.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return dao.capNhatNhanVien(nv);
    }
}
