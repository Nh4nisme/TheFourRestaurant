package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class KhuyenMaiDialog extends Stage {

    private KhuyenMai ketQua = null;
    private final boolean isEditMode;
    private final KhuyenMai khuyenMaiHienTai;
    private final KhuyenMaiController controller; // Thêm controller vào đây

    // UI Components
    private final TextField truongMaKM = new TextField();
    private final TextField truongMoTa = new TextField();
    private final ComboBox<LoaiKhuyenMai> loaiKhuyenMaiComboBox = new ComboBox<>();
    private final TextField truongTyLe = new TextField();
    private final TextField truongSoTien = new TextField();
    private final DatePicker ngayBatDauPicker = new DatePicker();
    private final DatePicker ngayKetThucPicker = new DatePicker();

    public KhuyenMaiDialog(KhuyenMai khuyenMai, List<LoaiKhuyenMai> tatCaLoaiKhuyenMai, String maKMMoi, KhuyenMaiController controller) {
        this.khuyenMaiHienTai = khuyenMai;
        this.isEditMode = (khuyenMai != null);
        this.controller = controller; // Gán controller

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(isEditMode ? "Tùy Chỉnh Khuyến Mãi" : "Thêm Khuyến Mãi Mới");

        Font fontMontserrat = null;
        try (InputStream luongFont = getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")) {
            if (luongFont != null) {
                fontMontserrat = Font.loadFont(luongFont, 14);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải font: " + e.getMessage());
        }

        String kieuFontStyle = (fontMontserrat != null) ? "-fx-font-family: '" + fontMontserrat.getFamily() + "';" : "";

        BorderPane layoutChinh = new BorderPane();


        Label nhanTieuDe = new Label(isEditMode ? "Tùy chỉnh khuyến mãi" : "Thêm khuyến mãi");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        GridPane luoiFormChinh = createMainForm(tatCaLoaiKhuyenMai, kieuFontStyle, maKMMoi);
        VBox hopGiua = new VBox(20, luoiFormChinh);
        hopGiua.setPadding(new Insets(20));

        HBox hopChanTrang = createFooter();

        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(hopGiua);
        layoutChinh.setBottom(hopChanTrang);

        if (isEditMode) {
            dienDuLieuHienCo();
        }

        Scene khungCanh = new Scene(layoutChinh, 500, 550);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        this.setHeight(550); // Chiều cao cố định
    }

    private GridPane createMainForm(List<LoaiKhuyenMai> tatCaLoaiKhuyenMai, String kieuFontStyle, String maKMMoi) {
        GridPane luoiForm = new GridPane();
        luoiForm.setVgap(12);
        luoiForm.setHgap(15);

        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        truongMaKM.setStyle(kieuTruongNhap); truongMaKM.getStyleClass().add("text-field");
        truongMaKM.setEditable(false);
        truongMaKM.setText(isEditMode ? khuyenMaiHienTai.getMaKM() : maKMMoi);

        truongMoTa.setStyle(kieuTruongNhap); truongMoTa.getStyleClass().add("text-field");
        loaiKhuyenMaiComboBox.setStyle(kieuTruongNhap); loaiKhuyenMaiComboBox.getStyleClass().add("combo-box");
        truongTyLe.setStyle(kieuTruongNhap); truongTyLe.getStyleClass().add("text-field");
        truongSoTien.setStyle(kieuTruongNhap); truongSoTien.getStyleClass().add("text-field");
        ngayBatDauPicker.setStyle(kieuTruongNhap); ngayBatDauPicker.getStyleClass().add("date-picker");
        ngayKetThucPicker.setStyle(kieuTruongNhap); ngayKetThucPicker.getStyleClass().add("date-picker");


        luoiForm.add(new Label("Mã KM:"), 0, 0);
        luoiForm.add(truongMaKM, 1, 0);

        luoiForm.add(new Label("Mô tả:"), 0, 1);
        luoiForm.add(truongMoTa, 1, 1);

        luoiForm.add(new Label("Loại KM:"), 0, 2);
        loaiKhuyenMaiComboBox.setItems(FXCollections.observableArrayList(tatCaLoaiKhuyenMai));
        loaiKhuyenMaiComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(LoaiKhuyenMai object) { return object == null ? "" : object.getTenLoaiKM(); }
            @Override public LoaiKhuyenMai fromString(String string) { return null; }
        });
        luoiForm.add(loaiKhuyenMaiComboBox, 1, 2);

        luoiForm.add(new Label("Tỷ lệ (%):"), 0, 3);
        luoiForm.add(truongTyLe, 1, 3);

        luoiForm.add(new Label("Số tiền:"), 0, 4);
        luoiForm.add(truongSoTien, 1, 4);

        luoiForm.add(new Label("Ngày BĐ:"), 0, 5);
        luoiForm.add(ngayBatDauPicker, 1, 5);

        luoiForm.add(new Label("Ngày KT:"), 0, 6);
        luoiForm.add(ngayKetThucPicker, 1, 6);

        return luoiForm;
    }

    private HBox createFooter() {
        HBox hopChanTrang = new HBox(10);
        hopChanTrang.setPadding(new Insets(15));
        hopChanTrang.setAlignment(Pos.CENTER_RIGHT);
        hopChanTrang.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        Button nutLuu = new ButtonSample("Lưu", 35, 14, 2);
        Button nutHuy = new ButtonSample("Hủy", 35, 14, 2);

        hopChanTrang.getChildren().addAll(nutLuu, nutHuy);

        // Thêm nút "Quản lý chi tiết" nếu đang ở chế độ chỉnh sửa
        if (isEditMode) {
            ButtonSample btnQuanLyChiTiet = new ButtonSample("Quản lý chi tiết", 35, 14, 2);
            btnQuanLyChiTiet.setOnAction(e -> {
                // Mở ChiTietKhuyenMaiManagerDialog
                ChiTietKhuyenMaiManagerDialog chiTietDialog = new ChiTietKhuyenMaiManagerDialog(khuyenMaiHienTai, controller);
                chiTietDialog.showAndWait();
            });
            hopChanTrang.getChildren().add(0, btnQuanLyChiTiet); // Thêm vào đầu HBox
        }

        nutHuy.setOnAction(e -> this.close());
        nutLuu.setOnAction(e -> luuThayDoi());

        return hopChanTrang;
    }

    private void dienDuLieuHienCo() {
        truongMoTa.setText(khuyenMaiHienTai.getMoTa());
        loaiKhuyenMaiComboBox.setValue(khuyenMaiHienTai.getLoaiKhuyenMai());
        if (khuyenMaiHienTai.getTyLe() != null) {
            truongTyLe.setText(khuyenMaiHienTai.getTyLe().stripTrailingZeros().toPlainString());
        }
        if (khuyenMaiHienTai.getSoTien() != null) {
            truongSoTien.setText(khuyenMaiHienTai.getSoTien().stripTrailingZeros().toPlainString());
        }
        ngayBatDauPicker.setValue(khuyenMaiHienTai.getNgayBatDau());
        ngayKetThucPicker.setValue(khuyenMaiHienTai.getNgayKetThuc());
    }

    private void luuThayDoi() {
        if (truongMoTa.getText().trim().isEmpty() || loaiKhuyenMaiComboBox.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập Mô tả và chọn Loại khuyến mãi!").showAndWait();
            return;
        }

        BigDecimal tyLe = null;
        if (!truongTyLe.getText().trim().isEmpty()) {
            try {
                tyLe = new BigDecimal(truongTyLe.getText().trim());
                if (tyLe.compareTo(BigDecimal.ZERO) < 0 || tyLe.compareTo(new BigDecimal(100)) > 0) {
                    new Alert(Alert.AlertType.WARNING, "Tỷ lệ phải là số từ 0 đến 100!").showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Tỷ lệ phải là một con số hợp lệ!").showAndWait();
                return;
            }
        }

        BigDecimal soTien = null;
        if (!truongSoTien.getText().trim().isEmpty()) {
            try {
                soTien = new BigDecimal(truongSoTien.getText().trim());
                if (soTien.compareTo(BigDecimal.ZERO) < 0) {
                    new Alert(Alert.AlertType.WARNING, "Số tiền không được là số âm!").showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Số tiền phải là một con số hợp lệ!").showAndWait();
                return;
            }
        }

        if (tyLe != null && soTien != null) {
            new Alert(Alert.AlertType.WARNING, "Không thể nhập cả Tỷ lệ và Số tiền cùng lúc!").showAndWait();
            return;
        }
        if (tyLe == null && soTien == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập Tỷ lệ hoặc Số tiền!").showAndWait();
            return;
        }

        LocalDate ngayBD = ngayBatDauPicker.getValue();
        LocalDate ngayKT = ngayKetThucPicker.getValue();

        if (ngayBD != null && ngayKT != null && ngayKT.isBefore(ngayBD)) {
            new Alert(Alert.AlertType.WARNING, "Ngày kết thúc không được trước ngày bắt đầu!").showAndWait();
            return;
        }

        if (isEditMode) {
            ketQua = this.khuyenMaiHienTai;
        }
        else {
            ketQua = new KhuyenMai();
            ketQua.setMaKM(truongMaKM.getText()); // Gán mã KM mới được tạo
        }

        ketQua.setMoTa(truongMoTa.getText().trim());
        ketQua.setLoaiKhuyenMai(loaiKhuyenMaiComboBox.getValue());
        ketQua.setTyLe(tyLe);
        ketQua.setSoTien(soTien);
        ketQua.setNgayBatDau(ngayBD);
        ketQua.setNgayKetThuc(ngayKT);

        this.close();
    }


    public KhuyenMai layKetQua() {
        return ketQua;
    }
}
