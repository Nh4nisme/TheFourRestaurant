package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class MonAnDialog extends Stage {

    private MonAn ketQua = null;
    private File tepAnhDaChon = null;
    private final boolean isEditMode;
    private final MonAn monAnHienTai;

    // UI Components that map to the database
    private final TextField truongTen = new TextField();
    private final TextField truongGia = new TextField();
    private final ComboBox<LoaiMon> loaiMonComboBox = new ComboBox<>();
    private final CheckBox hopKiemHienThi = new CheckBox();

    // UI Components that are for display only (as requested)
    private final TextField truongVAT = new TextField();
    private final TextField truongMoTa = new TextField();
    private final TextField truongSoLuong = new TextField();

    public MonAnDialog(MonAn monAn, List<LoaiMon> tatCaLoaiMon, String maLoaiMonMacDinh) {
        this.monAnHienTai = monAn;
        this.isEditMode = (monAn != null);

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(isEditMode ? "Tùy Chỉnh Món Ăn" : "Thêm Món Ăn Mới");

        // --- Font Loading (RESTORED) ---
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

        BorderPane layoutChinh = new BorderPane();

        // ===== HEADER =====
        Label nhanTieuDe = new Label(isEditMode ? "Tùy chỉnh món ăn" : "Thêm món ăn");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        // ===== BODY =====
        GridPane luoiFormChinh = createMainForm(tatCaLoaiMon, maLoaiMonMacDinh, kieuFontStyle);
        TitledPane khungNangCao = createAdvancedPane(kieuFontStyle);
        VBox hopGiua = new VBox(20, luoiFormChinh, khungNangCao);
        hopGiua.setPadding(new Insets(20));

        // ===== FOOTER =====
        HBox hopChanTrang = createFooter();

        // ===== LAYOUT =====
        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(hopGiua);
        layoutChinh.setBottom(hopChanTrang);

        // --- Fill data for edit mode ---
        if (isEditMode) {
            dienDuLieuHienCo();
        }

        // ===== ADDING THE MISSING LISTENER =====
        khungNangCao.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                this.setHeight(680); // Expanded height
            } else {
                this.setHeight(500); // Collapsed height
            }
        });

        // ===== SCENE =====
        Scene khungCanh = new Scene(layoutChinh, 500, 550);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        this.setHeight(500); // Set initial height
    }

    private GridPane createMainForm(List<LoaiMon> tatCaLoaiMon, String maLoaiMonMacDinh, String kieuFontStyle) {
        GridPane luoiForm = new GridPane();
        luoiForm.setVgap(12);
        luoiForm.setHgap(15);

        String kieuNhan = kieuFontStyle + "-fx-text-fill: #E19E11; -fx-font-size: 14px;";
        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        // Apply styles
        truongTen.setStyle(kieuTruongNhap);
        truongTen.getStyleClass().add("text-field");
        truongGia.setStyle(kieuTruongNhap);
        truongGia.getStyleClass().add("text-field");
        loaiMonComboBox.setStyle(kieuTruongNhap);
        loaiMonComboBox.getStyleClass().add("combo-box");
        hopKiemHienThi.getStyleClass().add("custom-checkbox");

        // Tên
        Label nhanTen = new Label("Tên:");
        nhanTen.setStyle(kieuNhan);
        luoiForm.add(nhanTen, 0, 0);
        luoiForm.add(truongTen, 1, 0);

        // Mã Món Ăn (Display only)
        Label nhanMa = new Label("Mã Món Ăn:");
        nhanMa.setStyle(kieuNhan);
        TextField truongMa = new TextField(isEditMode ? monAnHienTai.getMaMonAn() : "Tạo tự động");
        truongMa.setEditable(false);
        truongMa.setStyle(kieuTruongNhap);
        truongMa.getStyleClass().add("text-field");
        luoiForm.add(nhanMa, 0, 1);
        luoiForm.add(truongMa, 1, 1);

        // Giá
        Label nhanGia = new Label("Giá:");
        nhanGia.setStyle(kieuNhan);
        luoiForm.add(nhanGia, 0, 2);
        luoiForm.add(truongGia, 1, 2);

        // Loại Món Ăn (NEW)
        Label nhanLoaiMon = new Label("Loại món:");
        nhanLoaiMon.setStyle(kieuNhan);
        luoiForm.add(nhanLoaiMon, 0, 3);
        loaiMonComboBox.setItems(FXCollections.observableArrayList(tatCaLoaiMon));
        loaiMonComboBox.setConverter(new StringConverter<LoaiMon>() {
            @Override
            public String toString(LoaiMon object) {
                return object == null ? "" : object.getTenLoaiMon();
            }
            @Override
            public LoaiMon fromString(String string) { return null; }
        });
        tatCaLoaiMon.stream()
                .filter(lm -> lm.getMaLoaiMon().equals(maLoaiMonMacDinh))
                .findFirst()
                .ifPresent(loaiMonComboBox::setValue);
        luoiForm.add(loaiMonComboBox, 1, 3);

        // Hình ảnh
        Label nhanHinhAnh = new Label("Hình ảnh:");
        nhanHinhAnh.setStyle(kieuNhan);
        Hyperlink lienKetDinhKemAnh = new Hyperlink("đính kèm một ảnh");
        lienKetDinhKemAnh.setOnAction(e -> chonAnh());
        luoiForm.add(nhanHinhAnh, 0, 4);
        luoiForm.add(lienKetDinhKemAnh, 1, 4);

        // Hiển thị (Trạng thái)
        Label nhanHienThi = new Label("Hiển thị:");
        nhanHienThi.setStyle(kieuNhan);
        hopKiemHienThi.setSelected(true);
        luoiForm.add(nhanHienThi, 0, 5);
        luoiForm.add(hopKiemHienThi, 1, 5);

        return luoiForm;
    }

    private TitledPane createAdvancedPane(String kieuFontStyle) {
        TitledPane khungNangCao = new TitledPane();
        khungNangCao.setText("Tùy chỉnh nâng cao (chỉ hiển thị, không lưu)");
        khungNangCao.setCollapsible(true);
        khungNangCao.setExpanded(false);

        GridPane luoiNangCao = new GridPane();
        luoiNangCao.setVgap(10);
        luoiNangCao.setHgap(15);
        luoiNangCao.setPadding(new Insets(20));

        String kieuNhan = kieuFontStyle + "-fx-text-fill: #E19E11; -fx-font-size: 14px;";
        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        truongVAT.setStyle(kieuTruongNhap);
        truongVAT.getStyleClass().add("text-field");
        truongMoTa.setStyle(kieuTruongNhap);
        truongMoTa.getStyleClass().add("text-field");
        truongSoLuong.setStyle(kieuTruongNhap);
        truongSoLuong.getStyleClass().add("text-field");

        Label nhanVAT = new Label("VAT:");
        nhanVAT.setStyle(kieuNhan);
        luoiNangCao.add(nhanVAT, 0, 0);
        luoiNangCao.add(truongVAT, 1, 0);

        Label nhanMoTa = new Label("Mô tả:");
        nhanMoTa.setStyle(kieuNhan);
        luoiNangCao.add(nhanMoTa, 0, 1);
        luoiNangCao.add(truongMoTa, 1, 1);

        Label nhanKhuyenMai = new Label("Khuyến mãi:");
        nhanKhuyenMai.setStyle(kieuNhan);
        luoiNangCao.add(nhanKhuyenMai, 0, 2);
        luoiNangCao.add(new Hyperlink("+ Chọn khuyến mãi"), 1, 2);

        Label nhanSoLuong = new Label("Số lượng:");
        nhanSoLuong.setStyle(kieuNhan);
        luoiNangCao.add(nhanSoLuong, 0, 3);
        luoiNangCao.add(truongSoLuong, 1, 3);

        khungNangCao.setContent(luoiNangCao);
        return khungNangCao;
    }

    private HBox createFooter() {
        HBox hopChanTrang = new HBox(10);
        hopChanTrang.setPadding(new Insets(15));
        hopChanTrang.setAlignment(Pos.CENTER_RIGHT);
        hopChanTrang.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        Button nutLuu = new ButtonSample("Lưu", 35, 14, 2);
        Button nutHuy = new ButtonSample("Hủy", 35, 14, 2);

        nutHuy.setOnAction(e -> this.close());
        nutLuu.setOnAction(e -> luuThayDoi());

        hopChanTrang.getChildren().addAll(nutLuu, nutHuy);
        return hopChanTrang;
    }

    private void dienDuLieuHienCo() {
        truongTen.setText(monAnHienTai.getTenMon());
        truongGia.setText(monAnHienTai.getDonGia().toPlainString());
        hopKiemHienThi.setSelected(monAnHienTai.getTrangThai().equalsIgnoreCase("Con"));

        Optional<LoaiMon> selectedLoaiMon = loaiMonComboBox.getItems().stream()
                .filter(lm -> lm.getMaLoaiMon().equals(monAnHienTai.getMaLoaiMon()))
                .findFirst();
        selectedLoaiMon.ifPresent(loaiMonComboBox::setValue);
    }

    private void chonAnh() {
        FileChooser boChonTep = new FileChooser();
        boChonTep.setTitle("Chọn Ảnh Món Ăn");
        boChonTep.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File tep = boChonTep.showOpenDialog(this);
        if (tep != null) {
            tepAnhDaChon = tep;
        }
    }

    private void luuThayDoi() {
        if (truongTen.getText().trim().isEmpty() || truongGia.getText().trim().isEmpty() || loaiMonComboBox.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập Tên, Giá và chọn Loại món ăn!").showAndWait();
            return;
        }

        BigDecimal donGia;
        try {
            donGia = new BigDecimal(truongGia.getText().trim());
            if (donGia.compareTo(BigDecimal.ZERO) < 0) {
                new Alert(Alert.AlertType.WARNING, "Đơn giá không được là số âm!").showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Đơn giá phải là một con số hợp lệ!").showAndWait();
            return;
        }

        if (isEditMode) {
            ketQua = this.monAnHienTai;
        } else {
            ketQua = new MonAn();
        }

        ketQua.setTenMon(truongTen.getText().trim());
        ketQua.setDonGia(donGia);
        ketQua.setTrangThai(hopKiemHienThi.isSelected() ? "Con" : "Het");
        ketQua.setMaLoaiMon(loaiMonComboBox.getValue().getMaLoaiMon());

        if (tepAnhDaChon != null) {
            ketQua.setHinhAnh(tepAnhDaChon.toURI().toString());
        } else if (!isEditMode) {
            ketQua.setHinhAnh(null);
        }

        this.close();
    }

    public MonAn layKetQua() {
        return ketQua;
    }
}
