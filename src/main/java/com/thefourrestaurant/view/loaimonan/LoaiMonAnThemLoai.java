package com.thefourrestaurant.view.loaimonan;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoaiMonAnThemLoai extends Stage {

    private Map<String, Object> ketQua = null;
    private File tepAnhDaChon = null;

    public LoaiMonAnThemLoai() {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Thêm Loại Món Ăn Mới");

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

        String kieuNhan = kieuFontStyle + "-fx-text-fill: #E19E11; -fx-font-size: 14px;";
        String kieuTruongNhap = kieuFontStyle + "-fx-text-fill: #E19E11;";

        BorderPane layoutChinh = new BorderPane();

        // --- Header ---
        Label nhanTieuDe = new Label("Tùy chỉnh loại món ăn");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox phanDau = new HBox(nhanTieuDe);
        phanDau.setAlignment(Pos.CENTER_LEFT);
        phanDau.setPadding(new Insets(15));
        phanDau.setStyle("-fx-background-color: #1E424D;");

        // --- Phần Ảnh ---
        HBox phanTren = new HBox();
        phanTren.setAlignment(Pos.CENTER_LEFT);
        phanTren.setPadding(new Insets(20, 0, 10, 150));

        VBox hopAnh = new VBox(5);
        hopAnh.setAlignment(Pos.CENTER);
        hopAnh.setPrefSize(150, 150);
        hopAnh.setMinSize(150, 150);
        hopAnh.setMaxSize(150, 150);
        hopAnh.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #CCCCCC; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-style: dashed;");
        hopAnh.setCursor(Cursor.HAND);

        ImageView khungHinhAnh = new ImageView();
        try (InputStream luongAnh = getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png")) {
            if (luongAnh != null) {
                khungHinhAnh.setImage(new Image(luongAnh));
            } else {
                System.err.println("Không tìm thấy ảnh mặc định.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh mặc định: " + e.getMessage());
        }

        khungHinhAnh.setFitWidth(150);
        khungHinhAnh.setFitHeight(150);

        hopAnh.getChildren().addAll(khungHinhAnh);
        phanTren.getChildren().add(hopAnh);

        // --- Form ---
        VBox phanGiua = new VBox(15);
        phanGiua.setPadding(new Insets(10, 20, 20, 20));
        phanGiua.setAlignment(Pos.TOP_LEFT);

        GridPane luoiForm = new GridPane();
        luoiForm.setHgap(15);
        luoiForm.setVgap(15);
        luoiForm.setAlignment(Pos.TOP_LEFT);
        ColumnConstraints cot1 = new ColumnConstraints(80);
        ColumnConstraints cot2 = new ColumnConstraints();
        cot2.setHgrow(Priority.ALWAYS);
        luoiForm.getColumnConstraints().addAll(cot1, cot2);

        Label nhanTen = new Label("Tên:");
        nhanTen.setStyle(kieuNhan);
        TextField truongTen = new TextField();
        truongTen.setPromptText("Nhập tên loại món ăn");
        truongTen.setStyle(kieuTruongNhap);
        truongTen.setMaxWidth(Double.MAX_VALUE);
        truongTen.getStyleClass().add("text-field");

        Label nhanMoTa = new Label("Mô tả:");
        nhanMoTa.setStyle(kieuNhan);
        TextArea vungMoTa = new TextArea();
        vungMoTa.setPromptText("Nhập mô tả chi tiết");
        vungMoTa.setPrefRowCount(3);
        vungMoTa.setStyle(kieuTruongNhap);
        vungMoTa.setMaxWidth(Double.MAX_VALUE);
        vungMoTa.getStyleClass().add("text-area");

        Label nhanHien = new Label("Hiện:");
        nhanHien.setStyle(kieuNhan);
        CheckBox hopKiemHien = new CheckBox();
        hopKiemHien.setSelected(true);
        hopKiemHien.getStyleClass().add("custom-checkbox");

        luoiForm.add(nhanTen, 0, 0);
        luoiForm.add(truongTen, 1, 0);
        luoiForm.add(nhanMoTa, 0, 1);
        luoiForm.add(vungMoTa, 1, 1);
        luoiForm.add(nhanHien, 0, 2);
        luoiForm.add(hopKiemHien, 1, 2);

        phanGiua.getChildren().add(luoiForm);

        // --- Footer ---
        HBox phanCuoi = new HBox(10);
        phanCuoi.setPadding(new Insets(10));
        phanCuoi.setAlignment(Pos.CENTER_LEFT);
        phanCuoi.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        Button nutXoa = new ButtonSample("Xóa", 35, 14, 2);
        Region vungDem = new Region();
        HBox.setHgrow(vungDem, Priority.ALWAYS);
        Button nutOke = new ButtonSample("Oke", 35, 14, 2);
        Button nutHuy = new ButtonSample("Hủy", 35, 14, 2);
        phanCuoi.getChildren().addAll(nutXoa, vungDem, nutOke, nutHuy);

        // --- Layout tổng ---
        VBox noiDungChinh = new VBox();
        noiDungChinh.getChildren().addAll(phanDau, phanTren, phanGiua);
        layoutChinh.setCenter(noiDungChinh);
        layoutChinh.setBottom(phanCuoi);

        // --- Sự kiện ---
        hopAnh.setOnMouseClicked(event -> {
            FileChooser boChonTep = new FileChooser();
            boChonTep.setTitle("Chọn Ảnh");
            boChonTep.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File tep = boChonTep.showOpenDialog(this);
            if (tep != null) {
                tepAnhDaChon = tep;
                try {
                    Image anh = new Image(tep.toURI().toString());
                    khungHinhAnh.setImage(anh);
                    khungHinhAnh.setFitWidth(120);
                    khungHinhAnh.setFitHeight(120);
                    hopAnh.getChildren().clear();
                    hopAnh.getChildren().add(khungHinhAnh);
                    hopAnh.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
                } catch (Exception ex) {
                    System.err.println("Lỗi khi tải ảnh đã chọn: " + ex.getMessage());
                }
            }
        });

        nutHuy.setOnAction(e -> this.close());

        nutOke.setOnAction(e -> {
            String tenLoaiMonAn = truongTen.getText();
            if (tenLoaiMonAn == null || tenLoaiMonAn.trim().isEmpty()) {
                Alert canhBao = new Alert(Alert.AlertType.WARNING, "Tên loại món ăn không được để trống!", ButtonType.OK);
                canhBao.setTitle("Thiếu thông tin");
                canhBao.setHeaderText(null);
                canhBao.showAndWait();
            } else {
                ketQua = new HashMap<>();
                ketQua.put("name", tenLoaiMonAn);
                ketQua.put("description", vungMoTa.getText());
                ketQua.put("is_shown", hopKiemHien.isSelected());
                if (tepAnhDaChon != null) {
                    ketQua.put("imagePath", tepAnhDaChon.toURI().toString());
                }
                this.close();
            }
        });

        // --- Scene ---
        Scene khungCanh = new Scene(layoutChinh, 580, 450);
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
