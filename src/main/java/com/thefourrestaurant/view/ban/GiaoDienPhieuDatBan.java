package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.controller.PhieuDatBanController;
import com.thefourrestaurant.controller.TaiKhoanController;
import com.thefourrestaurant.model.*;
import com.thefourrestaurant.view.components.GiaoDienThucThe;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class GiaoDienPhieuDatBan extends GiaoDienThucThe {
    private final PhieuDatBanController controller;
    private final GiaoDienChiTietPhieuDatBan gdChiTietPhieuDatBan;
    private TableView<PhieuDatBan> table;
    private ObservableList<PhieuDatBan> danhSachGoc;
    private ObservableList<PhieuDatBan> danhSachHienThi;

    public GiaoDienPhieuDatBan() {
        super("Phiếu đặt bàn", new GiaoDienChiTietPhieuDatBan());
        controller = new PhieuDatBanController();
        gdChiTietPhieuDatBan = (GiaoDienChiTietPhieuDatBan) getChiTietNode();
        khoiTaoGiaoDien();
        lamMoiDuLieu();
    }

    @Override
    protected TableView<?> taoBangChinh() {
        table = new TableView<>();

        TableColumn<PhieuDatBan, String> colMaPDB = new TableColumn<>("Mã PDB");
        colMaPDB.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaPDB()));

        TableColumn<PhieuDatBan, String> colNgayTao = new TableColumn<>("Ngày tạo phiếu");
        colNgayTao.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNgayTao() == null ? "" : cell.getValue().getNgayTao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        TableColumn<PhieuDatBan, String> colNgayDat = new TableColumn<>("Ngày đặt bàn");
        colNgayDat.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNgayDat() == null ? "" : cell.getValue().getNgayDat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        TableColumn<PhieuDatBan, String> colTenKH = new TableColumn<>("Tên khách đặt bàn");
        colTenKH.setCellValueFactory(cell -> {
            KhachHang kh = cell.getValue().getKhachHang();
            return new SimpleStringProperty(kh == null ? "" : kh.getHoTen());
        });

        TableColumn<PhieuDatBan, String> colTenNV = new TableColumn<>("Tên nhân viên");
        colTenNV.setCellValueFactory(cell ->{
            NhanVien nv = cell.getValue().getNhanVien();
            return new SimpleStringProperty(nv == null ? "" : nv.getHoTen());
        });

        TableColumn<PhieuDatBan, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTrangThai()));

        TableColumn<PhieuDatBan, Void> colHanhDong = new TableColumn<>("Hành động");
        colHanhDong.setCellFactory(col -> new TableCell<>() {
            private final Button btnXoa = new Button("🗑");

            {
                btnXoa.setStyle("-fx-background-color: red; -fx-cursor: hand; -fx-font-size: 14;");
                btnXoa.setOnAction(event -> {
                    PhieuDatBan pdb = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) btnXoa.getScene().getWindow();

                    // Hộp thoại xác nhận
                    if (xacNhan(stage, "Bạn có chắc muốn xóa phiếu đặt bàn: " + pdb.getMaPDB() + " ?")) {

                        boolean ok = controller.xoaPhieuDatBan(pdb.getMaPDB()); // gọi DAO/controller xóa

                        if (ok) {
                            getTableView().getItems().remove(pdb);
                            hienThongBao(stage,"Đã xóa phiếu đặt bàn!");
                        } else {
                            hienThongBao(stage,"Không thể xóa phiếu đặt bàn này!", Alert.AlertType.ERROR);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnXoa);
            }
        });

        table.getColumns().addAll(colMaPDB, colNgayTao, colNgayDat, colTenKH, colTenNV, colTrangThai, colHanhDong);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<PhieuDatBan> dsPDB = controller.layDanhSachPDB();
        table.getItems().addAll(dsPDB);

        table.setRowFactory(t -> {
            TableRow<PhieuDatBan> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(!row.isEmpty()){
                    PhieuDatBan ds = row.getItem();
                    hienThiChiTiet(ds);
                }
            });
            return row;
        });
        return table;
    }

    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        if (danhSachGoc == null || danhSachGoc.isEmpty()) return;
        if (tuKhoa.isEmpty()) {
            table.setItems(danhSachGoc);
            return;
        }

        String lowerKey = tuKhoa.toLowerCase().trim();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ngayTimKiem = null;

        try {
            ngayTimKiem = LocalDate.parse(tuKhoa, fmt);
        } catch (DateTimeParseException ignored) {
            // không làm gì, có thể người dùng đang tìm bằng text
        }

        LocalDate finalNgayTimKiem = ngayTimKiem;
        ObservableList<PhieuDatBan> ketQua = danhSachGoc.filtered(pdb -> {
            boolean match = false;

            // So sánh theo chuỗi text
            if(pdb.getMaPDB() != null && pdb.getMaPDB().toLowerCase().contains(lowerKey))
                match = true;
            if(pdb.getKhachHang().getHoTen() != null && pdb.getKhachHang().getHoTen().toLowerCase().contains(lowerKey))
                match = true;
            if(pdb.getNhanVien().getHoTen()  != null && pdb.getNhanVien().getHoTen().toLowerCase().contains(lowerKey))
                match = true;
            if(pdb.getTrangThai() != null && pdb.getTrangThai().toLowerCase().contains(lowerKey))
                match = true;

            // So sánh theo ngày
            LocalDateTime ngayDat = pdb.getNgayDat();
            if (ngayDat != null) {
                // Nếu người dùng nhập đúng ngày dd/MM/yyyy
                if (ngayDat.equals(finalNgayTimKiem))
                    match = true;

                // Hoặc nếu chuỗi ngày chứa text tìm kiếm (ví dụ: 10/2025)
                String ngayStr = ngayDat.format(fmt).toLowerCase();
                if (ngayStr.contains(lowerKey))
                    match = true;
            }

            return match;
        });

        table.setItems(ketQua);
    }

    @Override
    protected void lamMoiDuLieu() {
        danhSachGoc = FXCollections.observableArrayList(new PhieuDatBanController().layDanhSachPDB());
        danhSachHienThi = FXCollections.observableArrayList(danhSachGoc);
        table.setItems(danhSachHienThi);
    }

    public void hienThiChiTiet(PhieuDatBan pdb){gdChiTietPhieuDatBan.hienThiThongTin(pdb);}
}
