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

    // Regex for validation
    private static final String TEN_KM_REGEX = "^[a-zA-Z0-9]{1,100}$"; // Tên KM là một dãy ký tự không dấu liền nhau, tối đa 100 ký tự
    private static final String MO_TA_REGEX = "^.{1,255}$"; // Mô tả không rỗng, tối đa 255 ký tự
    private static final String TY_LE_REGEX = "^(\\d{1,2}|100)$"; // Tỷ lệ từ 0-100
    private static final String SO_TIEN_REGEX = "^[1-9]\\d*$"; // Số tiền phải lớn hơn 0

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
        truongTyLe.setDisable(true);
        truongSoTien.setStyle(kieuTruongNhap);
        truongSoTien.getStyleClass().add("text-field");
        truongSoTien.setDisable(true);
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

        hopChonLoaiKhuyenMai.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                truongTyLe.setDisable(true);
                truongSoTien.setDisable(true);
                truongTyLe.clear();
                truongSoTien.clear();
                return;
            }
            String tenLoaiKM = newValue.getTenLoaiKM();
            switch (tenLoaiKM) {
                case "Giảm giá theo tỷ lệ":
                    truongTyLe.setDisable(false);
                    truongSoTien.setDisable(true);
                    truongSoTien.clear();
                    break;
                case "Giảm giá theo số tiền":
                    truongTyLe.setDisable(true);
                    truongTyLe.clear();
                    truongSoTien.setDisable(false);
                    break;
                case "Tặng món":
                    truongTyLe.setDisable(true);
                    truongSoTien.setDisable(true);
                    truongTyLe.clear();
                    truongSoTien.clear();
                    break;
                default:
                    truongTyLe.setDisable(false);
                    truongSoTien.setDisable(false);
                    truongTyLe.clear();
                    truongSoTien.clear();
                    break;
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
        // 1. Kiểm tra các trường cơ bản bằng Regex
        if (!truongTenKM.getText().trim().matches(TEN_KM_REGEX)) {
            showAlert(Alert.AlertType.WARNING, "Quy tắc Tên Khuyến Mãi: Phải là một dãy ký tự không dấu, không chứa khoảng trắng, và có độ dài từ 1 đến 100 ký tự.");
            return;
        }
        if (!truongMoTa.getText().trim().matches(MO_TA_REGEX)) {
            showAlert(Alert.AlertType.WARNING, "Quy tắc Mô Tả: Không được để trống và phải có độ dài từ 1 đến 255 ký tự.");
            return;
        }
        if (hopChonLoaiKhuyenMai.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn Loại khuyến mãi!");
            return;
        }

        LoaiKhuyenMai loaiKM = hopChonLoaiKhuyenMai.getValue();
        String tenLoaiKM = loaiKM.getTenLoaiKM();
        BigDecimal tyLe = null;
        BigDecimal soTien = null;

        // 2. Kiểm tra logic dựa trên Loại Khuyến Mãi
        switch (tenLoaiKM) {
            case "Giảm giá theo tỷ lệ":
                if (truongTyLe.getText().trim().isEmpty() || !truongTyLe.getText().trim().matches(TY_LE_REGEX)) {
                    showAlert(Alert.AlertType.WARNING, "Quy tắc Tỷ Lệ: Phải là một số hợp lệ từ 0 đến 100.");
                    return;
                }
                try {
                    tyLe = new BigDecimal(truongTyLe.getText().trim());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.WARNING, "Quy tắc Tỷ Lệ: Phải là một số hợp lệ từ 0 đến 100.");
                    return;
                }
                break;

            case "Giảm giá theo giá trị":
                if (truongSoTien.getText().trim().isEmpty() || !truongSoTien.getText().trim().matches(SO_TIEN_REGEX)) {
                    showAlert(Alert.AlertType.WARNING, "Quy tắc Số Tiền: Phải là một số hợp lệ và lớn hơn 0.");
                    return;
                }
                try {
                    soTien = new BigDecimal(truongSoTien.getText().trim());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.WARNING, "Quy tắc Số Tiền: Phải là một số hợp lệ và lớn hơn 0.");
                    return;
                }
                break;

            case "Tặng món ăn":
                // Không cần kiểm tra gì thêm vì các ô đã bị vô hiệu hóa
                break;
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
