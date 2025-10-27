package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.controller.MonAnController;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GiaoDienMonAn extends VBox {

    private final String maLoaiMon;
    private final String tenLoaiMon;
    private final MonAnController controller;

    private List<MonAn> danhSachMonAnGoc; // Danh sách món ăn gốc, không bị lọc
    private List<MonAn> danhSachMonAnHienThi; // Danh sách món ăn đang hiển thị (đã lọc/sắp xếp)

    private final VBox dsMonAnContainer = new VBox(20);
    private final GridPane gridViewPane = new GridPane();
    private final TableView<MonAn> listViewPane = new TableView<>();
    private final int soCotMoiHang = 8;

    public GiaoDienMonAn(String maLoaiMon, String tenLoaiMon) {
        this.maLoaiMon = maLoaiMon;
        this.tenLoaiMon = tenLoaiMon;
        this.controller = new MonAnController();

        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        contentPane.add(createTopBar(), 0, 0);
        contentPane.add(createMiddleBar(), 0, 1);

        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.CENTER);
        khungDuoi.setPadding(new Insets(20));
        GridPane.setMargin(khungDuoi, new Insets(10, 10, 10, 10));
        contentPane.add(khungDuoi, 0, 2);
        GridPane.setHgrow(khungDuoi, Priority.ALWAYS);
        GridPane.setVgrow(khungDuoi, Priority.ALWAYS);

        dsMonAnContainer.setStyle("-fx-background-color: #F0F2F3; -fx-background-radius: 10;");
        dsMonAnContainer.setAlignment(Pos.TOP_CENTER);
        dsMonAnContainer.setPadding(new Insets(20));
        khungDuoi.getChildren().add(dsMonAnContainer);
        VBox.setVgrow(dsMonAnContainer, Priority.ALWAYS);

        setupGridView();
        setupListView();

        dsMonAnContainer.getChildren().add(createGridViewContent());

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        this.getChildren().add(contentPane);
        refreshViews();
    }

    private VBox createTopBar() {
        Label duongDan = new Label("Quản Lý > Món Ăn > " + tenLoaiMon);
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungTren = new VBox(duongDan);
        khungTren.setStyle("-fx-background-color: #673E1F;");
        khungTren.setAlignment(Pos.CENTER_LEFT);
        khungTren.setPadding(new Insets(0, 20, 0, 20));
        khungTren.setPrefHeight(30);
        khungTren.setMinHeight(30);
        khungTren.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(khungTren, Priority.ALWAYS);
        return khungTren;
    }

    private HBox createMiddleBar() {
        HBox khungGiua = new HBox(10);
        khungGiua.setPadding(new Insets(10, 20, 10, 20));
        khungGiua.setAlignment(Pos.CENTER_LEFT);
        khungGiua.setStyle("-fx-background-color: #1E424D;");

        ImageView iconList = new ImageView(getClass().getResource("/com/thefourrestaurant/images/icon/List.png").toExternalForm());
        ImageView iconGrid = new ImageView(getClass().getResource("/com/thefourrestaurant/images/icon/Grid.png").toExternalForm());
        iconList.setFitWidth(20); iconList.setFitHeight(20);
        iconGrid.setFitWidth(20); iconGrid.setFitHeight(20);

        ButtonSample btnList = new ButtonSample("", "", 35, 16, 3);
        ButtonSample btnGrid = new ButtonSample("", "", 35, 16, 3);
        btnList.setGraphic(iconList); btnGrid.setGraphic(iconGrid);
        btnList.setPrefSize(35, 35); btnGrid.setPrefSize(35, 35);

        btnList.setOnAction(event -> dsMonAnContainer.getChildren().setAll(listViewPane));
        btnGrid.setOnAction(event -> dsMonAnContainer.getChildren().setAll(createGridViewContent()));

        Label lblSapXep = new Label("Sắp xếp:");
        lblSapXep.setTextFill(Color.web("#E5D595"));
        lblSapXep.setFont(Font.font("System", FontWeight.BOLD, 14));

        DropDownButton btnTheoChuCai = new DropDownButton("Theo bảng chữ cái  ▼", List.of("A → Z", "Z → A"), null, 35, 16, 3);
        DropDownButton btnTheoGia = new DropDownButton("Theo giá  ▼", List.of("Tăng dần", "Giảm dần"), null, 35, 16, 3);
        ButtonSample btnApDung = new ButtonSample("Áp dụng", "", 35, 13, 3);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm...");
        txtTimKiem.setPrefWidth(300);

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);
        btnTim.setOnAction(event -> locVaCapNhatMonAn(txtTimKiem.getText()));
        txtTimKiem.setOnAction(event -> locVaCapNhatMonAn(txtTimKiem.getText())); // Trigger search on Enter key

        khungGiua.getChildren().addAll(btnList, btnGrid, lblSapXep, btnTheoChuCai, btnTheoGia, btnApDung, space, txtTimKiem, btnTim);
        return khungGiua;
    }

    private void setupGridView() {
        gridViewPane.setAlignment(Pos.CENTER);
        gridViewPane.setHgap(20);
        gridViewPane.setVgap(20);
    }

    private Node createGridViewContent() {
        VBox gridContainer = new VBox(20);
        VBox.setVgrow(gridContainer, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(gridViewPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox hopThemMoi = MonAnBox.createThemMoiBox();
        GridPane luoiThem = new GridPane();
        luoiThem.setAlignment(Pos.BASELINE_LEFT);
        luoiThem.setPadding(new Insets(0, 0, 0, 15));
        luoiThem.add(hopThemMoi, 0, 0);

        hopThemMoi.setPickOnBounds(true);
        hopThemMoi.setOnMouseClicked(event -> {
            Stage owner = (Stage) getScene().getWindow();
            if (controller.themMoiMonAn(owner, this.maLoaiMon)) {
                refreshViews();
            }
        });

        gridContainer.getChildren().addAll(luoiThem, scrollPane);
        return gridContainer;
    }

    private void setupListView() {
        VBox.setVgrow(listViewPane, Priority.ALWAYS);

        TableColumn<MonAn, String> maMonCol = new TableColumn<>("Mã món");
        maMonCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaMonAn()));

        TableColumn<MonAn, String> tenMonAnCol = new TableColumn<>("Tên món ăn");
        tenMonAnCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTenMon()));

        TableColumn<MonAn, String> donGiaCol = new TableColumn<>("Đơn giá (VND)");
        donGiaCol.setCellValueFactory(cellData -> {
            BigDecimal gia = cellData.getValue().getDonGia();
            String formattedGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(gia);
            return new SimpleStringProperty(formattedGia);
        });

        TableColumn<MonAn, String> trangThaiCol = new TableColumn<>("Trạng thái");
        trangThaiCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTrangThai()));

        listViewPane.getColumns().addAll(maMonCol, tenMonAnCol, donGiaCol, trangThaiCol);
        listViewPane.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshViews() {
        this.danhSachMonAnGoc = controller.layMonAnTheoLoai(maLoaiMon);
        this.danhSachMonAnHienThi = FXCollections.observableArrayList(danhSachMonAnGoc);
        updateViews();
    }

    private void locVaCapNhatMonAn(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            danhSachMonAnHienThi = FXCollections.observableArrayList(danhSachMonAnGoc);
        } else {
            String lowerCaseTuKhoa = tuKhoa.trim().toLowerCase();
            danhSachMonAnHienThi = danhSachMonAnGoc.stream()
                    .filter(monAn -> monAn.getTenMon().toLowerCase().contains(lowerCaseTuKhoa) ||
                                     monAn.getMaMonAn().toLowerCase().contains(lowerCaseTuKhoa))
                    .collect(Collectors.toList());
        }
        updateViews();
    }

    private void updateViews() {
        updateGridView();
        updateListView();
    }

    private void updateGridView() {
        gridViewPane.getChildren().clear();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (int i = 0; i < danhSachMonAnHienThi.size(); i++) {
            MonAn item = danhSachMonAnHienThi.get(i);
            String formattedPrice = currencyFormatter.format(item.getDonGia());
            MonAnBox hopMonAn = new MonAnBox(item.getTenMon(), formattedPrice, item.getHinhAnh());

            hopMonAn.setPickOnBounds(true);

            ContextMenu contextMenu = createContextMenu(item);
            hopMonAn.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Stage owner = (Stage) getScene().getWindow();
                    if (controller.tuyChinhMonAn(owner, item)) {
                        refreshViews();
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(hopMonAn, event.getScreenX(), event.getScreenY());
                }
            });

            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;
            gridViewPane.add(hopMonAn, col, row);
        }
    }

    private void updateListView() {
        listViewPane.setItems(FXCollections.observableArrayList(danhSachMonAnHienThi));
    }

    private ContextMenu createContextMenu(MonAn monAn) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Sửa");
        editItem.setOnAction(e -> {
            Stage owner = (Stage) getScene().getWindow();
            if (controller.tuyChinhMonAn(owner, monAn)) {
                refreshViews();
            }
        });

        MenuItem deleteItem = new MenuItem("Xóa");
        deleteItem.setOnAction(e -> {
            Stage owner = (Stage) getScene().getWindow();
            if (controller.xoaMonAn(owner, monAn)) {
                refreshViews();
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        return contextMenu;
    }
}
