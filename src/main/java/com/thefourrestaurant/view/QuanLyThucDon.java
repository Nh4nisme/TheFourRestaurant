package com.thefourrestaurant.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.thefourrestaurant.DAO.ThucDonDAO;
import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.NavBar;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class QuanLyThucDon extends VBox {

    private final TableView<ThucDonDAO.ThucDonView> tableThucDon;
    private final TextField txtTenThucDon;
    private final ComboBox<String> cbLoaiMonAn;
    private final VBox boxChonThucAn;
    private final List<FoodItem> selectedFoods = new ArrayList<>();

    public QuanLyThucDon() {
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #FAFAFA;");

        // === Thanh breadcrumb ===
        Label duongDan = new Label("Quản Lý > Thực Đơn");
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungDuongDan = new VBox(duongDan);
        khungDuongDan.setStyle("-fx-background-color: #673E1F;");
        khungDuongDan.setAlignment(Pos.CENTER_LEFT);
        khungDuongDan.setPadding(new Insets(10, 20, 10, 20));
        khungDuongDan.setPrefHeight(40);
        khungDuongDan.setMaxWidth(Double.MAX_VALUE);

        // === Khu vực chính (chia 2 cột) ===
        HBox mainContent = new HBox(24);
        mainContent.setPadding(new Insets(24));
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // === BÊN TRÁI: Danh sách thực đơn ===
        VBox leftPane = new VBox(16);
        leftPane.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4,0,0,2);");
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(450);

        Label lblDanhSach = new Label("Danh sách Thực Đơn");
        lblDanhSach.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblDanhSach.setTextFill(Color.web("#1E424D"));

        HBox thanhCongCu = new HBox(10);
        thanhCongCu.setAlignment(Pos.CENTER_LEFT);
        ButtonSample btnTaiLai = new ButtonSample("Tải lại", 35, 14, 3);
    thanhCongCu.getChildren().add(btnTaiLai);

    tableThucDon = new TableView<>();
        tableThucDon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    TableColumn<ThucDonDAO.ThucDonView, String> tenCol = new TableColumn<>("Tên");
    TableColumn<ThucDonDAO.ThucDonView, String> loaiMonAnCol = new TableColumn<>("Các Loại Món Ăn");
    tenCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tenTD));
    loaiMonAnCol.setCellValueFactory(d -> new SimpleStringProperty(
        d.getValue().loaiMon == null ? "" : d.getValue().loaiMon
    ));
    tableThucDon.getColumns().addAll(tenCol, loaiMonAnCol);
        VBox.setVgrow(tableThucDon, Priority.ALWAYS);

        leftPane.getChildren().addAll(lblDanhSach, thanhCongCu, tableThucDon);

        // === BÊN PHẢI: Form tạo thực đơn ===
        VBox rightPane = new VBox(32);
        rightPane.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4,0,0,2);");
        rightPane.setPadding(new Insets(24));
        VBox.setVgrow(rightPane, Priority.ALWAYS);

        Label lblTaoMoi = new Label("Chi tiết thực đơn");
        lblTaoMoi.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTaoMoi.setTextFill(Color.web("#1E424D"));

    Label lblTen = new Label("Tên thực đơn");
    lblTen.setStyle("-fx-text-fill: #E19E11; -fx-font-size: 14px; -fx-font-weight: bold;");
    txtTenThucDon = new TextField();
    txtTenThucDon.setPromptText("Nhập tên thực đơn...");
    txtTenThucDon.setStyle("-fx-background-color: #D9DEE2; -fx-background-radius: 10; -fx-font-size: 15px; -fx-padding: 8 12;");

    VBox boxTen = new VBox(6, lblTen, txtTenThucDon);

        // Chọn loại món ăn
        Label lblLoai = new Label("Chọn loại món ăn");
        lblLoai.setStyle("-fx-text-fill: #E19E11; -fx-font-size: 14px; -fx-font-weight: bold;");
    cbLoaiMonAn = new ComboBox<>();
    // Load loại món ăn từ DB để đồng bộ với dữ liệu và hiển thị đúng trong bảng bên trái
    loadLoaiMonAnFromDB();
        cbLoaiMonAn.setPromptText("Chọn loại món ăn...");
        cbLoaiMonAn.setStyle("-fx-background-color: #D9DEE2; -fx-background-radius: 10; -fx-font-size: 15px; -fx-padding: 8 12;");
        cbLoaiMonAn.setPrefWidth(400);

        boxChonThucAn = new VBox(0);
        boxChonThucAn.setSpacing(0);
        VBox.setVgrow(boxChonThucAn, Priority.ALWAYS);
        capNhatBoxChonThucAn();

        cbLoaiMonAn.setOnAction(e -> {
            String selected = cbLoaiMonAn.getValue();
            if (selected != null) {
                Optional<FoodItem> existing = selectedFoods.stream()
                        .filter(f -> f.name.equals(selected))
                        .findFirst();
                if (existing.isPresent()) {
                    existing.get().quantity++;
                } else {
                    selectedFoods.add(new FoodItem(selected, getFoodIcon(selected), 1));
                }
                capNhatBoxChonThucAn();
                Platform.runLater(() -> cbLoaiMonAn.getSelectionModel().clearSelection());
            }
        });

        ButtonSample btnLuu = new ButtonSample("Lưu Thực Đơn", 45, 16, 3);
        btnLuu.setStyle("-fx-font-weight: bold;");

        // Lưu thực đơn xuống DB
        btnLuu.setOnAction(e -> {
            String ten = txtTenThucDon.getText() != null ? txtTenThucDon.getText().trim() : "";
            if (ten.isBlank()) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng nhập tên thực đơn.");
                return;
            }
            if (selectedFoods.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng chọn ít nhất một loại món ăn.");
                return;
            }
            List<String> loai = selectedFoods.stream().map(f -> f.name).distinct().toList();

            boolean ok = new ThucDonDAO().luuThucDonTheoLoaiMon(ten, loai);
            if (ok) {
                showAlert(Alert.AlertType.INFORMATION, "Đã lưu thực đơn.");
                napBangThucDon();
            } else {
                showAlert(Alert.AlertType.ERROR, "Không thể lưu thực đơn. Kiểm tra kết nối CSDL và bảng dữ liệu.");
            }
        });

        // Nút Tải lại
        btnTaiLai.setOnAction(e -> napBangThucDon());

        rightPane.getChildren().addAll(lblTaoMoi, boxTen, lblLoai, cbLoaiMonAn, boxChonThucAn, btnLuu);

        // === Gộp 2 bên lại ===
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        mainContent.getChildren().addAll(leftPane, rightPane);

        // === Load CSS ===
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        getChildren().addAll(khungDuongDan, mainContent);
        // Tải dữ liệu lúc mở màn hình
        napBangThucDon();
    }

    // ==== HÀM CẬP NHẬT DANH SÁCH MÓN ĂN ====
    private void capNhatBoxChonThucAn() {
        boxChonThucAn.getChildren().clear();
        if (selectedFoods.isEmpty()) {
            return;
        }

        HBox header = new HBox();
        header.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-width: 1 1 0 1;");
        header.setPadding(new Insets(8, 16, 8, 16));
        header.setSpacing(0);
        Label lblTen = new Label("Tên");
        lblTen.setPrefWidth(300);
        Label lblSoLuong = new Label("Số lượng");
        lblSoLuong.setPrefWidth(100);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(lblTen, lblSoLuong, spacer);
        boxChonThucAn.getChildren().add(header);

        for (FoodItem item : selectedFoods) {
            HBox row = new HBox();
            row.setStyle("-fx-background-color: #FFF; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 1;");
            row.setPadding(new Insets(8, 16, 8, 16));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setSpacing(8);

            ImageView iconView = null;
            try {
                iconView = new ImageView(new Image(getClass().getResourceAsStream(item.icon)));
                iconView.setFitWidth(24);
                iconView.setFitHeight(24);
            } catch (Exception e) {
                System.err.println("Không thể tải icon: " + item.icon);
            }

            Label lblTenMon = new Label(item.name);
            lblTenMon.setFont(Font.font("System", FontWeight.BOLD, 14));

            HBox boxTen = new HBox(8);
            if (iconView != null) boxTen.getChildren().add(iconView);
            boxTen.getChildren().add(lblTenMon);
            boxTen.setPrefWidth(300);

            Label lblSL = new Label(String.valueOf(item.quantity));
            lblSL.setPrefWidth(100);

            Button btnXoa = new ButtonSample("Xóa", 28, 13, 1);
            btnXoa.setOnAction(e -> {
                selectedFoods.remove(item);
                capNhatBoxChonThucAn();
            });

            Region space = new Region();
            HBox.setHgrow(space, Priority.ALWAYS);

            row.getChildren().addAll(boxTen, lblSL, space, btnXoa);
            boxChonThucAn.getChildren().add(row);
        }
    }

    private String getFoodIcon(String name) {
        return switch (name) {
            case "Coffee" -> "/com/thefourrestaurant/images/icon/food/coffee.png";
            case "Cơm" -> "/com/thefourrestaurant/images/icon/food/rice.png";
            case "Nước giải khát" -> "/com/thefourrestaurant/images/icon/food/coffee.png";
            case "Đồ ăn nhanh" -> "/com/thefourrestaurant/images/icon/food/rice.png";
            default -> "/com/thefourrestaurant/images/icon/food/coffee.png";
        };
    }

    private static class FoodItem {
        String name;
        String icon;
        int quantity;

        FoodItem(String name, String icon, int quantity) {
            this.name = name;
            this.icon = icon;
            this.quantity = quantity;
        }
    }

    private void napBangThucDon() {
        ThucDonDAO dao = new ThucDonDAO();
        var list = dao.layTatCaThucDonGomLoai();
        tableThucDon.setItems(javafx.collections.FXCollections.observableArrayList(list));
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert a = new Alert(type, message);
        a.showAndWait();
    }

    // Đồng bộ danh sách loại món ăn với DB (bảng LoaiMonAn.tenLoaiMon)
    private void loadLoaiMonAnFromDB() {
        try {
            var ds = new LoaiMonDAO().layTatCaLoaiMon();
            cbLoaiMonAn.getItems().clear();
            for (var lm : ds) {
                if (lm != null && lm.getTenLoaiMon() != null && !lm.getTenLoaiMon().isBlank()) {
                    cbLoaiMonAn.getItems().add(lm.getTenLoaiMon());
                }
            }
        } catch (Exception ex) {
            System.err.println("Không thể tải danh sách loại món ăn từ DB: " + ex.getMessage());
        }
    }
}
