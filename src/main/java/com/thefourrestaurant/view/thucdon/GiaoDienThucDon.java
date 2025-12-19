package com.thefourrestaurant.view.thucdon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.thefourrestaurant.DAO.ThucDonDAO;
import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.DAO.MonAnDAO;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import java.util.Comparator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ButtonType;

public class GiaoDienThucDon extends VBox {

    private final TableView<ThucDonDAO.ThucDonView> tableThucDon;
    private final TextField txtTenThucDon;
    private final ComboBox<String> cbLoaiMonAn;
    private final VBox boxChonThucAn;
    private final List<FoodItem> selectedFoods = new ArrayList<>();
    private final LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
    private final MonAnDAO monAnDAO = new MonAnDAO();
    // map tenLoai -> maLoai for quick lookup
    private final Map<String, String> loaiNameToMa = new HashMap<>();
    // store recently deleted menus (in-session) for row-based restore
    private final java.util.List<DeletedThucDon> recentlyDeleted = new java.util.ArrayList<>();

    private static class DeletedThucDon {
        final ThucDonDAO.ThucDonView view;
        final java.util.List<String> loai;
        DeletedThucDon(ThucDonDAO.ThucDonView v, java.util.List<String> l) { this.view = v; this.loai = l == null ? java.util.Collections.emptyList() : new java.util.ArrayList<>(l); }
    }

    public GiaoDienThucDon() {
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #FAFAFA;");

        // === Thanh breadcrumb ===
        Label duongDan = new Label("Thực đơn");
        duongDan.getStyleClass().add("toolbar-title");
        VBox khungDuongDan = new VBox(duongDan);
        khungDuongDan.setStyle("-fx-background-color: #1E424D;");
        khungDuongDan.setAlignment(Pos.CENTER_LEFT);
        khungDuongDan.setPadding(new Insets(10, 20, 10, 20));
        khungDuongDan.setPrefHeight(55);
        khungDuongDan.setMaxWidth(Double.MAX_VALUE);

        // === Khu vực chính (chia 2 cột) ===
        HBox mainContent = new HBox(24);
        mainContent.setPadding(new Insets(24));
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // === BÊN TRÁI: Danh sách thực đơn ===
        VBox leftPane = new VBox(16);
        leftPane.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4,0,0,2);");
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(600);
        // keep left pane width stable to avoid table shifting when right pane content changes
        leftPane.setMinWidth(600);
        leftPane.setMaxWidth(600);

        Label lblDanhSach = new Label("Danh sách Thực Đơn");
        lblDanhSach.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblDanhSach.setTextFill(Color.web("#1E424D"));

        HBox thanhCongCu = new HBox(10);
        thanhCongCu.setAlignment(Pos.CENTER_LEFT);
        ButtonSample btnTaiLai = new ButtonSample("Tải lại", 35, 14, 3);
        ButtonSample btnKhoiPhuc = new ButtonSample("Khôi phục", 35, 14, 3);
    thanhCongCu.getChildren().addAll(btnTaiLai, btnKhoiPhuc);

    tableThucDon = new TableView<>();
        tableThucDon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    TableColumn<ThucDonDAO.ThucDonView, String> tenCol = new TableColumn<>("Tên");
    TableColumn<ThucDonDAO.ThucDonView, String> loaiMonAnCol = new TableColumn<>("Các Loại Món Ăn");
    tenCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tenTD));
    loaiMonAnCol.setCellValueFactory(d -> new SimpleStringProperty(
        d.getValue().loaiMon == null ? "" : d.getValue().loaiMon
    ));

    TableColumn<ThucDonDAO.ThucDonView, Void> colAction = new TableColumn<>("Hành động");
    colAction.setCellFactory(tc -> new TableCell<>() {
        private final HBox box = new HBox(6);
        {
            box.setAlignment(Pos.CENTER);
        }
        private final ButtonSample btnSua = new ButtonSample("Sửa", 36, 14, 1);
        private final ButtonSample btnXoa = new ButtonSample("Xóa", 36, 14, 2);
        private final ButtonSample btnAdd = new ButtonSample("Thêm thực đơn", 36, 16, 1);

        {
            btnSua.setOnAction(e -> {
                ThucDonDAO.ThucDonView tv = getTableView().getItems().get(getIndex());
                if (tv != null) {
                    getTableView().getSelectionModel().select(tv);
                }
            });

            btnXoa.setOnAction(e -> {
                ThucDonDAO.ThucDonView tv = getTableView().getItems().get(getIndex());
                if (tv == null || tv.maTD == null || tv.maTD.trim().isEmpty()) return;
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Xác nhận");
                a.setHeaderText("Xác nhận");
                a.setContentText("Bạn có chắc muốn xóa thực đơn này?");
                a.initOwner(getTableView().getScene() != null ? (javafx.stage.Window) getTableView().getScene().getWindow() : null);
                a.showAndWait().ifPresent(bt -> {
                    if (bt == ButtonType.OK) {
                        try {
                            // capture associated loai before deletion so we can restore if needed
                            ThucDonDAO dao = new ThucDonDAO();
                            List<String> loai = dao.layLoaiMonTheoThucDon(tv.tenTD);
                            boolean ok = dao.xoaThucDon(tv.maTD);
                            if (ok) {
                                // remember deleted item for in-session restore
                                recentlyDeleted.add(new DeletedThucDon(tv, loai));
                                napBangThucDon();
                            } else {
                                Alert err = new Alert(Alert.AlertType.ERROR, "Xóa thất bại.");
                                err.initOwner(getTableView().getScene() != null ? (javafx.stage.Window) getTableView().getScene().getWindow() : null);
                                err.showAndWait();
                            }
                        } catch (Exception ex) { ex.printStackTrace(); }
                    }
                });
            });

            btnAdd.setOnAction(e -> {
                tableThucDon.getSelectionModel().clearSelection();
                selectedFoods.clear();
                capNhatBoxChonThucAn();
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            box.getChildren().clear();
            if (empty) {
                setGraphic(null);
                return;
            }
            ThucDonDAO.ThucDonView tv = getTableView().getItems().get(getIndex());
            if (tv == null || tv.maTD == null || tv.maTD.trim().isEmpty()) {
                btnAdd.setPrefWidth(180);
                box.getChildren().add(btnAdd);
            } else {
                btnSua.setPrefWidth(80);
                btnXoa.setPrefWidth(80);
                box.getChildren().addAll(btnSua, btnXoa);
            }
            setGraphic(box);
            setAlignment(Pos.CENTER);
        }
    });
    colAction.setPrefWidth(300);

    tableThucDon.getColumns().addAll(tenCol, loaiMonAnCol, colAction);
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
                // Lấy maLoai từ tên để đếm số món thuộc loại
                String maLoai = loaiNameToMa.get(selected);
                int qty = 0;
                if (maLoai != null) {
                    var list = monAnDAO.layMonAnTheoLoai(maLoai);
                    qty = list == null ? 0 : list.size();
                }
                Optional<FoodItem> existing = selectedFoods.stream()
                        .filter(f -> f.name.equals(selected))
                        .findFirst();
                if (existing.isPresent()) {
                    // không tăng khi chọn lại, chỉ cập nhật số lượng bằng số món thực tế
                    existing.get().quantity = qty;
                } else {
                    selectedFoods.add(new FoodItem(selected, getFoodIcon(selected), qty));
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
            List<String> loai = selectedFoods.stream().map(f -> f.name).distinct().collect(Collectors.toList());

            // Gọi DAO để lưu (DAO sẽ tạo mới hoặc cập nhật nếu tên đã tồn tại)
            boolean ok = new ThucDonDAO().luuThucDonTheoLoaiMon(ten, loai);
            if (ok) {
                showAlert(Alert.AlertType.INFORMATION, "Đã lưu thực đơn.");
                napBangThucDon();
            } else {
                showAlert(Alert.AlertType.ERROR, "Không thể lưu thực đơn. Kiểm tra kết nối CSDL và bảng dữ liệu.");
            }
        });

        btnTaiLai.setOnAction(e -> napBangThucDon());
        btnKhoiPhuc.setOnAction(e -> {
            if (recentlyDeleted.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Không có thực đơn nào để khôi phục.");
                return;
            }

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Khôi phục thực đơn");
            if (this.getScene() != null) dialog.initOwner(this.getScene().getWindow());

            TableView<DeletedThucDon> delTable = new TableView<>();
            TableColumn<DeletedThucDon, String> nameCol = new TableColumn<>("Tên thực đơn");
            nameCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().view.tenTD));
            TableColumn<DeletedThucDon, String> loaiCol = new TableColumn<>("Các loại món");
            loaiCol.setCellValueFactory(cd -> new SimpleStringProperty(String.join(", ", cd.getValue().loai)));
            TableColumn<DeletedThucDon, Void> actCol = new TableColumn<>("Hành động");
            actCol.setCellFactory(c -> new TableCell<>() {
                private final ButtonSample btnRestore = new ButtonSample("Khôi phục", 80, 14, 1);
                {
                    btnRestore.setOnAction(ev -> {
                        DeletedThucDon item = getTableView().getItems().get(getIndex());
                        if (item == null) return;
                        ThucDonDAO dao = new ThucDonDAO();
                        boolean ok = dao.luuThucDonTheoLoaiMon(item.view.tenTD, item.loai);
                        if (ok) {
                            recentlyDeleted.remove(item);
                            getTableView().getItems().remove(item);
                            napBangThucDon();
                        } else {
                            Alert err = new Alert(Alert.AlertType.ERROR, "Khôi phục thất bại.");
                            if (dialog.getDialogPane().getScene() != null) err.initOwner(dialog.getDialogPane().getScene().getWindow());
                            err.showAndWait();
                        }
                    });
                }
                @Override protected void updateItem(Void it, boolean empty) {
                    super.updateItem(it, empty);
                    setGraphic(empty ? null : btnRestore);
                }
            });

            delTable.getColumns().addAll(nameCol, loaiCol, actCol);
            delTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            delTable.getItems().setAll(recentlyDeleted);

            dialog.getDialogPane().setContent(delTable);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.setResizable(true);
            dialog.showAndWait();
        });

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
        // Thiết lập hành vi khi chọn thực đơn bên trái
        setupTableSelectionBehavior();
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
        var source = javafx.collections.FXCollections.observableArrayList(list);
        ThucDonDAO.ThucDonView placeholder = new ThucDonDAO.ThucDonView(null, "", "");
        source.add(placeholder);

        tableThucDon.setItems(source);
        tableThucDon.comparatorProperty().addListener((obs, old, nw) -> {
            Comparator<ThucDonDAO.ThucDonView> comp = nw;
            javafx.application.Platform.runLater(() -> {
                if (comp != null) {
                    javafx.collections.FXCollections.sort(source, (a, b) -> {
                        if (a == placeholder && b == placeholder) return 0;
                        if (a == placeholder) return 1;
                        if (b == placeholder) return -1;
                        return comp.compare(a, b);
                    });
                }
            });
        });
    }

    private void setupTableSelectionBehavior() {
        // Khi chọn 1 thực đơn bên trái, điền chi tiết sang bên phải
        tableThucDon.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) return;
            txtTenThucDon.setText(newV.tenTD);
            List<String> loai = new ThucDonDAO().layLoaiMonTheoThucDon(newV.tenTD);
            selectedFoods.clear();
            if (loai != null) {
                for (String tenLoai : loai) {
                    String ma = loaiNameToMa.get(tenLoai);
                    int qty = 0;
                    if (ma != null) {
                        var listMon = monAnDAO.layMonAnTheoLoai(ma);
                        qty = listMon == null ? 0 : listMon.size();
                    }
                    selectedFoods.add(new FoodItem(tenLoai, getFoodIcon(tenLoai), qty));
                }
            }
            capNhatBoxChonThucAn();
        });
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert a = new Alert(type, message);
        a.showAndWait();
    }

    // Đồng bộ danh sách loại món ăn với DB (bảng LoaiMonAn.tenLoaiMon)
    private void loadLoaiMonAnFromDB() {
        try {
            var ds = loaiMonDAO.layTatCaLoaiMon();
            cbLoaiMonAn.getItems().clear();
            loaiNameToMa.clear();
            for (var lm : ds) {
                if (lm != null && lm.getTenLoaiMon() != null && !lm.getTenLoaiMon().isBlank()) {
                    cbLoaiMonAn.getItems().add(lm.getTenLoaiMon());
                    loaiNameToMa.put(lm.getTenLoaiMon(), lm.getMaLoaiMon());
                }
            }
        } catch (Exception ex) {
            System.err.println("Không thể tải danh sách loại món ăn từ DB: " + ex.getMessage());
        }
    }
}
