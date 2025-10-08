package com.thefourrestaurant.view.QuanLy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;


public class LoaiMonAn extends GridPane {

    // --- Thuộc tính của lớp cho chức năng phân trang ---
    private GridPane luoiCacMucDuoi;
    private HBox boChuyenTrang;
    private final int tongHop = 25;
    private final int soMucMoiTrang = 20;
    private final int soCotMoiHang = 10;
    private int trangHienTai = 1;

    public LoaiMonAn() {
        // GridPane
        this.setStyle("-fx-background-color: #F5F5F5;");

        // Chia GridPane Trên Dưới
        RowConstraints hangTren = new RowConstraints();
        hangTren.setPercentHeight(5);
        RowConstraints hangDuoi = new RowConstraints();
        hangDuoi.setPercentHeight(95);
        this.getRowConstraints().addAll(hangTren, hangDuoi);

        // Phần trên: Đường dẫn
        Label duongDan = new Label("Quản Lý > Loại Món Ăn");
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungTren = new VBox(duongDan);
        khungTren.setStyle("-fx-background-color: #673E1F;"); // Đặt màu nền cho khung trên
        khungTren.setAlignment(Pos.CENTER_LEFT);
        khungTren.setPadding(new Insets(0, 20, 0, 20));
        khungTren.setMaxWidth(Double.MAX_VALUE);
        khungTren.setMaxHeight(Double.MAX_VALUE);
        GridPane.setHgrow(khungTren, Priority.ALWAYS);
        GridPane.setVgrow(khungTren, Priority.ALWAYS);
        this.add(khungTren, 0, 0);

        // Phần dưới: Nội dung chính
        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.CENTER);
        khungDuoi.setPadding(new Insets(20));
        GridPane.setMargin(khungDuoi, new Insets(10, 10, 10, 10)); // Thêm lề cho khung dưới
        this.add(khungDuoi, 0, 1);

        VBox dsLoaiMonAn = new VBox(20); // thêm khoảng cách giữa trên và dưới
        dsLoaiMonAn.setStyle("-fx-background-color: #F0F2F3; -fx-background-radius: 10;");
        dsLoaiMonAn.setAlignment(Pos.TOP_CENTER);
        dsLoaiMonAn.setPadding(new Insets(20));
        GridPane.setMargin(dsLoaiMonAn, new Insets(20, 20, 20, 20));
        khungDuoi.getChildren().add(dsLoaiMonAn);
        dsLoaiMonAn.setPrefWidth(800);
        dsLoaiMonAn.setPrefHeight(800);

        VBox khungTrenLoaiMonAn = new VBox();
        khungTrenLoaiMonAn.setPrefHeight(250);
        khungTrenLoaiMonAn.setAlignment(Pos.CENTER_LEFT);
        khungTrenLoaiMonAn.setPadding(new Insets(10, 20, 10, 20));
        khungTrenLoaiMonAn.setStyle("-fx-background-color: #F0F2F3");

        // Thay đổi VBox thành BorderPane để khóa cứng vị trí bộ chuyển trang
        BorderPane khungDuoiLoaiMonAn = new BorderPane();
        khungDuoiLoaiMonAn.setPadding(new Insets(10, 0, 0, 0));

        // Khởi tạo Lưới và Bộ điều khiển phân trang
        luoiCacMucDuoi = new GridPane();
        luoiCacMucDuoi.setAlignment(Pos.CENTER);
        luoiCacMucDuoi.setHgap(20);
        luoiCacMucDuoi.setVgap(20);
        luoiCacMucDuoi.getStyleClass().add("grid-pane");

        boChuyenTrang = new HBox(10);
        boChuyenTrang.setAlignment(Pos.CENTER);
        boChuyenTrang.setPadding(new Insets(15, 0, 0, 0)); // Thêm khoảng cách phía trên bộ chuyển trang

        GridPane luoiCacMucTren = new GridPane();
        luoiCacMucTren.setAlignment(Pos.BASELINE_LEFT);
        luoiCacMucTren.setHgap(20);
        luoiCacMucTren.setVgap(20);
        luoiCacMucTren.getStyleClass().add("grid-pane");
        luoiCacMucTren.setPadding(new Insets(0, 0, 0, 5));


        // Thêm hộp thêm mục mới
        VBox hopThemMoi = taoHopThemMoi();
        luoiCacMucTren.add(hopThemMoi, 0, 0);

        // Hiển thị dữ liệu ban đầu ---
        capNhatLuoiMonAn(trangHienTai);
        thietLapPhanTrang();

        khungTrenLoaiMonAn.getChildren().add(luoiCacMucTren);

        // Đặt lưới vào giữa và bộ chuyển trang xuống dưới cùng của BorderPane
        khungDuoiLoaiMonAn.setCenter(luoiCacMucDuoi);
        khungDuoiLoaiMonAn.setBottom(boChuyenTrang);

        dsLoaiMonAn.getChildren().addAll(khungTrenLoaiMonAn, khungDuoiLoaiMonAn);
        // Cho phép khung dưới mở rộng để đẩy bộ chuyển trang xuống
        VBox.setVgrow(khungDuoiLoaiMonAn, Priority.ALWAYS);

        // Tải CSS
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }
    }

    private void capNhatLuoiMonAn(int trang) {
        trangHienTai = trang;
        luoiCacMucDuoi.getChildren().clear();

        int batDau = (trang - 1) * soMucMoiTrang;
        int ketThuc = Math.min(batDau + soMucMoiTrang, tongHop);

        for (int i = batDau; i < ketThuc; i++) {
            VBox hopLoaiMonAn = taoHopLoaiMonAn("Loại Món Ăn " + (i + 1));

            int chiSoTrongTrang = i - batDau;
            int col = chiSoTrongTrang % soCotMoiHang;
            int row = chiSoTrongTrang / soCotMoiHang;

            luoiCacMucDuoi.add(hopLoaiMonAn, col, row);
        }
        // Cập nhật lưới
        thietLapPhanTrang();
    }

    private void thietLapPhanTrang() {
        boChuyenTrang.getChildren().clear();

        if (tongHop <= soMucMoiTrang) {
            return;
        }

        int tongSoTrang = (int) Math.ceil((double) tongHop / soMucMoiTrang);

        for (int i = 1; i <= tongSoTrang; i++) {
            final int soTrang = i;
            Label trangLabel = new Label(String.valueOf(soTrang));
            trangLabel.setPadding(new Insets(5, 10, 5, 10));
            trangLabel.setCursor(Cursor.HAND);

            if (trangHienTai == soTrang) {
                trangLabel.setStyle("-fx-background-color: #673E1F; -fx-text-fill: white; -fx-background-radius: 5;");
            } else {
                trangLabel.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: black; -fx-background-radius: 5;");
            }

            trangLabel.setOnMouseClicked(event -> {
                capNhatLuoiMonAn(soTrang);
            });

            boChuyenTrang.getChildren().add(trangLabel);
        }
    }

    private VBox taoHopThemMoi() {
        VBox hop = new VBox(5);
        hop.setAlignment(Pos.CENTER);
        hop.getStyleClass().add("add-item-box");

        Image plusImage = new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/Them.png"));
        ImageView plusImageView = new ImageView(plusImage);
        plusImageView.setFitWidth(50); // Điều chỉnh kích thước ảnh
        plusImageView.setFitHeight(50); // Điều chỉnh kích thước ảnh

        // Tạo Button
        Button addButton = new Button("Thêm loại mới", plusImageView);
        addButton.setContentDisplay(ContentDisplay.TOP);
        addButton.getStyleClass().add("add-item-button");
        addButton.setPrefSize(145, 180);
        addButton.setMaxSize(145, 180);
        addButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        hop.setPrefWidth(145); hop.setPrefHeight(180);
        hop.setMaxSize(145, 180);

        // Hiệu ứng Hover
        final String normalStyle = "-fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 15px; -fx-background-color: white;";
        final String hoverStyle = "-fx-border-color: #673E1F; -fx-border-width: 10px; -fx-border-radius: 15px; -fx-background-color: #F9F9F9;";

        hop.setStyle(normalStyle);

        hop.setOnMouseEntered(event -> {
            hop.setStyle(hoverStyle);
            hop.setCursor(Cursor.HAND);
        });

        hop.setOnMouseExited(event -> {
            hop.setStyle(normalStyle);
            hop.setCursor(Cursor.DEFAULT);
        });

        // Thêm button vào VBox
        hop.getChildren().add(addButton);

        return hop;
    }

    private VBox taoHopLoaiMonAn(String ten) {
        VBox hopLoaiMonAn = new VBox(5);
        hopLoaiMonAn.setAlignment(Pos.CENTER);
        hopLoaiMonAn.getStyleClass().add("loai-mon-an-box");

        // Gộp thiết lập kích thước và kiểu dáng vào đây
        hopLoaiMonAn.setPrefWidth(145);
        hopLoaiMonAn.setPrefHeight(180);
        hopLoaiMonAn.setMaxSize(145, 180);

        Label nhanTen = new Label(ten);
        nhanTen.getStyleClass().add("loai-mon-an-name");

        hopLoaiMonAn.getChildren().add(nhanTen);

        // Thêm hiệu ứng Hover
        final String normalStyle = "-fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 15px; -fx-background-color: white;";
        final String hoverStyle = "-fx-border-color: #673E1F; -fx-border-width: 10px; -fx-border-radius: 15px; -fx-background-color: #F9F9F9;";

        hopLoaiMonAn.setStyle(normalStyle);

        hopLoaiMonAn.setOnMouseEntered(event -> {
            hopLoaiMonAn.setStyle(hoverStyle);
            hopLoaiMonAn.setCursor(Cursor.HAND);
        });

        hopLoaiMonAn.setOnMouseExited(event -> {
            hopLoaiMonAn.setStyle(normalStyle);
            hopLoaiMonAn.setCursor(Cursor.DEFAULT);
        });

        return hopLoaiMonAn;
    }
}
