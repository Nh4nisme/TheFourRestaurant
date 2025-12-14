package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.controller.MonAnController;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonAnDialog extends Stage {

    private MonAn ketQua = null;
    private File tepAnhDaChon = null;
    private final boolean isEditMode;
    private final MonAn monAnHienTai;
    private final MonAnController controller;

    // Các thành phần UI
    private final TextField truongTen = new TextField();
    private final TextField truongGia = new TextField();
    private final ComboBox<LoaiMon> loaiMonComboBox = new ComboBox<>();
    private final CheckBox hopKiemHienThi = new CheckBox();
    private final Spinner<Integer> truongSoLuong = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000, 0));
    private final ImageView khungHinhAnh = new ImageView();

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
        Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".bmp")
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public MonAnDialog(MonAn monAn, List<LoaiMon> tatCaLoaiMon, LoaiMon loaiMonMacDinh, MonAnController controller) {
        this.monAnHienTai = monAn;
        this.isEditMode = (monAn != null);
        this.controller = controller;

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(isEditMode ? "Tùy Chỉnh Món Ăn" : "Thêm Món Ăn Mới");

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

        Label nhanTieuDe = new Label(isEditMode ? "Tùy chỉnh món ăn" : "Thêm món ăn");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        GridPane luoiFormChinh = createMainForm(tatCaLoaiMon, loaiMonMacDinh, kieuFontStyle);
        VBox hopGiua = new VBox(20, luoiFormChinh);
        hopGiua.setPadding(new Insets(20));

        HBox hopChanTrang = createFooter();

        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(hopGiua);
        layoutChinh.setBottom(hopChanTrang);

        if (isEditMode) {
            dienDuLieuHienCo();
        }

        Scene khungCanh = new Scene(layoutChinh, 500, 450);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        this.setResizable(false);
    }

    private GridPane createMainForm(List<LoaiMon> tatCaLoaiMon, LoaiMon loaiMonMacDinh, String kieuFontStyle) {
        GridPane luoiForm = new GridPane();
        luoiForm.setVgap(12);
        luoiForm.setHgap(15);

        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        truongTen.setStyle(kieuTruongNhap);
        truongTen.getStyleClass().add("text-field");
        truongGia.setStyle(kieuTruongNhap);
        truongGia.getStyleClass().add("text-field");
        loaiMonComboBox.setStyle(kieuTruongNhap);
        loaiMonComboBox.getStyleClass().add("combo-box");
        hopKiemHienThi.getStyleClass().add("custom-checkbox");

        // Ảnh ở trên cùng
        VBox hopAnh = new VBox(5);
        hopAnh.setAlignment(Pos.CENTER);
        hopAnh.setPrefSize(120, 120);
        hopAnh.setMinSize(120, 120);
        hopAnh.setMaxSize(120, 120);
        hopAnh.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #CCCCCC; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-style: dashed;");
        hopAnh.setCursor(javafx.scene.Cursor.HAND);

        try (InputStream luongAnh = getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png")) {
            if (luongAnh != null) {
                khungHinhAnh.setImage(new javafx.scene.image.Image(luongAnh));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        khungHinhAnh.setFitWidth(100);
        khungHinhAnh.setFitHeight(100);
        hopAnh.getChildren().add(khungHinhAnh);
        hopAnh.setOnMouseClicked(e -> chonAnh());
        luoiForm.add(hopAnh, 0, 0, 2, 1);

        luoiForm.add(new Label("Tên:"), 0, 1);
        luoiForm.add(truongTen, 1, 1);

        TextField truongMa = new TextField(isEditMode ? monAnHienTai.getMaMonAn() : "Tạo tự động");
        truongMa.setEditable(false);
        truongMa.setDisable(true);
        truongMa.setFocusTraversable(false);
        truongMa.setStyle(kieuTruongNhap);
        truongMa.getStyleClass().add("text-field");
        truongMa.setTooltip(new Tooltip("Mã được tạo tự động và không thể chỉnh sửa"));
        luoiForm.add(new Label("Mã Món Ăn:"), 0, 2);
        luoiForm.add(truongMa, 1, 2);

        luoiForm.add(new Label("Giá:"), 0, 3);
        luoiForm.add(truongGia, 1, 3);

        luoiForm.add(new Label("Loại món:"), 0, 4);
        loaiMonComboBox.setItems(FXCollections.observableArrayList(tatCaLoaiMon));
        loaiMonComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(LoaiMon object) {
                return object == null ? "" : object.getTenLoaiMon();
            }

            @Override
            public LoaiMon fromString(String string) {
                return null;
            }
        });
        if (loaiMonMacDinh != null) {
            loaiMonComboBox.setValue(loaiMonMacDinh);
        }
        luoiForm.add(loaiMonComboBox, 1, 4);

        luoiForm.add(new Label("Hiển thị:"), 0, 5);
        hopKiemHienThi.setSelected(true);
        luoiForm.add(hopKiemHienThi, 1, 5);

        // Số lượng (chỉ thêm một lần)
        truongSoLuong.setEditable(true);
        truongSoLuong.setPrefWidth(120);
        truongSoLuong.getStyleClass().add("text-field");
        luoiForm.add(new Label("Số lượng:"), 0, 6);
        luoiForm.add(truongSoLuong, 1, 6);

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
        truongTen.setText(monAnHienTai.getTenMon());
        truongGia.setText(monAnHienTai.getDonGia().toPlainString());
        hopKiemHienThi.setSelected(monAnHienTai.getTrangThai().equalsIgnoreCase("Con"));

        loaiMonComboBox.setValue(monAnHienTai.getLoaiMon());
        try {
            truongSoLuong.getValueFactory().setValue(monAnHienTai.getSoLuong());
        } catch (Exception e) {
            truongSoLuong.getValueFactory().setValue(0);
        }
        // Hiển thị ảnh hiện có nếu có
        String imagePath = monAnHienTai.getHinhAnh();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                javafx.scene.image.Image anh = null;
                if (imagePath.startsWith("/")) {
                    try (java.io.InputStream stream = getClass().getResourceAsStream(imagePath)) {
                        if (stream != null) {
                            anh = new javafx.scene.image.Image(stream);
                        } else {
                            System.err.println("Không tìm thấy tài nguyên classpath: " + imagePath);
                        }
                    }
                } else {
                    anh = new javafx.scene.image.Image(imagePath);
                }

                if (anh != null && !anh.isError()) {
                    khungHinhAnh.setImage(anh);
                }
            } catch (Exception e) {
                System.err.println("Không thể tải ảnh: " + imagePath);
                e.printStackTrace();
            }
        }
    }

    private void chonAnh() {
        FileChooser boChonTep = new FileChooser();
        boChonTep.setTitle("Chọn Ảnh Món Ăn");
        boChonTep.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File tep = boChonTep.showOpenDialog(this);
        if (tep != null) {
            // Kiểm tra định dạng file
            String fileName = tep.getName().toLowerCase();
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = fileName.substring(dotIndex);
            }
            
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                showAlert(Alert.AlertType.WARNING, 
                    "Định dạng ảnh không hỗ trợ!\n" +
                    "Chỉ chấp nhận: PNG, JPG, JPEG, GIF, BMP\n" +
                    "File của bạn: " + extension);
                return;
            }
            
            // Kiểm tra kích thước file
            if (tep.length() > MAX_FILE_SIZE) {
                showAlert(Alert.AlertType.WARNING, 
                    "File ảnh quá lớn!\n" +
                    "Giới hạn: 10MB\n" +
                    "Kích thước file: " + (tep.length() / 1024 / 1024) + "MB");
                return;
            }
            
            tepAnhDaChon = tep;
            try {
                javafx.scene.image.Image anh = new javafx.scene.image.Image(tep.toURI().toString());
                khungHinhAnh.setImage(anh);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            showAlert(Alert.AlertType.INFORMATION, "Đã chọn ảnh: " + tep.getName());
        }
    }

    private void luuThayDoi() {
        if (truongTen.getText().trim().isEmpty() || truongGia.getText().trim().isEmpty() || loaiMonComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập Tên, Giá và chọn Loại món ăn!");
            return;
        }

        BigDecimal donGia;
        try {
            donGia = new BigDecimal(truongGia.getText().trim());
            if (donGia.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.WARNING, "Đơn giá không được là số âm!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Đơn giá phải là một con số hợp lệ!");
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
        ketQua.setLoaiMon(loaiMonComboBox.getValue());
        try {
            ketQua.setSoLuong(truongSoLuong.getValue());
        } catch (Exception e) {
            ketQua.setSoLuong(0);
        }

        if (tepAnhDaChon != null) {
            String newImagePath = controller.saoChepHinhAnhVaoProject(tepAnhDaChon.getAbsolutePath());
            if (newImagePath != null) {
                ketQua.setHinhAnh(newImagePath);
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi khi sao chép hình ảnh!");
                return; // Dừng lưu nếu sao chép ảnh thất bại
            }
        } else if (!isEditMode) {
            ketQua.setHinhAnh(null);
        }
        // nếu đang chỉnh sửa và không chọn ảnh mới, đường dẫn ảnh cũ được giữ mặc định

        this.close();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(this);
        alert.showAndWait();
    }

    public MonAn layKetQua() {
        return ketQua;
    }
}