package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

public class CuaSoChiTietTraCuuMonAn extends Stage {

    private final MonAn monAn;
    private final ImageView khungHinhAnh = new ImageView();

    public CuaSoChiTietTraCuuMonAn(Stage owner, MonAn monAn) {
        this.monAn = monAn;

        this.initOwner(owner);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Chi Tiết Món Ăn");

        // Tải font Montserrat
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

        // Header
        Label nhanTieuDe = new Label("Thông tin chi tiết món ăn");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        // Center
        GridPane luoiForm = createDetailForm(kieuFontStyle);
        VBox hopGiua = new VBox(20, luoiForm);
        hopGiua.setPadding(new Insets(20));
        hopGiua.setAlignment(Pos.TOP_CENTER);

        // Footer
        HBox hopChanTrang = new HBox(10);
        hopChanTrang.setPadding(new Insets(15));
        hopChanTrang.setAlignment(Pos.CENTER_RIGHT);
        hopChanTrang.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");
        Button nutDong = new ButtonSample("Đóng", 35, 14, 2);
        nutDong.setOnAction(e -> this.close());
        hopChanTrang.getChildren().add(nutDong);

        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(hopGiua);
        layoutChinh.setBottom(hopChanTrang);

        Scene khungCanh = new Scene(layoutChinh, 400, 640);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        this.setResizable(false);
        this.centerOnScreen();
    }

    private GridPane createDetailForm(String kieuFontStyle) {
        GridPane luoiForm = new GridPane();
        luoiForm.setVgap(15);
        luoiForm.setHgap(15);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        luoiForm.getColumnConstraints().addAll(col1, col2);

        String kieuNhan = kieuFontStyle + "-fx-text-fill: #1E424D; -fx-font-weight: bold;";
        String kieuGiaTri = kieuFontStyle + "-fx-text-fill: #333333; -fx-background-color: #F9F9F9; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #E0E0E0; -fx-border-radius: 5;";

        // Ảnh
        VBox hopAnh = new VBox();
        hopAnh.setAlignment(Pos.CENTER);
        hopAnh.setPrefSize(150, 150);
        hopAnh.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");

        loadMonAnImage();
        khungHinhAnh.setFitWidth(140);
        khungHinhAnh.setFitHeight(140);
        khungHinhAnh.setPreserveRatio(true);
        hopAnh.getChildren().add(khungHinhAnh);
        luoiForm.add(hopAnh, 0, 0, 2, 1);
        GridPane.setHalignment(hopAnh, javafx.geometry.HPos.CENTER);

        // Các trường thông tin
        addInfoRow(luoiForm, "Mã Món Ăn:", monAn.getMaMonAn(), 1, kieuNhan, kieuGiaTri);
        addInfoRow(luoiForm, "Tên Món Ăn:", monAn.getTenMon(), 2, kieuNhan, kieuGiaTri);

        String formattedGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(monAn.getDonGia());
        addInfoRow(luoiForm, "Đơn Giá:", formattedGia, 3, kieuNhan, kieuGiaTri);

        String tenLoai = (monAn.getLoaiMon() != null) ? monAn.getLoaiMon().getTenLoaiMon() : "N/A";
        addInfoRow(luoiForm, "Loại Món:", tenLoai, 4, kieuNhan, kieuGiaTri);

        addInfoRow(luoiForm, "Số Lượng:", String.valueOf(monAn.getSoLuong()), 5, kieuNhan, kieuGiaTri);

        String trangThai = monAn.getTrangThai();
        Label lblTrangThai = new Label(trangThai);
        lblTrangThai.setMaxWidth(Double.MAX_VALUE);
        lblTrangThai.setStyle(kieuGiaTri + (trangThai.equalsIgnoreCase("Còn") ? "-fx-text-fill: green;" : "-fx-text-fill: red;"));
        luoiForm.add(new Label("Trạng Thái:"), 0, 6);
        luoiForm.add(lblTrangThai, 1, 6);

        return luoiForm;
    }

    private void addInfoRow(GridPane grid, String label, String value, int row, String labelStyle, String valueStyle) {
        Label lbl = new Label(label);
        lbl.setStyle(labelStyle);
        Label val = new Label(value);
        val.setMaxWidth(Double.MAX_VALUE);
        val.setStyle(valueStyle);
        grid.add(lbl, 0, row);
        grid.add(val, 1, row);
    }

    private void loadMonAnImage() {
        String imagePath = monAn.getHinhAnh();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = null;
                if (imagePath.startsWith("/")) {
                    try (InputStream stream = getClass().getResourceAsStream(imagePath)) {
                        if (stream != null) {
                            image = new Image(stream);
                        }
                    }
                } else {
                    image = new Image(imagePath);
                }

                if (image != null && !image.isError()) {
                    khungHinhAnh.setImage(image);
                } else {
                    loadDefaultImage();
                }
            } catch (Exception e) {
                loadDefaultImage();
            }
        } else {
            loadDefaultImage();
        }
    }

    private void loadDefaultImage() {
        try (InputStream stream = getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png")) {
            if (stream != null) {
                khungHinhAnh.setImage(new Image(stream));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
