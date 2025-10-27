package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.collections.FXCollections;
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
import java.time.LocalDateTime;
import java.util.List;

public class KhuyenMaiDialog extends Stage {

    private KhuyenMai ketQua = null;
    private final boolean laCheDoChinhSua;
    private final KhuyenMai khuyenMaiHienTai;
    private final KhuyenMaiController boDieuKhien;

    private final TextField truongMaKM = new TextField();
    private final TextField truongTenKM = new TextField();
    private final TextField truongMoTa = new TextField();
    private final ComboBox<LoaiKhuyenMai> hopChonLoaiKhuyenMai = new ComboBox<>();
    private final TextField truongTyLe = new TextField();
    private final TextField truongSoTien = new TextField();
    private final DatePicker boChonNgayBatDau = new DatePicker();
    private final DatePicker boChonNgayKetThuc = new DatePicker();

    public KhuyenMaiDialog(KhuyenMai khuyenMai, List<LoaiKhuyenMai> danhSachTatCaLoaiKhuyenMai, String maKhuyenMaiMoi, KhuyenMaiController boDieuKhien) {
        this.khuyenMaiHienTai = khuyenMai;
        this.laCheDoChinhSua = (khuyenMai != null);
        this.boDieuKhien = boDieuKhien;

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(laCheDoChinhSua ? "Tùy Chỉnh Khuyến Mãi" : "Thêm Khuyến Mãi Mới");

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

        Label nhanTieuDe = new Label(laCheDoChinhSua ? "Tùy chỉnh khuyến mãi" : "Thêm khuyến mãi");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        GridPane luoiFormChinh = taoFormChinh(danhSachTatCaLoaiKhuyenMai, kieuFontStyle, maKhuyenMaiMoi);
        VBox hopGiua = new VBox(20, luoiFormChinh);
        hopGiua.setPadding(new Insets(20));

        HBox hopChanTrang = taoChanTrang();

        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(hopGiua);
        layoutChinh.setBottom(hopChanTrang);

        if (laCheDoChinhSua) {
            dienDuLieuHienCo();
        }

        Scene khungCanh = new Scene(layoutChinh, 500, 550);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        this.setHeight(550);
    }

    private GridPane taoFormChinh(List<LoaiKhuyenMai> danhSachTatCaLoaiKhuyenMai, String kieuFontStyle, String maKhuyenMaiMoi) {
        GridPane luoiForm = new GridPane();
        luoiForm.setVgap(12);
        luoiForm.setHgap(15);

        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        truongMaKM.setStyle(kieuTruongNhap);
        truongMaKM.getStyleClass().add("text-field");
        truongMaKM.setEditable(false);
        truongMaKM.setText(laCheDoChinhSua ? khuyenMaiHienTai.getMaKM() : maKhuyenMaiMoi);

        truongTenKM.setStyle(kieuTruongNhap);
        truongTenKM.getStyleClass().add("text-field");

        truongMoTa.setStyle(kieuTruongNhap);
        truongMoTa.getStyleClass().add("text-field");
        hopChonLoaiKhuyenMai.setStyle(kieuTruongNhap);
        hopChonLoaiKhuyenMai.getStyleClass().add("combo-box");
        truongTyLe.setStyle(kieuTruongNhap);
        truongTyLe.getStyleClass().add("text-field");
        truongSoTien.setStyle(kieuTruongNhap);
        truongSoTien.getStyleClass().add("text-field");
        boChonNgayBatDau.setStyle(kieuTruongNhap);
        boChonNgayBatDau.getStyleClass().add("date-picker");
        boChonNgayKetThuc.setStyle(kieuTruongNhap);
        boChonNgayKetThuc.getStyleClass().add("date-picker");

        luoiForm.add(new Label("Mã KM:"), 0, 0);
        luoiForm.add(truongMaKM, 1, 0);

        luoiForm.add(new Label("Tên KM:"), 0, 1);
        luoiForm.add(truongTenKM, 1, 1);
        luoiForm.add(new Label("Mô tả:"), 0, 2);
        luoiForm.add(truongMoTa, 1, 2);

        luoiForm.add(new Label("Loại KM:"), 0, 3);
        hopChonLoaiKhuyenMai.setItems(FXCollections.observableArrayList(danhSachTatCaLoaiKhuyenMai));
        hopChonLoaiKhuyenMai.setConverter(new StringConverter<>() {
            @Override
            public String toString(LoaiKhuyenMai object) {
                return object == null ? "" : object.getTenLoaiKM();
            }

            @Override
            public LoaiKhuyenMai fromString(String string) {
                return null;
            }
        });
        luoiForm.add(hopChonLoaiKhuyenMai, 1, 3);

        luoiForm.add(new Label("Tỷ lệ (%):"), 0, 4);
        luoiForm.add(truongTyLe, 1, 4);

        luoiForm.add(new Label("Số tiền:"), 0, 5);
        luoiForm.add(truongSoTien, 1, 5);

        luoiForm.add(new Label("Ngày BĐ:"), 0, 6);
        luoiForm.add(boChonNgayBatDau, 1, 6);

        luoiForm.add(new Label("Ngày KT:"), 0, 7);
        luoiForm.add(boChonNgayKetThuc, 1, 7);

        return luoiForm;
    }

    private HBox taoChanTrang() {
        HBox hopChanTrang = new HBox(10);
        hopChanTrang.setPadding(new Insets(15));
        hopChanTrang.setAlignment(Pos.CENTER_RIGHT);
        hopChanTrang.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        Button nutLuu = new ButtonSample("Lưu", 35, 14, 2);
        Button nutHuy = new ButtonSample("Hủy", 35, 14, 2);

        hopChanTrang.getChildren().addAll(nutLuu, nutHuy);

        if (laCheDoChinhSua) {
            // Thêm nút Quản lý khung giờ
            ButtonSample nutQuanLyKhungGio = new ButtonSample("Quản lý khung giờ", 35, 14, 2);
            nutQuanLyKhungGio.setOnAction(e -> {
                KhungGioManagerDialog khungGioDialog = new KhungGioManagerDialog(khuyenMaiHienTai.getMaKM());
                khungGioDialog.initOwner(this);
                khungGioDialog.showAndWait();
            });
            hopChanTrang.getChildren().add(0, nutQuanLyKhungGio);

            ButtonSample nutQuanLyChiTiet = new ButtonSample("Quản lý chi tiết", 35, 14, 2);
            nutQuanLyChiTiet.setOnAction(e -> {
                ChiTietKhuyenMaiManagerDialog chiTietDialog = new ChiTietKhuyenMaiManagerDialog(khuyenMaiHienTai, boDieuKhien);
                chiTietDialog.initOwner(this);
                chiTietDialog.showAndWait();
            });
            hopChanTrang.getChildren().add(1, nutQuanLyChiTiet);
        }

        nutHuy.setOnAction(e -> this.close());
        nutLuu.setOnAction(e -> luuThayDoi());

        return hopChanTrang;
    }

    private void dienDuLieuHienCo() {
        truongTenKM.setText(khuyenMaiHienTai.getTenKM());
        truongMoTa.setText(khuyenMaiHienTai.getMoTa());
        hopChonLoaiKhuyenMai.setValue(khuyenMaiHienTai.getLoaiKhuyenMai());
        if (khuyenMaiHienTai.getTyLe() != null) {
            truongTyLe.setText(khuyenMaiHienTai.getTyLe().stripTrailingZeros().toPlainString());
        }
        if (khuyenMaiHienTai.getSoTien() != null) {
            truongSoTien.setText(khuyenMaiHienTai.getSoTien().stripTrailingZeros().toPlainString());
        }
        if (khuyenMaiHienTai.getNgayBatDau() != null) {
            boChonNgayBatDau.setValue(khuyenMaiHienTai.getNgayBatDau().toLocalDate());
        }
        if (khuyenMaiHienTai.getNgayKetThuc() != null) {
            boChonNgayKetThuc.setValue(khuyenMaiHienTai.getNgayKetThuc().toLocalDate());
        }
    }

    private void luuThayDoi() {
        // 1. Kiểm tra các trường cơ bản
        if (truongTenKM.getText().trim().isEmpty() || truongMoTa.getText().trim().isEmpty() || hopChonLoaiKhuyenMai.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập Tên, Mô tả và chọn Loại khuyến mãi!");
            return;
        }

        LoaiKhuyenMai loaiKM = hopChonLoaiKhuyenMai.getValue();
        String tenLoaiKM = loaiKM.getTenLoaiKM();
        BigDecimal tyLe = null;
        BigDecimal soTien = null;

        // 2. Kiểm tra logic dựa trên Loại Khuyến Mãi
        if ("Giảm giá theo tỷ lệ".equals(tenLoaiKM)) {
            if (!truongSoTien.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Với 'Giảm giá theo tỷ lệ', ô 'Số tiền' phải để trống.");
                return;
            }
            if (truongTyLe.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Với 'Giảm giá theo tỷ lệ', bạn phải nhập 'Tỷ lệ'.");
                return;
            }
            try {
                tyLe = new BigDecimal(truongTyLe.getText().trim());
                if (tyLe.compareTo(BigDecimal.ZERO) <= 0 || tyLe.compareTo(new BigDecimal(100)) > 0) {
                    showAlert(Alert.AlertType.WARNING, "Tỷ lệ phải là số lớn hơn 0 và nhỏ hơn hoặc bằng 100!");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Tỷ lệ phải là một con số hợp lệ!");
                return;
            }
        } else if ("Giảm giá theo giá trị".equals(tenLoaiKM)) {
            if (!truongTyLe.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Với 'Giảm giá theo giá trị', ô 'Tỷ lệ' phải để trống.");
                return;
            }
            if (truongSoTien.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Với 'Giảm giá theo giá trị', bạn phải nhập 'Số tiền'.");
                return;
            }
            try {
                soTien = new BigDecimal(truongSoTien.getText().trim());
                if (soTien.compareTo(BigDecimal.ZERO) <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Số tiền phải là số lớn hơn 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Số tiền phải là một con số hợp lệ!");
                return;
            }
        } else if ("Tặng món ăn".equals(tenLoaiKM)) {
            if (!truongTyLe.getText().trim().isEmpty() || !truongSoTien.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Với 'Tặng món ăn', ô 'Tỷ lệ' và 'Số tiền' phải để trống.");
                return;
            }
        }

        // 3. Kiểm tra ngày tháng
        LocalDate ngayBD_localDate = boChonNgayBatDau.getValue();
        LocalDate ngayKT_localDate = boChonNgayKetThuc.getValue();

        if (ngayBD_localDate != null && ngayKT_localDate != null && ngayKT_localDate.isBefore(ngayBD_localDate)) {
            showAlert(Alert.AlertType.WARNING, "Ngày kết thúc không được trước ngày bắt đầu!");
            return;
        }

        LocalDateTime ngayBD = (ngayBD_localDate != null) ? ngayBD_localDate.atStartOfDay() : null;
        LocalDateTime ngayKT = (ngayKT_localDate != null) ? ngayKT_localDate.atStartOfDay() : null;

        // 4. Tạo hoặc cập nhật đối tượng KhuyenMai
        if (laCheDoChinhSua) {
            ketQua = this.khuyenMaiHienTai;
        } else {
            ketQua = new KhuyenMai();
            ketQua.setMaKM(truongMaKM.getText());
        }

        ketQua.setTenKM(truongTenKM.getText().trim());
        ketQua.setMoTa(truongMoTa.getText().trim());
        ketQua.setLoaiKhuyenMai(loaiKM);
        ketQua.setTyLe(tyLe); // Sẽ là null nếu không phải loại giảm giá theo tỷ lệ
        ketQua.setSoTien(soTien); // Sẽ là null nếu không phải loại giảm giá theo giá trị
        ketQua.setNgayBatDau(ngayBD);
        ketQua.setNgayKetThuc(ngayKT);

        this.close();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(this);
        alert.showAndWait();
    }

    public KhuyenMai layKetQua() {
        return ketQua;
    }
}
