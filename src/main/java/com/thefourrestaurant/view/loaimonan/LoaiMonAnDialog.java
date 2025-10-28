package com.thefourrestaurant.view.loaimonan;

import com.thefourrestaurant.controller.LoaiMonAnController;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.view.components.ButtonSample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class LoaiMonAnDialog extends Stage {

    private LoaiMon ketQua = null;
    private File tepAnhDaChon = null;
    private final boolean isEditMode;
    private final LoaiMon loaiMonHienTai;
    private final LoaiMonAnController controller;

    private final TextField truongTen = new TextField();
    private final ImageView khungHinhAnh = new ImageView();

    public LoaiMonAnDialog(LoaiMon loaiMon, LoaiMonAnController controller) {
        this.loaiMonHienTai = loaiMon;
        this.isEditMode = (loaiMon != null);
        this.controller = controller;

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(isEditMode ? "Tùy Chỉnh Loại Món Ăn" : "Thêm Loại Món Ăn Mới");

        BorderPane layoutChinh = new BorderPane();

        // --- Phần đầu ---
        Label nhanTieuDe = new Label(isEditMode ? "Tùy chỉnh loại món ăn" : "Thêm loại món ăn mới");
        nhanTieuDe.setStyle("-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox phanDau = new HBox(nhanTieuDe);
        phanDau.setAlignment(Pos.CENTER_LEFT);
        phanDau.setPadding(new Insets(15));
        phanDau.setStyle("-fx-background-color: #1E424D;");

        // --- Nội dung ---
        VBox hopAnh = taoPhanAnh();
        GridPane luoiForm = taoForm();

        VBox noiDungGiua = new VBox(20, hopAnh, luoiForm);
        noiDungGiua.setPadding(new Insets(20));

        // --- Phần chân trang ---
        HBox phanCuoi = taoFooter();

        // --- Bố cục ---
        layoutChinh.setTop(phanDau);
        layoutChinh.setCenter(noiDungGiua);
        layoutChinh.setBottom(phanCuoi);

        // --- Điền dữ liệu cho chế độ chỉnh sửa ---
        if (isEditMode) {
            dienDuLieuHienCo();
        }

        // --- Khung cảnh ---
        Scene khungCanh = new Scene(layoutChinh, 450, 420);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
    }

    private VBox taoPhanAnh() {
        VBox hopAnh = new VBox(5);
        hopAnh.setAlignment(Pos.CENTER);
        hopAnh.setPrefSize(120, 120);
        hopAnh.setMinSize(120, 120);
        hopAnh.setMaxSize(120, 120);
        hopAnh.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #CCCCCC; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-style: dashed;");
        hopAnh.setCursor(Cursor.HAND);

        try (InputStream luongAnh = getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png")) {
            if (luongAnh != null) {
                khungHinhAnh.setImage(new Image(luongAnh));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        khungHinhAnh.setFitWidth(100);
        khungHinhAnh.setFitHeight(100);

        hopAnh.getChildren().add(khungHinhAnh);

        hopAnh.setOnMouseClicked(event -> chonAnh());

        VBox container = new VBox(hopAnh);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private GridPane taoForm() {
        GridPane luoiForm = new GridPane();
        luoiForm.setHgap(10);
        luoiForm.setVgap(15);

        Label nhanTen = new Label("Tên loại:");
        truongTen.setPromptText("Nhập tên loại món ăn");

        luoiForm.add(nhanTen, 0, 0);
        luoiForm.add(truongTen, 1, 0);
        GridPane.setHgrow(truongTen, Priority.ALWAYS);

        return luoiForm;
    }

    private HBox taoFooter() {
        HBox phanCuoi = new HBox(10);
        phanCuoi.setPadding(new Insets(15));
        phanCuoi.setAlignment(Pos.CENTER_RIGHT);
        phanCuoi.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        Button nutLuu = new ButtonSample("Lưu", 35, 14, 2);
        Button nutHuy = new ButtonSample("Hủy", 35, 14, 2);

        nutHuy.setOnAction(e -> this.close());
        nutLuu.setOnAction(e -> luuThayDoi());

        phanCuoi.getChildren().addAll(nutLuu, nutHuy);
        return phanCuoi;
    }

    private void dienDuLieuHienCo() {
        truongTen.setText(loaiMonHienTai.getTenLoaiMon());
        String imagePath = loaiMonHienTai.getHinhAnh();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image anh;
                if (imagePath.startsWith("/")) { // Đây là đường dẫn tài nguyên
                    URL imageUrl = getClass().getResource(imagePath);
                    if (imageUrl != null) {
                        anh = new Image(imageUrl.toExternalForm());
                    } else {
                        return; // Giữ ảnh mặc định nếu không tìm thấy tài nguyên
                    }
                } else { // Có thể là một URL đầy đủ (ví dụ: file:/...)
                    anh = new Image(imagePath);
                }

                if (!anh.isError()) {
                    khungHinhAnh.setImage(anh);
                }
            } catch (Exception e) {
                System.err.println("Could not load image: " + imagePath);
                e.printStackTrace();
            }
        }
    }

    private void chonAnh() {
        FileChooser boChonTep = new FileChooser();
        boChonTep.setTitle("Chọn Ảnh");
        boChonTep.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File tep = boChonTep.showOpenDialog(this);
        if (tep != null) {
            tepAnhDaChon = tep;
            try {
                Image anh = new Image(tep.toURI().toString());
                khungHinhAnh.setImage(anh);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void luuThayDoi() {
        String tenLoaiMonAn = truongTen.getText();
        if (tenLoaiMonAn == null || tenLoaiMonAn.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Tên loại món ăn không được để trống!");
            return;
        }

        if (isEditMode) {
            ketQua = this.loaiMonHienTai;
        } else {
            ketQua = new LoaiMon();
        }

        ketQua.setTenLoaiMon(tenLoaiMonAn);

        if (tepAnhDaChon != null) {
            String newImagePath = controller.saoChepHinhAnhVaoProject(tepAnhDaChon.getAbsolutePath());
            if (newImagePath != null) {
                ketQua.setHinhAnh(newImagePath);
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi khi sao chép hình ảnh!");
                return;
            }
        } else if (!isEditMode) {
            ketQua.setHinhAnh(null); // Không có ảnh mới cho mục mới
        }
        // nếu đang chỉnh sửa và không chọn ảnh mới, đường dẫn ảnh cũ được giữ mặc định

        this.close();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(this);
        alert.showAndWait();
    }

    public LoaiMon layKetQua() {
        return ketQua;
    }
}
