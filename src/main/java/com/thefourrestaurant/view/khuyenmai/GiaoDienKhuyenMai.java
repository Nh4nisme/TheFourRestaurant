package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.GiaoDienThucThe;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienKhuyenMai extends GiaoDienThucThe {

    private final KhuyenMaiController controller;
    private final GiaoDienChiTietKhuyenMai chiTietKhuyenMaiPane;
    private final TableView<KhuyenMai> typedTable;

    public GiaoDienKhuyenMai() {
        super("Khuyến mãi", new GiaoDienChiTietKhuyenMai());
        this.controller = new KhuyenMaiController();

        this.chiTietKhuyenMaiPane = (GiaoDienChiTietKhuyenMai) this.chiTietNode;
        this.typedTable = (TableView<KhuyenMai>) this.tableChinh;

        this.typedTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            chiTietKhuyenMaiPane.hienThiChiTiet(newSelection);
        });

        ButtonSample themButton = new ButtonSample("Thêm Khuyến Mãi", "", 35, 14, 3);
        themButton.setOnAction(e -> {
            if (controller.themMoiKhuyenMai()) {
                refreshTable();
            }
        });

        // Safely get the toolbar by finding the parent of the title label
        if (this.lblTieuDe != null && this.lblTieuDe.getParent() instanceof HBox) {
            HBox toolbar = (HBox) this.lblTieuDe.getParent();
            // Add the button after the title label, before the spacer region
            if (toolbar.getChildren().size() > 1) {
                toolbar.getChildren().add(1, themButton);
            } else {
                toolbar.getChildren().add(themButton);
            }
        }

        refreshTable();
    }

    @Override
    protected TableView<?> taoBangChinh() {
        TableView<KhuyenMai> table = new TableView<>();

        TableColumn<KhuyenMai, String> maKMCol = new TableColumn<>("Mã KM");
        maKMCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKM()));

        TableColumn<KhuyenMai, String> moTaCol = new TableColumn<>("Mô Tả");
        moTaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMoTa()));
        moTaCol.setPrefWidth(250);

        TableColumn<KhuyenMai, String> loaiKMCol = new TableColumn<>("Loại KM");
        loaiKMCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            String tenLoai = (km.getLoaiKhuyenMai() != null) ? km.getLoaiKhuyenMai().getTenLoaiKM() : "";
            return new SimpleStringProperty(tenLoai);
        });

        TableColumn<KhuyenMai, String> giaTriCol = new TableColumn<>("Giá Trị");
        giaTriCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            String giaTri = "";
            if (km.getTyLe() != null) {
                giaTri = km.getTyLe() + " %";
            } else if (km.getSoTien() != null) {
                giaTri = km.getSoTien().toPlainString() + " VND";
            } else if (km.getMonAnTang() != null) {
                giaTri = "Tặng " + km.getMonAnTang().getTenMon();
            } else if (km.getMonAnApDung() != null) {
                giaTri = "Giảm cho " + km.getMonAnApDung().getTenMon();
            }
            return new SimpleStringProperty(giaTri);
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TableColumn<KhuyenMai, String> ngayBDCol = new TableColumn<>("Ngày Bắt Đầu");
        ngayBDCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getNgayBatDau();
            return new SimpleStringProperty(date == null ? "" : date.format(formatter));
        });

        TableColumn<KhuyenMai, String> ngayKTCol = new TableColumn<>("Ngày Kết Thúc");
        ngayKTCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getNgayKetThuc();
            return new SimpleStringProperty(date == null ? "" : date.format(formatter));
        });

        table.getColumns().addAll(maKMCol, moTaCol, loaiKMCol, giaTriCol, ngayBDCol, ngayKTCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setRowFactory(tv -> {
            TableRow<KhuyenMai> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Sửa");
            editItem.setOnAction(event -> {
                KhuyenMai selectedItem = row.getItem();
                if (selectedItem != null && controller.tuyChinhKhuyenMai(selectedItem)) {
                    refreshTable();
                }
            });

            MenuItem deleteItem = new MenuItem("Xóa");
            deleteItem.setOnAction(event -> {
                KhuyenMai selectedItem = row.getItem();
                if (selectedItem != null && controller.xoaKhuyenMai(selectedItem)) {
                    refreshTable();
                }
            });

            contextMenu.getItems().addAll(editItem, new SeparatorMenuItem(), deleteItem);

            row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                row.setContextMenu(isNowEmpty ? null : contextMenu);
            });
            return row;
        });

        return table;
    }

    private void refreshTable() {
        List<KhuyenMai> data = controller.layTatCaKhuyenMai();
        this.typedTable.setItems(FXCollections.observableArrayList(data));
        
        if (data.isEmpty() || this.typedTable.getSelectionModel().getSelectedItem() == null) {
            chiTietKhuyenMaiPane.hienThiChiTiet(null);
        }
    }
}
