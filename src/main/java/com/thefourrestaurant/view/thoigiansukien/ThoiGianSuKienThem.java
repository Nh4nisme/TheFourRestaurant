package com.thefourrestaurant.view.thoigiansukien;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThoiGianSuKienThem extends Stage {

    // Giữ lại biến ketQua để lưu trữ dữ liệu
    private Map<String, Object> ketQua = null;

    public ThoiGianSuKienThem() {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Thêm sự kiện khuyến mãi");

        // --- Font ---
        Font fontMontserrat = null;
        try (InputStream luongFont = getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")) {
            if (luongFont != null) {
                fontMontserrat = Font.loadFont(luongFont, 14);
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

        // --- Kiểu dáng chung ---
        String kieuNhan = kieuFontStyle + "-fx-text-fill: #E19E11; -fx-font-size: 14px;";
        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #E19E11;";
        String kieuNutToggle = kieuFontStyle + "-fx-background-color: #DDB248; -fx-text-fill: #1E424D; -fx-background-radius: 5; -fx-font-size: 13px;";
        String kieuNutToggleSelected = kieuFontStyle + "-fx-background-color: #1E424D; -fx-text-fill: #DDB248; -fx-background-radius: 5; -fx-font-size: 13px;";


        BorderPane layoutChinh = new BorderPane();

        // --- Header ---
        Label nhanTieuDe = new Label("Tùy chỉnh sự kiện khuyến mãi");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox phanDau = new HBox(nhanTieuDe);
        phanDau.setAlignment(Pos.CENTER_LEFT);
        phanDau.setPadding(new Insets(15));
        phanDau.setStyle("-fx-background-color: #1E424D;");


        // ===== PHẦN NỘI DUNG GIỮA =====
        VBox hopGiua = new VBox(15);
        hopGiua.setPadding(new Insets(20));
        hopGiua.setAlignment(Pos.TOP_LEFT);

        // --- 1. Loại sự kiện (Cố định, Tăng giá, Giảm giá) ---
        Label nhanLoaiSuKien = new Label("Loại sự kiện:");
        nhanLoaiSuKien.setStyle(kieuNhan);
        ToggleGroup nhomLoaiKM = new ToggleGroup();
        ToggleButton nutCoDinh = createStyledToggleButton("Cố định", nhomLoaiKM, kieuNutToggle, kieuNutToggleSelected);
        ToggleButton nutTangGia = createStyledToggleButton("Tăng giá", nhomLoaiKM, kieuNutToggle, kieuNutToggleSelected);
        ToggleButton nutGiamGia = createStyledToggleButton("Giảm giá", nhomLoaiKM, kieuNutToggle, kieuNutToggleSelected);
        nutCoDinh.setSelected(true);
        HBox hopLoaiKM = new HBox(10, nutCoDinh, nutTangGia, nutGiamGia);
        hopLoaiKM.setAlignment(Pos.CENTER_LEFT);


        // --- 2. Chọn ngày áp dụng ---
        Label nhanNgayApDung = new Label("Chọn ngày áp dụng:");
        nhanNgayApDung.setStyle(kieuNhan);
        HBox hopNgay = new HBox(8);
        String[] cacNgay = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "CN"};
        for (String ngay : cacNgay) {
            ToggleButton nutNgay = createStyledToggleButton(ngay, null, kieuNutToggle, kieuNutToggleSelected);
            hopNgay.getChildren().add(nutNgay);
        }


        // --- 3. Chọn giờ áp dụng ---
        Label nhanGioApDung = new Label("Chọn giờ áp dụng:");
        nhanGioApDung.setStyle(kieuNhan);
        Label nhanTuGio = new Label("Từ:");
        nhanTuGio.setStyle(kieuNhan);
        TextField truongTuGio = new TextField("09:00");
        truongTuGio.setStyle(kieuTruongNhap);
        truongTuGio.getStyleClass().add("text-field");
        truongTuGio.setPrefWidth(80);
        Label nhanDenGio = new Label("Đến:");
        nhanDenGio.setStyle(kieuNhan);
        TextField truongDenGio = new TextField("11:00");
        truongDenGio.setStyle(kieuTruongNhap);
        truongDenGio.getStyleClass().add("text-field");
        truongDenGio.setPrefWidth(80);
        HBox hopGio = new HBox(10, nhanTuGio, truongTuGio, nhanDenGio, truongDenGio);
        hopGio.setAlignment(Pos.CENTER_LEFT);


        // --- 4. Chọn thời hạn ---
        Label nhanThoiHan = new Label("Chọn thời hạn:");
        nhanThoiHan.setStyle(kieuNhan);
        Label nhanTuNgay = new Label("Từ:");
        nhanTuNgay.setStyle(kieuNhan);
        DatePicker pickerTuNgay = new DatePicker(LocalDate.of(2025, 4, 10));
        pickerTuNgay.setPrefWidth(120);
        pickerTuNgay.getStyleClass().add("text-field");
        Label nhanDenNgay = new Label("Đến:");
        nhanDenNgay.setStyle(kieuNhan);
        DatePicker pickerDenNgay = new DatePicker(LocalDate.of(2025, 4, 10));
        pickerDenNgay.setPrefWidth(120);
        pickerDenNgay.getStyleClass().add("text-field");
        CheckBox checkVoThoiHan = new CheckBox("Vô thời hạn");
        checkVoThoiHan.setStyle(kieuNhan);
        checkVoThoiHan.getStyleClass().add("custom-checkbox");
        HBox hopThoiHan = new HBox(10, nhanTuNgay, pickerTuNgay, nhanDenNgay, pickerDenNgay, checkVoThoiHan);
        hopThoiHan.setAlignment(Pos.CENTER_LEFT);


        // --- 5. Chọn món ---
        Hyperlink lienKetChonLoaiMon = new Hyperlink("+ Chọn loại món");
        lienKetChonLoaiMon.setStyle(kieuFontStyle + "-fx-text-fill: #E19E11;");
        Hyperlink lienKetChonMon = new Hyperlink("+ Chọn món");
        lienKetChonMon.setStyle(kieuFontStyle + "-fx-text-fill: #E19E11;");
        VBox hopChonMon = new VBox(5, lienKetChonLoaiMon, lienKetChonMon);

        hopGiua.getChildren().addAll(nhanLoaiSuKien, hopLoaiKM, nhanNgayApDung, hopNgay, nhanGioApDung, hopGio, nhanThoiHan, hopThoiHan, hopChonMon);

        // --- Footer ---
        HBox phanCuoi = new HBox(10);
        phanCuoi.setPadding(new Insets(10));
        phanCuoi.setAlignment(Pos.CENTER_LEFT);
        phanCuoi.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");
        phanCuoi.setStyle("-fx-background-color: #1E424D;");

        Button nutXoa = new ButtonSample("Xóa", 35, 14, 3);
        Button nutLuu = new ButtonSample("Lưu", 35, 14, 3);
        Region vungDem = new Region();
        HBox.setHgrow(vungDem, Priority.ALWAYS);
        Button nutOK = new ButtonSample("OK", 35, 14, 3);
        Button nutHuy = new ButtonSample("Hủy", 35, 14, 3);
        phanCuoi.getChildren().addAll(nutXoa, nutLuu, vungDem, nutOK, nutHuy);


        // --- Layout tổng ---
        VBox noiDungChinh = new VBox();
        noiDungChinh.getChildren().addAll(phanDau, hopGiua);
        layoutChinh.setCenter(noiDungChinh);
        layoutChinh.setBottom(phanCuoi);


        // ===== SỰ KIỆN =====
        nutHuy.setOnAction(e -> this.close());

        // Cập nhật sự kiện cho nút OK để thu thập dữ liệu
        nutOK.setOnAction(e -> {
            ketQua = new HashMap<>();

            // 1. Lấy loại khuyến mãi
            ToggleButton loaiKMChon = (ToggleButton) nhomLoaiKM.getSelectedToggle();
            if (loaiKMChon != null) {
                ketQua.put("loaiKhuyenMai", loaiKMChon.getText());
            }

            // 2. Lấy các ngày được chọn
            List<String> ngayDaChon = new ArrayList<>();
            for (Node nut : hopNgay.getChildren()) {
                if (nut instanceof ToggleButton && ((ToggleButton) nut).isSelected()) {
                    ngayDaChon.add(((ToggleButton) nut).getText());
                }
            }
            ketQua.put("ngayApDung", ngayDaChon);

            // 3. Lấy giờ áp dụng
            ketQua.put("tuGio", truongTuGio.getText());
            ketQua.put("denGio", truongDenGio.getText());

            // 4. Lấy thời hạn
            ketQua.put("voThoiHan", checkVoThoiHan.isSelected());
            if (!checkVoThoiHan.isSelected()) {
                ketQua.put("tuNgay", pickerTuNgay.getValue());
                ketQua.put("denNgay", pickerDenNgay.getValue());
            }

            // Có thể thêm logic lấy dữ liệu cho "chọn món" ở đây nếu cần

            this.close(); // Đóng cửa sổ sau khi lấy dữ liệu
        });

        checkVoThoiHan.setOnAction(e -> {
            boolean isChecked = checkVoThoiHan.isSelected();
            pickerTuNgay.setDisable(isChecked);
            pickerDenNgay.setDisable(isChecked);
        });

        // ===== KHUNG CẢNH =====
        Scene khungCanh = new Scene(layoutChinh, 680, 520);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }
        this.setScene(khungCanh);
    }

    private ToggleButton createStyledToggleButton(String text, ToggleGroup group, String defaultStyle, String selectedStyle) {
        ToggleButton button = new ToggleButton(text);
        button.setStyle(defaultStyle);
        if (group != null) {
            button.setToggleGroup(group);
        }

        button.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                button.setStyle(selectedStyle);
            } else {
                button.setStyle(defaultStyle);
            }
        });
        return button;
    }

    public Map<String, Object> layKetQua() {
        return ketQua;
    }
}
