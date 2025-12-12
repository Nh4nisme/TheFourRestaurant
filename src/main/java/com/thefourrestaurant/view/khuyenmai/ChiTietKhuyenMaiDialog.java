package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChiTietKhuyenMaiDialog extends Stage {

    // Thay vì trả về list ChiTietKhuyenMai, trả về data
    private Set<MonAn> cacMonApDungKetQua = null;
    private MonAn monTangKetQua = null;
    private BigDecimal tyLeGiamKetQua = null;
    private BigDecimal soTienGiamKetQua = null;
    private Integer soLuongTangKetQua = null;
    private boolean daLuu = false;

    private List<ChiTietKhuyenMai> danhSachKetQua = null;
    private final boolean isEditMode;
    private final ChiTietKhuyenMai chiTietHienTai;
    private final KhuyenMai khuyenMaiCha;
    private final List<MonAn> tatCaMonAn;
    private final String maCTKMGoc;
    private final KhuyenMaiController boDieuKhien;

    // UI Components
    private final TextField truongMaCTKM = new TextField();
    private final TextField truongMonApDung = new TextField();
    private final TextField truongMonTang = new TextField();
    private final TextField truongTyLeGiam = new TextField();
    private final TextField truongSoTienGiam = new TextField();
    private final TextField truongSoLuongTang = new TextField();

    private final Set<MonAn> cacMonApDungDaChon = new HashSet<>();
    private MonAn monTangDaChon = null;

    public ChiTietKhuyenMaiDialog(ChiTietKhuyenMai chiTiet, KhuyenMai khuyenMaiCha, List<MonAn> tatCaMonAn, String maCTKMMoi) {
        this(chiTiet, khuyenMaiCha, tatCaMonAn, maCTKMMoi, null);
    }

    public ChiTietKhuyenMaiDialog(ChiTietKhuyenMai chiTiet, KhuyenMai khuyenMaiCha, List<MonAn> tatCaMonAn, String maCTKMMoi, KhuyenMaiController boDieuKhien) {
        this.chiTietHienTai = chiTiet;
        this.khuyenMaiCha = khuyenMaiCha;
        this.tatCaMonAn = tatCaMonAn;
        this.isEditMode = (chiTiet != null);
        this.maCTKMGoc = maCTKMMoi;
        this.boDieuKhien = boDieuKhien;

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

        Scene khungCanh = new Scene(layoutChinh, 500, 550);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        this.setHeight(550);
    }

    private GridPane createMainForm(List<MonAn> tatCaMonAn, String kieuFontStyle, String maCTKMMoi) {
        GridPane luoiForm = new GridPane();
        luoiForm.setVgap(12);
        luoiForm.setHgap(15);

        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        truongMonApDung.setStyle(kieuTruongNhap); truongMonApDung.getStyleClass().add("text-field");
        truongMonApDung.setEditable(false);
        truongMonApDung.setPromptText("Chọn món áp dụng...");

        truongMonTang.setStyle(kieuTruongNhap); truongMonTang.getStyleClass().add("text-field");
        truongMonTang.setEditable(false);
        truongMonTang.setPromptText("Chọn món tặng...");

        truongTyLeGiam.setStyle(kieuTruongNhap); truongTyLeGiam.getStyleClass().add("text-field");
        truongSoTienGiam.setStyle(kieuTruongNhap); truongSoTienGiam.getStyleClass().add("text-field");
        truongSoLuongTang.setStyle(kieuTruongNhap); truongSoLuongTang.getStyleClass().add("text-field");

        luoiForm.add(new Label("Món áp dụng:"), 0, 0);
        HBox hopMonApDung = new HBox(10);
        HBox.setHgrow(truongMonApDung, Priority.ALWAYS);
        ButtonSample btnChonMonApDung = new ButtonSample("Chọn", 35, 13, 2);
        btnChonMonApDung.setOnAction(e -> moDialogChonMonApDung());
        hopMonApDung.getChildren().addAll(truongMonApDung, btnChonMonApDung);
        luoiForm.add(hopMonApDung, 1, 0);

        luoiForm.add(new Label("Món tặng:"), 0, 1);
        HBox hopMonTang = new HBox(10);
        HBox.setHgrow(truongMonTang, Priority.ALWAYS);
        ButtonSample btnChonMonTang = new ButtonSample("Chọn", 35, 13, 2);
        btnChonMonTang.setOnAction(e -> moDialogChonMonTang());
        hopMonTang.getChildren().addAll(truongMonTang, btnChonMonTang);
        luoiForm.add(hopMonTang, 1, 1);

        luoiForm.add(new Label("Tỷ lệ giảm (%):"), 0, 2);
        luoiForm.add(truongTyLeGiam, 1, 2);

        luoiForm.add(new Label("Số tiền giảm:"), 0, 3);
        luoiForm.add(truongSoTienGiam, 1, 3);

        luoiForm.add(new Label("Số lượng tặng:"), 0, 4);
        luoiForm.add(truongSoLuongTang, 1, 4);

        // Logic to enable/disable fields and auto-fill values from parent promotion
        if (khuyenMaiCha != null && khuyenMaiCha.getLoaiKhuyenMai() != null) {
            String tenLoaiKM = khuyenMaiCha.getLoaiKhuyenMai().getTenLoaiKM();
            switch (tenLoaiKM) {
                case "Giảm giá theo tỷ lệ":
                    truongTyLeGiam.setDisable(false);
                    truongSoTienGiam.setDisable(true);
                    hopMonTang.setDisable(true);
                    truongSoLuongTang.setDisable(true);

                    // Tự động điền trị số từ khuyến mãi cha
                    if (!isEditMode && khuyenMaiCha.getTyLe() != null) {
                        truongTyLeGiam.setText(khuyenMaiCha.getTyLe().stripTrailingZeros().toPlainString());
                    }
                    break;

                case "Giảm giá theo số tiền":
                    truongTyLeGiam.setDisable(true);
                    truongSoTienGiam.setDisable(false);
                    hopMonTang.setDisable(true);
                    truongSoLuongTang.setDisable(true);

                    // Tự động điền trị số từ khuyến mãi cha
                    if (!isEditMode && khuyenMaiCha.getSoTien() != null) {
                        truongSoTienGiam.setText(khuyenMaiCha.getSoTien().stripTrailingZeros().toPlainString());
                    }
                    break;

                case "Tặng món":
                    truongTyLeGiam.setDisable(true);
                    truongSoTienGiam.setDisable(true);
                    hopMonTang.setDisable(false);
                    truongSoLuongTang.setDisable(false);
                    break;

                default:
                    truongTyLeGiam.setDisable(false);
                    truongSoTienGiam.setDisable(false);
                    hopMonTang.setDisable(false);
                    truongSoLuongTang.setDisable(false);
                    break;
            }
        }

        return luoiForm;
    }

    private void moDialogChonMonApDung() {
        ChonMonAnDialog dialog = new ChonMonAnDialog(tatCaMonAn, cacMonApDungDaChon, true);
        dialog.initOwner(this);
        dialog.showAndWait();

        Set<MonAn> ketQua = dialog.getCacMonDaChon();
        if (ketQua != null) {
            cacMonApDungDaChon.clear();
            cacMonApDungDaChon.addAll(ketQua);
            capNhatHienThiMonApDung();
        }
    }

    private void moDialogChonMonTang() {
        Set<MonAn> cacMonHienTai = new HashSet<>();
        if (monTangDaChon != null) {
            cacMonHienTai.add(monTangDaChon);
        }

        ChonMonAnDialog dialog = new ChonMonAnDialog(tatCaMonAn, cacMonHienTai, false);
        dialog.initOwner(this);
        dialog.showAndWait();

        Set<MonAn> ketQua = dialog.getCacMonDaChon();
        if (ketQua != null) {
            if (ketQua.isEmpty()) {
                monTangDaChon = null;
            } else {
                monTangDaChon = ketQua.iterator().next();
            }
            capNhatHienThiMonTang();
        }
    }

    private void capNhatHienThiMonApDung() {
        if (cacMonApDungDaChon.isEmpty()) {
            truongMonApDung.setText("");
        } else {
            String danhSachTen = cacMonApDungDaChon.stream()
                    .map(MonAn::getTenMon)
                    .sorted()
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            truongMonApDung.setText(danhSachTen);
        }
    }

    private void capNhatHienThiMonTang() {
        if (monTangDaChon == null) {
            truongMonTang.setText("");
        } else {
            truongMonTang.setText(monTangDaChon.getTenMon());
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
        if (chiTietHienTai.getMonApDung() != null) {
            cacMonApDungDaChon.add(chiTietHienTai.getMonApDung());
            capNhatHienThiMonApDung();
        }

        if (chiTietHienTai.getMonTang() != null) {
            monTangDaChon = chiTietHienTai.getMonTang();
            capNhatHienThiMonTang();
        }

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
        if (cacMonApDungDaChon.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn ít nhất một món áp dụng!");
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

        // Nếu đang edit, cập nhật chiTietHienTai
        if (isEditMode) {
            MonAn monApDung = cacMonApDungDaChon.iterator().next();
            chiTietHienTai.setMonApDung(monApDung);
            chiTietHienTai.setMonTang(monTangDaChon);
            chiTietHienTai.setTyLeGiam(tyLeGiam);
            chiTietHienTai.setSoTienGiam(soTienGiam);
            chiTietHienTai.setSoLuongTang(soLuongTang);
        }

        // Lưu data để ManagerDialog xử lý
        cacMonApDungKetQua = new HashSet<>(cacMonApDungDaChon);
        monTangKetQua = monTangDaChon;
        tyLeGiamKetQua = tyLeGiam;
        soTienGiamKetQua = soTienGiam;
        soLuongTangKetQua = soLuongTang;
        daLuu = true;

        this.close();
    }

    public ChiTietKhuyenMai layKetQua() {
        if (isEditMode && chiTietHienTai != null) {
            return chiTietHienTai;
        }
        return null;
    }

    public boolean daLuu() {
        return daLuu;
    }

    public Set<MonAn> layCacMonApDung() {
        return cacMonApDungKetQua;
    }

    public MonAn layMonTang() {
        return monTangKetQua;
    }

    public BigDecimal layTyLeGiam() {
        return tyLeGiamKetQua;
    }

    public BigDecimal laySoTienGiam() {
        return soTienGiamKetQua;
    }

    public Integer laySoLuongTang() {
        return soLuongTangKetQua;
    }

    public List<ChiTietKhuyenMai> layDanhSachKetQua() {
        return danhSachKetQua;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Lỗi" : "Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(this);
        alert.showAndWait();
    }
}