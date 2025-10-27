package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.MonAn;
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
import java.util.List;
import java.util.Objects;

public class ChiTietKhuyenMaiDialog extends Stage {

    private ChiTietKhuyenMai ketQua = null;
    private final boolean isEditMode;
    private final ChiTietKhuyenMai chiTietHienTai;
    private final KhuyenMai khuyenMaiCha; // Parent KhuyenMai

    // UI Components
    private final TextField truongMaCTKM = new TextField();
    private final ComboBox<MonAn> monApDungComboBox = new ComboBox<>();
    private final ComboBox<MonAn> monTangComboBox = new ComboBox<>();
    private final TextField truongTyLeGiam = new TextField();
    private final TextField truongSoTienGiam = new TextField();
    private final TextField truongSoLuongTang = new TextField();

    public ChiTietKhuyenMaiDialog(ChiTietKhuyenMai chiTiet, KhuyenMai khuyenMaiCha, List<MonAn> tatCaMonAn, String maCTKMMoi) {
        this.chiTietHienTai = chiTiet;
        this.khuyenMaiCha = khuyenMaiCha;
        this.isEditMode = (chiTiet != null);

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(isEditMode ? "Tùy Chỉnh Chi tiết KM" : "Thêm Chi tiết KM Mới");

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

        Label nhanTieuDe = new Label(isEditMode ? "Tùy chỉnh chi tiết khuyến mãi" : "Thêm chi tiết khuyến mãi");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        GridPane luoiFormChinh = createMainForm(tatCaMonAn, kieuFontStyle, maCTKMMoi);
        VBox hopGiua = new VBox(20, luoiFormChinh);
        hopGiua.setPadding(new Insets(20));

        HBox hopChanTrang = createFooter();

        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(hopGiua);
        layoutChinh.setBottom(hopChanTrang);

        if (isEditMode) {
            dienDuLieuHienCo();
        }

        Scene khungCanh = new Scene(layoutChinh, 500, 500);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        this.setHeight(500); // Chiều cao cố định
    }

    private GridPane createMainForm(List<MonAn> tatCaMonAn, String kieuFontStyle, String maCTKMMoi) {
        GridPane luoiForm = new GridPane();
        luoiForm.setVgap(12);
        luoiForm.setHgap(15);

        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        truongMaCTKM.setStyle(kieuTruongNhap); truongMaCTKM.getStyleClass().add("text-field");
        truongMaCTKM.setEditable(false);
        truongMaCTKM.setText(isEditMode ? chiTietHienTai.getMaCTKM() : maCTKMMoi);

        monApDungComboBox.setStyle(kieuTruongNhap); monApDungComboBox.getStyleClass().add("combo-box");
        monTangComboBox.setStyle(kieuTruongNhap); monTangComboBox.getStyleClass().add("combo-box");
        truongTyLeGiam.setStyle(kieuTruongNhap); truongTyLeGiam.getStyleClass().add("text-field");
        truongSoTienGiam.setStyle(kieuTruongNhap); truongSoTienGiam.getStyleClass().add("text-field");
        truongSoLuongTang.setStyle(kieuTruongNhap); truongSoLuongTang.getStyleClass().add("text-field");

        luoiForm.add(new Label("Mã CTKM:"), 0, 0);
        luoiForm.add(truongMaCTKM, 1, 0);

        luoiForm.add(new Label("Món áp dụng:"), 0, 1);
        monApDungComboBox.setItems(FXCollections.observableArrayList(tatCaMonAn));
        monApDungComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(MonAn object) { return object == null ? "" : object.getTenMon(); }
            @Override public MonAn fromString(String string) { return null; }
        });
        luoiForm.add(monApDungComboBox, 1, 1);

        luoiForm.add(new Label("Món tặng:"), 0, 2);
        ObservableList<MonAn> monTangItems = FXCollections.observableArrayList(tatCaMonAn);
        monTangItems.add(0, null); // Add null for "None" option
        monTangComboBox.setItems(monTangItems);
        monTangComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(MonAn object) { return object == null ? "Không có" : object.getTenMon(); }
            @Override public MonAn fromString(String string) { return null; }
        });
        luoiForm.add(monTangComboBox, 1, 2);

        luoiForm.add(new Label("Tỷ lệ giảm (%):"), 0, 3);
        luoiForm.add(truongTyLeGiam, 1, 3);

        luoiForm.add(new Label("Số tiền giảm:"), 0, 4);
        luoiForm.add(truongSoTienGiam, 1, 4);

        luoiForm.add(new Label("Số lượng tặng:"), 0, 5);
        luoiForm.add(truongSoLuongTang, 1, 5);

        return luoiForm;
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
        monApDungComboBox.setValue(chiTietHienTai.getMonApDung());
        monTangComboBox.setValue(chiTietHienTai.getMonTang());
        if (chiTietHienTai.getTyLeGiam() != null) {
            truongTyLeGiam.setText(chiTietHienTai.getTyLeGiam().stripTrailingZeros().toPlainString());
        }
        if (chiTietHienTai.getSoTienGiam() != null) {
            truongSoTienGiam.setText(chiTietHienTai.getSoTienGiam().stripTrailingZeros().toPlainString());
        }
        if (chiTietHienTai.getSoLuongTang() != null) {
            truongSoLuongTang.setText(String.valueOf(chiTietHienTai.getSoLuongTang()));
        }
    }

    private void luuThayDoi() {
        if (monApDungComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn Món áp dụng!");
            return;
        }

        BigDecimal tyLeGiam = null;
        if (!truongTyLeGiam.getText().trim().isEmpty()) {
            try {
                tyLeGiam = new BigDecimal(truongTyLeGiam.getText().trim());
                if (tyLeGiam.compareTo(BigDecimal.ZERO) < 0 || tyLeGiam.compareTo(new BigDecimal(100)) > 0) {
                    showAlert(Alert.AlertType.WARNING, "Tỷ lệ giảm phải là số từ 0 đến 100!");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Tỷ lệ giảm phải là một con số hợp lệ!");
                return;
            }
        }

        BigDecimal soTienGiam = null;
        if (!truongSoTienGiam.getText().trim().isEmpty()) {
            try {
                soTienGiam = new BigDecimal(truongSoTienGiam.getText().trim());
                if (soTienGiam.compareTo(BigDecimal.ZERO) < 0) {
                    showAlert(Alert.AlertType.WARNING, "Số tiền giảm không được là số âm!");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Số tiền giảm phải là một con số hợp lệ!");
                return;
            }
        }

        Integer soLuongTang = null;
        if (!truongSoLuongTang.getText().trim().isEmpty()) {
            try {
                soLuongTang = Integer.parseInt(truongSoLuongTang.getText().trim());
                if (soLuongTang < 0) {
                    showAlert(Alert.AlertType.WARNING, "Số lượng tặng không được là số âm!");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Số lượng tặng phải là một số nguyên hợp lệ!");
                return;
            }
        }

        if (tyLeGiam != null && soTienGiam != null) {
            showAlert(Alert.AlertType.WARNING, "Không thể nhập cả Tỷ lệ giảm và Số tiền giảm cùng lúc!");
            return;
        }
        if (tyLeGiam == null && soTienGiam == null && soLuongTang == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập Tỷ lệ giảm, Số tiền giảm hoặc Số lượng tặng!");
            return;
        }

        if (isEditMode) {
            ketQua = this.chiTietHienTai;
        } else {
            ketQua = new ChiTietKhuyenMai();
            ketQua.setMaCTKM(truongMaCTKM.getText());
            ketQua.setKhuyenMai(khuyenMaiCha);
        }

        ketQua.setMonApDung(monApDungComboBox.getValue());
        ketQua.setMonTang(monTangComboBox.getValue());
        ketQua.setTyLeGiam(tyLeGiam);
        ketQua.setSoTienGiam(soTienGiam);
        ketQua.setSoLuongTang(soLuongTang);

        this.close();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(this);
        alert.showAndWait();
    }

    public ChiTietKhuyenMai layKetQua() {
        return ketQua;
    }
}
