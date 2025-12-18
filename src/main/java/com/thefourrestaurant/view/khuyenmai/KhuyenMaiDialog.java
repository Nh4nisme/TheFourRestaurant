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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class KhuyenMaiDialog extends Stage {

    private static final String TEN_KM_REGEX = "^[a-zA-Z0-9]{1,100}$";
    private static final String MO_TA_REGEX = "^.{1,255}$";
    private static final String MA_CODE_REGEX = "^[A-Z0-9]{3,50}$";
    private static final String SO_LUOT_REGEX = "^[1-9]\\d*$";

    private KhuyenMai ketQua = null;
    private final boolean laCheDoChinhSua;
    private final KhuyenMai khuyenMaiHienTai;
    private final KhuyenMaiController boDieuKhien;

    private final TextField truongMaKM = new TextField();
    private final TextField truongTenKM = new TextField();
    private final TextField truongMoTa = new TextField();
    private final ComboBox<LoaiKhuyenMai> hopChonLoaiKhuyenMai = new ComboBox<>();
    private final ComboBox<String> hopChonKieuKM = new ComboBox<>();
    private final TextField truongMaCode = new TextField();
    private final TextField truongSoLuotSuDung = new TextField();
    private final DatePicker boChonNgayBatDau = new DatePicker();
    private final DatePicker boChonNgayKetThuc = new DatePicker();

    private Label nhanMaCode;
    private Label nhanSoLuotSuDung;
    private List<LoaiKhuyenMai> danhSachTatCaLoaiKhuyenMai;

    public KhuyenMaiDialog(KhuyenMai khuyenMai, List<LoaiKhuyenMai> danhSachTatCaLoaiKhuyenMai, String maKhuyenMaiMoi, KhuyenMaiController boDieuKhien) {
        this.khuyenMaiHienTai = khuyenMai;
        this.laCheDoChinhSua = (khuyenMai != null);
        this.boDieuKhien = boDieuKhien;
        this.danhSachTatCaLoaiKhuyenMai = danhSachTatCaLoaiKhuyenMai;

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

        GridPane luoiFormChinh = taoFormChinh(kieuFontStyle, maKhuyenMaiMoi);
        VBox hopGiua = new VBox(20, luoiFormChinh);
        hopGiua.setPadding(new Insets(20));

        HBox hopChanTrang = taoChanTrang();

        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(hopGiua);
        layoutChinh.setBottom(hopChanTrang);

        if (laCheDoChinhSua) {
            dienDuLieuHienCo();
        }

        Scene khungCanh = new Scene(layoutChinh, 550, 550);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
    }

    private GridPane taoFormChinh(String kieuFontStyle, String maKhuyenMaiMoi) {
        GridPane luoiForm = new GridPane();
        luoiForm.setVgap(12);
        luoiForm.setHgap(15);

        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        truongMaKM.setStyle(kieuTruongNhap);
        truongMaKM.setEditable(false);
        truongMaKM.setText(laCheDoChinhSua ? khuyenMaiHienTai.getMaKM() : maKhuyenMaiMoi);

        truongTenKM.setStyle(kieuTruongNhap);
        truongMoTa.setStyle(kieuTruongNhap);

        hopChonKieuKM.setStyle(kieuTruongNhap);
        hopChonKieuKM.setItems(FXCollections.observableArrayList("Sự kiện (Tự động áp)", "Mã giảm giá (Nhập mã)"));
        hopChonKieuKM.setValue("Sự kiện (Tự động áp)");

        truongMaCode.setStyle(kieuTruongNhap);
        truongMaCode.setPromptText("VD: GIAM10K");

        truongSoLuotSuDung.setStyle(kieuTruongNhap);
        truongSoLuotSuDung.setPromptText("Để trống = Không giới hạn");

        hopChonLoaiKhuyenMai.setStyle(kieuTruongNhap);
        boChonNgayBatDau.setStyle(kieuTruongNhap);
        boChonNgayKetThuc.setStyle(kieuTruongNhap);

        int row = 0;
        luoiForm.add(new Label("Mã KM:"), 0, row);
        luoiForm.add(truongMaKM, 1, row++);

        luoiForm.add(new Label("Tên KM:"), 0, row);
        luoiForm.add(truongTenKM, 1, row++);

        luoiForm.add(new Label("Mô tả:"), 0, row);
        luoiForm.add(truongMoTa, 1, row++);

        luoiForm.add(new Label("Kiểu KM:"), 0, row);
        luoiForm.add(hopChonKieuKM, 1, row++);

        nhanMaCode = new Label("Mã Code:");
        luoiForm.add(nhanMaCode, 0, row);
        luoiForm.add(truongMaCode, 1, row++);

        nhanSoLuotSuDung = new Label("Số lượt SD:");
        luoiForm.add(nhanSoLuotSuDung, 0, row);
        luoiForm.add(truongSoLuotSuDung, 1, row++);

        capNhatHienThiTheoKieuKM();
        hopChonKieuKM.setOnAction(e -> capNhatHienThiTheoKieuKM());

        luoiForm.add(new Label("Loại KM:"), 0, row);
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
        luoiForm.add(hopChonLoaiKhuyenMai, 1, row++);

        luoiForm.add(new Label("Ngày BĐ:"), 0, row);
        luoiForm.add(boChonNgayBatDau, 1, row++);

        luoiForm.add(new Label("Ngày KT:"), 0, row);
        luoiForm.add(boChonNgayKetThuc, 1, row++);

        return luoiForm;
    }

    private void capNhatHienThiTheoKieuKM() {
        String kieuChon = hopChonKieuKM.getValue();
        boolean laMaGiamGia = "Mã giảm giá (Nhập mã)".equals(kieuChon);

        truongMaCode.setDisable(!laMaGiamGia);
        truongSoLuotSuDung.setDisable(!laMaGiamGia);
        nhanMaCode.setDisable(!laMaGiamGia);
        nhanSoLuotSuDung.setDisable(!laMaGiamGia);

        if (!laMaGiamGia) {
            truongMaCode.clear();
            truongSoLuotSuDung.clear();
        }
        
        // Lọc danh sách loại khuyến mãi
        if (laMaGiamGia) {
            List<LoaiKhuyenMai> filteredList = danhSachTatCaLoaiKhuyenMai.stream()
                .filter(lkm -> !"Tặng món".equalsIgnoreCase(lkm.getTenLoaiKM()))
                .collect(Collectors.toList());
            hopChonLoaiKhuyenMai.setItems(FXCollections.observableArrayList(filteredList));
        } else {
            hopChonLoaiKhuyenMai.setItems(FXCollections.observableArrayList(danhSachTatCaLoaiKhuyenMai));
        }
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
            ButtonSample nutQuanLyDieuKien = new ButtonSample("Quản lý Điều kiện", 35, 14, 2);
            nutQuanLyDieuKien.setOnAction(e -> {
                DieuKienKhuyenMaiManagerDialog dieuKienDialog = new DieuKienKhuyenMaiManagerDialog(khuyenMaiHienTai, boDieuKhien);
                dieuKienDialog.initOwner(this);
                dieuKienDialog.showAndWait();
            });
            hopChanTrang.getChildren().add(0, nutQuanLyDieuKien);

            ButtonSample nutQuanLyKhungGio = new ButtonSample("Quản lý khung giờ", 35, 14, 2);
            nutQuanLyKhungGio.setOnAction(e -> {
                KhungGioManagerDialog khungGioDialog = new KhungGioManagerDialog(khuyenMaiHienTai.getMaKM());
                khungGioDialog.initOwner(this);
                khungGioDialog.showAndWait();
            });
            hopChanTrang.getChildren().add(1, nutQuanLyKhungGio);
        }

        nutHuy.setOnAction(e -> this.close());
        nutLuu.setOnAction(e -> luuThayDoi());

        return hopChanTrang;
    }

    private void dienDuLieuHienCo() {
        truongTenKM.setText(khuyenMaiHienTai.getTenKM());
        truongMoTa.setText(khuyenMaiHienTai.getMoTa());

        if (KhuyenMai.KIEU_MA_GIAM_GIA.equals(khuyenMaiHienTai.getKieuKM())) {
            hopChonKieuKM.setValue("Mã giảm giá (Nhập mã)");
        } else {
            hopChonKieuKM.setValue("Sự kiện (Tự động áp)");
        }
        capNhatHienThiTheoKieuKM();

        if (khuyenMaiHienTai.getMaCode() != null) {
            truongMaCode.setText(khuyenMaiHienTai.getMaCode());
        }
        if (khuyenMaiHienTai.getSoLuotSuDung() != null) {
            truongSoLuotSuDung.setText(String.valueOf(khuyenMaiHienTai.getSoLuotSuDung()));
        }

        hopChonLoaiKhuyenMai.setValue(khuyenMaiHienTai.getLoaiKhuyenMai());
        
        if (khuyenMaiHienTai.getNgayBatDau() != null) {
            boChonNgayBatDau.setValue(khuyenMaiHienTai.getNgayBatDau().toLocalDate());
        }
        if (khuyenMaiHienTai.getNgayKetThuc() != null) {
            boChonNgayKetThuc.setValue(khuyenMaiHienTai.getNgayKetThuc().toLocalDate());
        }
    }

    private void luuThayDoi() {
        if (!truongTenKM.getText().trim().matches(TEN_KM_REGEX)) {
            showAlert(Alert.AlertType.WARNING, "Tên khuyến mãi phải là một dãy ký tự không dấu, không chứa khoảng trắng và có độ dài từ 1 đến 100 ký tự.");
            return;
        }
        if (!truongMoTa.getText().trim().matches(MO_TA_REGEX)) {
            showAlert(Alert.AlertType.WARNING, "Mô tả không được để trống và phải có độ dài từ 1 đến 255 ký tự.");
            return;
        }
        if (hopChonLoaiKhuyenMai.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn loại khuyến mãi!");
            return;
        }

        String kieuKMChon = hopChonKieuKM.getValue();
        String kieuKM = "Mã giảm giá (Nhập mã)".equals(kieuKMChon) ? KhuyenMai.KIEU_MA_GIAM_GIA : KhuyenMai.KIEU_SU_KIEN;

        String maCode = null;
        Integer soLuotSuDung = null;

        if (KhuyenMai.KIEU_MA_GIAM_GIA.equals(kieuKM)) {
            String maCodeInput = truongMaCode.getText().trim().toUpperCase();
            if (maCodeInput.isEmpty() || !maCodeInput.matches(MA_CODE_REGEX)) {
                showAlert(Alert.AlertType.WARNING, "Mã Code phải từ 3-50 ký tự, chỉ gồm chữ in hoa và số (VD: GIAM10K).");
                return;
            }
            maCode = maCodeInput;

            String soLuotText = truongSoLuotSuDung.getText().trim();
            if (!soLuotText.isEmpty()) {
                if (!soLuotText.matches(SO_LUOT_REGEX)) {
                    showAlert(Alert.AlertType.WARNING, "Số lượt sử dụng phải là số nguyên dương!");
                    return;
                }
                soLuotSuDung = Integer.parseInt(soLuotText);
            }
        }

        LoaiKhuyenMai loaiKM = hopChonLoaiKhuyenMai.getValue();
        
        LocalDate ngayBD_localDate = boChonNgayBatDau.getValue();
        LocalDate ngayKT_localDate = boChonNgayKetThuc.getValue();

        if (ngayBD_localDate != null && ngayKT_localDate != null && ngayKT_localDate.isBefore(ngayBD_localDate)) {
            showAlert(Alert.AlertType.WARNING, "Ngày kết thúc không được trước ngày bắt đầu!");
            return;
        }

        LocalDateTime ngayBD = (ngayBD_localDate != null) ? ngayBD_localDate.atStartOfDay() : null;
        LocalDateTime ngayKT = (ngayKT_localDate != null) ? ngayKT_localDate.atStartOfDay() : null;

        if (laCheDoChinhSua) {
            ketQua = this.khuyenMaiHienTai;
        } else {
            ketQua = new KhuyenMai();
            ketQua.setMaKM(truongMaKM.getText());
        }

        ketQua.setTenKM(truongTenKM.getText().trim());
        ketQua.setMoTa(truongMoTa.getText().trim());
        ketQua.setKieuKM(kieuKM);
        ketQua.setMaCode(maCode);
        ketQua.setSoLuotSuDung(soLuotSuDung);
        ketQua.setLoaiKhuyenMai(loaiKM);
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
