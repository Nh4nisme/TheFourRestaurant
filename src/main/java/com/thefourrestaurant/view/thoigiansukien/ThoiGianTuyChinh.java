package com.thefourrestaurant.view.thoigiansukien;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.InputStream;
import java.net.URL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ThoiGianTuyChinh extends Stage {

    private Map<String, Object> ketQua = null;
    private File tepAnhDaChon = null;

    public ThoiGianTuyChinh() {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Tùy chỉnh thời gian sự kiện");

        // --- Font ---
        Font fontMontserrat = null;
        try (InputStream luongFont = getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")) {
            if (luongFont != null) {
                fontMontserrat = Font.loadFont(luongFont, 12);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải font: " + e.getMessage());
        }

        String kieuFontStyle = "";
        if (fontMontserrat != null) {
            kieuFontStyle = "-fx-font-family: '" + fontMontserrat.getFamily() + "';";
        } else {
            System.err.println("Không tìm thấy font Montserrat-SemiBold.ttf. Sử dụng font mặc định.");
        }

        BorderPane layoutChinh = new BorderPane();

        // ===== PHẦN ĐẦU =====
        Label nhanTieuDe = new Label("Tùy chỉnh thời gian sự kiện");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 16px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(10));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        // ===== BIỂU MẪU =====
        GridPane luoiForm = new GridPane();
        luoiForm.setPadding(new Insets(15));
        luoiForm.setVgap(10);
        luoiForm.setHgap(10);

        String kieuNhan = kieuFontStyle + "-fx-text-fill: #E19E11; -fx-font-size: 12px;";
        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8; -fx-font-size: 12px;";

        Label nhanTen = new Label("Tên:");
        nhanTen.setStyle(kieuNhan);
        TextField truongTen = new TextField();
        truongTen.setStyle(kieuTruongNhap);
        truongTen.getStyleClass().add("text-field");

        Label nhanMa = new Label("Mã Sự Kiện:");
        nhanMa.setStyle(kieuNhan);
        TextField truongMa = new TextField("tạo tự động");
        truongMa.setEditable(false);
        truongMa.setStyle(kieuTruongNhap);
        truongMa.getStyleClass().add("text-field");

        Label nhanGia = new Label("Giá:");
        nhanGia.setStyle(kieuNhan);
        TextField truongGia = new TextField();
        truongGia.setStyle(kieuTruongNhap);
        truongGia.getStyleClass().add("text-field");

        Label nhanHinhAnh = new Label("Hình ảnh:");
        nhanHinhAnh.setStyle(kieuNhan);
        Hyperlink lienKetDinhKemAnh = new Hyperlink("đính kèm một ảnh");
        lienKetDinhKemAnh.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-font-size: 12px;");

        Label nhanHienThi = new Label("Hiển thị:");
        nhanHienThi.setStyle(kieuNhan);
        CheckBox hopKiemHienThi = new CheckBox();
        hopKiemHienThi.getStyleClass().add("custom-checkbox");

        luoiForm.add(nhanTen, 0, 0); luoiForm.add(truongTen, 1, 0);
        luoiForm.add(nhanMa, 0, 1); luoiForm.add(truongMa, 1, 1);
        luoiForm.add(nhanGia, 0, 2); luoiForm.add(truongGia, 1, 2);
        luoiForm.add(nhanHinhAnh, 0, 3); luoiForm.add(lienKetDinhKemAnh, 1, 3);
        luoiForm.add(nhanHienThi, 0, 4); luoiForm.add(hopKiemHienThi, 1, 4);

        // ===== TÙY CHỈNH NÂNG CAO =====
        TitledPane khungNangCao = new TitledPane();
        khungNangCao.setText("Tùy chỉnh nâng cao");
        khungNangCao.setCollapsible(true);
        khungNangCao.setExpanded(false);

        GridPane luoiNangCao = new GridPane();
        luoiNangCao.setVgap(8);
        luoiNangCao.setHgap(10);
        luoiNangCao.setPadding(new Insets(8));

        Label nhanVAT = new Label("VAT:");
        nhanVAT.setStyle(kieuNhan);
        TextField truongVAT = new TextField();
        truongVAT.setStyle(kieuTruongNhap);
        truongVAT.getStyleClass().add("text-field");

        Label nhanMoTa = new Label("Mô tả:");
        nhanMoTa.setStyle(kieuNhan);
        TextField truongMoTa = new TextField();
        truongMoTa.setStyle(kieuTruongNhap);
        truongMoTa.getStyleClass().add("text-field");

        Label nhanKhuyenMai = new Label("Khuyến mãi:");
        nhanKhuyenMai.setStyle(kieuNhan);
        Hyperlink lienKetKhuyenMai = new Hyperlink("+ Chọn khuyến mãi");
        lienKetKhuyenMai.setStyle("-fx-text-fill: blue; -fx-font-size: 12px;");

        Label nhanSoLuong = new Label("Số lượng:");
        nhanSoLuong.setStyle(kieuNhan);
        TextField truongSoLuong = new TextField();
        truongSoLuong.setStyle(kieuTruongNhap);
        truongSoLuong.getStyleClass().add("text-field");

        luoiNangCao.add(nhanVAT, 0, 0); luoiNangCao.add(truongVAT, 1, 0);
        luoiNangCao.add(nhanMoTa, 0, 1); luoiNangCao.add(truongMoTa, 1, 1);
        luoiNangCao.add(nhanKhuyenMai, 0, 2); luoiNangCao.add(lienKetKhuyenMai, 1, 2);
        luoiNangCao.add(nhanSoLuong, 0, 3); luoiNangCao.add(truongSoLuong, 1, 3);

        khungNangCao.setContent(luoiNangCao);

        VBox hopGiua = new VBox(8, luoiForm, khungNangCao);
        hopGiua.setPadding(new Insets(8, 15, 15, 15));

        // ===== PHẦN CUỐI =====
        HBox hopChanTrang = new HBox(8);
        hopChanTrang.setPadding(new Insets(8));
        hopChanTrang.setAlignment(Pos.CENTER_LEFT);
        hopChanTrang.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        Region vungDem = new Region();
        HBox.setHgrow(vungDem, Priority.ALWAYS);
        Button nutOK = new ButtonSample("Oke", 30, 12, 2);
        Button nutHuy = new ButtonSample("Hủy", 30, 12, 2);
        hopChanTrang.getChildren().addAll(vungDem, nutOK, nutHuy);


        // ===== SỰ KIỆN =====
        lienKetDinhKemAnh.setOnAction(e -> {
            FileChooser boChonTep = new FileChooser();
            boChonTep.setTitle("Chọn Ảnh");
            boChonTep.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File tep = boChonTep.showOpenDialog(this);
            if (tep != null) tepAnhDaChon = tep;
        });

        nutHuy.setOnAction(e -> this.close());

        nutOK.setOnAction(e -> {
            if (truongTen.getText().trim().isEmpty() || truongGia.getText().trim().isEmpty()) {
                Alert canhBao = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên và giá sự kiện!", ButtonType.OK);
                canhBao.showAndWait();
            } else {
                ketQua = new HashMap<>();
                ketQua.put("ten", truongTen.getText());
                ketQua.put("gia", truongGia.getText());
                ketQua.put("vat", truongVAT.getText());
                ketQua.put("moTa", truongMoTa.getText());
                ketQua.put("soLuong", truongSoLuong.getText());
                ketQua.put("hienThi", hopKiemHienThi.isSelected());
                if (tepAnhDaChon != null) ketQua.put("imagePath", tepAnhDaChon.toURI().toString());
                this.close();
            }
        });

        khungNangCao.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                this.setHeight(500);
            } else {
                this.setHeight(380);
            }
        });

        // ===== KHUNG CẢNH =====
        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(hopGiua);
        layoutChinh.setBottom(hopChanTrang);

        Scene khungCanh = new Scene(layoutChinh, 450, 380);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }
        this.setScene(khungCanh);
    }

    public Map<String, Object> layKetQua() {
        return ketQua;
    }
}
