package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.util.List;

public class KhuyenMaiDialog extends Stage {

    private KhuyenMai ketQua = null;
    private final boolean isEditMode;
    private final KhuyenMai khuyenMaiHienTai;

    // UI Components
    private final ComboBox<LoaiKhuyenMai> loaiKMComboBox = new ComboBox<>();
    private final TextArea moTaTextArea = new TextArea();
    private final DatePicker ngayBatDauPicker = new DatePicker();
    private final DatePicker ngayKetThucPicker = new DatePicker();

    // Dynamic fields
    private final TextField tyLeField = new TextField();
    private final TextField soTienField = new TextField();
    private final ComboBox<MonAn> monTangComboBox = new ComboBox<>();

    private final GridPane dynamicFieldsPane = new GridPane();

    public KhuyenMaiDialog(KhuyenMai km, List<LoaiKhuyenMai> tatCaLoaiKM, List<MonAn> tatCaMonAn) {
        this.khuyenMaiHienTai = km;
        this.isEditMode = (km != null);

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(isEditMode ? "Tùy Chỉnh Khuyến Mãi" : "Thêm Khuyến Mãi Mới");

        // --- Font Loading ---
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
        }

        BorderPane layoutChinh = new BorderPane();

        // ===== HEADER =====
        Label nhanTieuDe = new Label(isEditMode ? "Tùy chỉnh khuyến mãi" : "Thêm khuyến mãi mới");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        // ===== BODY =====
        VBox hopGiua = createMainForm(tatCaLoaiKM, tatCaMonAn, kieuFontStyle);

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

        // ===== SCENE =====
        Scene khungCanh = new Scene(layoutChinh, 650, 500);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
    }

    private VBox createMainForm(List<LoaiKhuyenMai> tatCaLoaiKM, List<MonAn> tatCaMonAn, String kieuFontStyle) {
        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(12);
        mainGrid.setHgap(15);

        // --- Column Constraints for Labels ---
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(350); // Set preferred width for labels
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        mainGrid.getColumnConstraints().addAll(col1, col2);

        String kieuNhan = kieuFontStyle + "-fx-text-fill: #E19E11; -fx-font-size: 14px;";
        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        // Apply styles to components
        loaiKMComboBox.setStyle(kieuTruongNhap); loaiKMComboBox.getStyleClass().add("combo-box");
        moTaTextArea.setStyle(kieuTruongNhap); moTaTextArea.getStyleClass().add("text-area");
        ngayBatDauPicker.setStyle(kieuTruongNhap); ngayBatDauPicker.getStyleClass().add("date-picker");
        ngayKetThucPicker.setStyle(kieuTruongNhap); ngayKetThucPicker.getStyleClass().add("date-picker");
        tyLeField.setStyle(kieuTruongNhap); tyLeField.getStyleClass().add("text-field");
        soTienField.setStyle(kieuTruongNhap); soTienField.getStyleClass().add("text-field");
        monTangComboBox.setStyle(kieuTruongNhap); monTangComboBox.getStyleClass().add("combo-box");

        // Loại khuyến mãi
        Label lblLoaiKM = new Label("Loại khuyến mãi:");
        lblLoaiKM.setStyle(kieuNhan);
        mainGrid.add(lblLoaiKM, 0, 0);
        loaiKMComboBox.setItems(FXCollections.observableArrayList(tatCaLoaiKM));
        mainGrid.add(loaiKMComboBox, 1, 0);

        // Dynamic fields pane
        dynamicFieldsPane.setVgap(12);
        dynamicFieldsPane.setHgap(15);
        dynamicFieldsPane.getColumnConstraints().addAll(new ColumnConstraints(200), col2); // Apply constraints here too
        mainGrid.add(dynamicFieldsPane, 0, 1, 2, 1); // Span 2 columns

        // Mô tả
        Label lblMoTa = new Label("Mô tả chi tiết:");
        lblMoTa.setStyle(kieuNhan);
        mainGrid.add(lblMoTa, 0, 2);
        moTaTextArea.setPromptText("Mô tả chương trình khuyến mãi");
        moTaTextArea.setPrefRowCount(3);
        mainGrid.add(moTaTextArea, 1, 2);

        // Ngày bắt đầu
        Label lblNgayBD = new Label("Bắt đầu từ:");
        lblNgayBD.setStyle(kieuNhan);
        mainGrid.add(lblNgayBD, 0, 3);
        mainGrid.add(ngayBatDauPicker, 1, 3);

        // Ngày kết thúc
        Label lblNgayKT = new Label("Kết thúc vào:");
        lblNgayKT.setStyle(kieuNhan);
        mainGrid.add(lblNgayKT, 0, 4);
        mainGrid.add(ngayKetThucPicker, 1, 4);

        // Listener to change UI based on LoaiKM
        loaiKMComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateDynamicFields(newVal, tatCaMonAn, kieuNhan);
            }
        });

        // Set initial value if not in edit mode
        if (!isEditMode && !tatCaLoaiKM.isEmpty()) {
            loaiKMComboBox.setValue(tatCaLoaiKM.get(0));
        }

        VBox container = new VBox(20, mainGrid);
        container.setPadding(new Insets(20));
        return container;
    }

    private void updateDynamicFields(LoaiKhuyenMai loaiKM, List<MonAn> tatCaMonAn, String kieuNhan) {
        dynamicFieldsPane.getChildren().clear();
        String maLoaiKM = loaiKM.getMaLoaiKM();

        if (maLoaiKM.equals("LKM00001")) { // Giảm giá theo tỷ lệ
            Label label = new Label("Tỷ lệ giảm (%):");
            label.setStyle(kieuNhan);
            dynamicFieldsPane.add(label, 0, 0);
            tyLeField.setPromptText("Ví dụ: 10.5");
            dynamicFieldsPane.add(tyLeField, 1, 0);
        } else if (maLoaiKM.equals("LKM00002")) { // Tặng món
            Label label = new Label("Chọn món tặng:");
            label.setStyle(kieuNhan);
            dynamicFieldsPane.add(label, 0, 0);
            monTangComboBox.setItems(FXCollections.observableArrayList(tatCaMonAn));
            monTangComboBox.setConverter(new StringConverter<MonAn>() {
                @Override
                public String toString(MonAn monAn) {
                    return monAn == null ? "" : monAn.getTenMon();
                }
                @Override
                public MonAn fromString(String s) { return null; }
            });
            dynamicFieldsPane.add(monTangComboBox, 1, 0);
        }
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
        loaiKMComboBox.getItems().stream()
                .filter(lkm -> lkm.getMaLoaiKM().equals(khuyenMaiHienTai.getMaLoaiKM()))
                .findFirst().ifPresent(loaiKMComboBox::setValue);

        moTaTextArea.setText(khuyenMaiHienTai.getMoTa());
        ngayBatDauPicker.setValue(khuyenMaiHienTai.getNgayBatDau());
        ngayKetThucPicker.setValue(khuyenMaiHienTai.getNgayKetThuc());

        if (khuyenMaiHienTai.getTyLe() != null) {
            tyLeField.setText(khuyenMaiHienTai.getTyLe().toPlainString());
        }
        if (khuyenMaiHienTai.getSoTien() != null) {
            soTienField.setText(khuyenMaiHienTai.getSoTien().toPlainString());
        }
        if (khuyenMaiHienTai.getMaMonTang() != null) {
            monTangComboBox.getItems().stream()
                    .filter(m -> m.getMaMonAn().equals(khuyenMaiHienTai.getMaMonTang()))
                    .findFirst().ifPresent(monTangComboBox::setValue);
        }
    }

    private void luuThayDoi() {
        LoaiKhuyenMai selectedLoai = loaiKMComboBox.getValue();
        if (selectedLoai == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn loại khuyến mãi!").showAndWait();
            return;
        }

        if (isEditMode) {
            ketQua = this.khuyenMaiHienTai;
        } else {
            ketQua = new KhuyenMai();
        }

        ketQua.setMaLoaiKM(selectedLoai.getMaLoaiKM());
        ketQua.setMoTa(moTaTextArea.getText());
        ketQua.setNgayBatDau(ngayBatDauPicker.getValue());
        ketQua.setNgayKetThuc(ngayKetThucPicker.getValue());

        // Clear old values
        ketQua.setTyLe(null);
        ketQua.setSoTien(null);
        ketQua.setMaMonTang(null);

        // Set new values based on type
        String maLoaiKM = selectedLoai.getMaLoaiKM();
        try {
            if (maLoaiKM.equals("LKM00001")) { // Giảm giá theo tỷ lệ
                if (!tyLeField.getText().isEmpty()) {
                    ketQua.setTyLe(new BigDecimal(tyLeField.getText()));
                }
            } else if (maLoaiKM.equals("LKM00002")) { // Tặng món
                if (monTangComboBox.getValue() != null) {
                    ketQua.setMaMonTang(monTangComboBox.getValue().getMaMonAn());
                }
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Giá trị nhập vào không hợp lệ (ví dụ: tỷ lệ phải là số).").showAndWait();
            return;
        }

        this.close();
    }

    public KhuyenMai layKetQua() {
        return ketQua;
    }
}
