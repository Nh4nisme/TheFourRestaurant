package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.view.components.GiaoDienThucThe;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienKhuyenMai extends GiaoDienThucThe {

    private final KhuyenMaiController controller;
    private final GiaoDienChiTietKhuyenMai chiTietKhuyenMaiPane;
    private final TableView<KhuyenMai> typedTable; // The correctly typed reference

    public GiaoDienKhuyenMai() {
        super("Khuyến mãi", new GiaoDienChiTietKhuyenMai());
        this.controller = new KhuyenMaiController();

        // 1. Get the detail pane instance
        this.chiTietKhuyenMaiPane = (GiaoDienChiTietKhuyenMai) this.chiTietNode;

        // 2. Safely cast the generic table from the superclass to a typed one
        this.typedTable = (TableView<KhuyenMai>) this.tableChinh;

        // 3. Add a listener to the typed table's selection model
        this.typedTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                chiTietKhuyenMaiPane.hienThiChiTiet(newSelection);
            }
        });

        // Initial data load
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
            String maLoaiKM = cellData.getValue().getMaLoaiKM();
            return new SimpleStringProperty(maLoaiKM);
        });

        TableColumn<KhuyenMai, String> giaTriCol = new TableColumn<>("Giá Trị");
        giaTriCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            String giaTri = "";
            if (km.getTyLe() != null) {
                giaTri = km.getTyLe() + " %";
            } else if (km.getSoTien() != null) {
                giaTri = km.getSoTien().toPlainString() + " VND";
            } else if (km.getMaMonTang() != null) {
                giaTri = "Tặng " + km.getMaMonTang();
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

        return table;
    }

    private void refreshTable() {
        List<KhuyenMai> data = controller.getAllKhuyenMai();
        // Use the typed reference to set items
        this.typedTable.setItems(FXCollections.observableArrayList(data));
        
        // Clear details if the table is empty or no selection
        if (data.isEmpty() || this.typedTable.getSelectionModel().getSelectedItem() == null) {
            chiTietKhuyenMaiPane.hienThiChiTiet(null);
        }
    }
}
