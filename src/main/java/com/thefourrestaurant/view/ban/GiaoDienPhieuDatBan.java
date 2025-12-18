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

    public GiaoDienPhieuDatBan() {
        super("Phiếu đặt bàn", new GiaoDienChiTietPhieuDatBan());

        controller = new PhieuDatBanController();
        gdChiTietPhieuDatBan = (GiaoDienChiTietPhieuDatBan) getChiTietNode();
        khoiTaoGiaoDien();
        khoiTaoBoLocNgayCuThe();
        khoiTaoBoLocTimKiem();
        lamMoiDuLieu();
    }

    @Override
    protected TableView<?> taoBangChinh() {
        table = new TableView<>();

        TableColumn<PhieuDatBan, String> colMaPDB = new TableColumn<>("Mã PDB");
        colMaPDB.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMaPDB())
        );

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TableColumn<PhieuDatBan, String> colNgayTao = new TableColumn<>("Ngày tạo");
        colNgayTao.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getNgayTao() == null ? "" :
                                c.getValue().getNgayTao().format(fmt))
        );

        TableColumn<PhieuDatBan, String> colNgayDat = new TableColumn<>("Ngày đặt");
        colNgayDat.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getNgayDat() == null ? "" :
                                c.getValue().getNgayDat().format(fmt))
        );

        TableColumn<PhieuDatBan, String> colTenKH = new TableColumn<>("Khách hàng");
        colTenKH.setCellValueFactory(c -> {
            KhachHang kh = c.getValue().getKhachHang();
            return new SimpleStringProperty(kh == null ? "" : kh.getHoTen());
        });

        TableColumn<PhieuDatBan, String> colTenNV = new TableColumn<>("Nhân viên");
        colTenNV.setCellValueFactory(c -> {
            NhanVien nv = c.getValue().getNhanVien();
            return new SimpleStringProperty(nv == null ? "" : nv.getHoTen());
        });

        TableColumn<PhieuDatBan, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTrangThai())
        );

        TableColumn<PhieuDatBan, Void> colHanhDong = taoCotXoa();

        table.getColumns().addAll(
                colMaPDB, colNgayTao, colNgayDat,
                colTenKH, colTenNV, colTrangThai, colHanhDong
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setRowFactory(t -> {
            TableRow<PhieuDatBan> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                    hienThiChiTiet(row.getItem());
                }
            });
            return row;
        });

        return table;
    }

    private TableColumn<PhieuDatBan, Void> taoCotXoa() {
        TableColumn<PhieuDatBan, Void> col = new TableColumn<>("Hành động");

        col.setCellFactory(c -> new TableCell<>() {
            private final Button btnXoa = new Button("🗑");

            {
                btnXoa.setOnAction(e -> {
                    PhieuDatBan pdb = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) btnXoa.getScene().getWindow();

                    if (xacNhan(stage, "Xóa phiếu " + pdb.getMaPDB() + "?")) {
                        if (controller.xoaPhieuDatBan(pdb.getMaPDB())) {
                            table.getItems().remove(pdb);
                            hienThongBao(stage, "Đã xóa phiếu đặt bàn");
                        } else {
                            hienThongBao(stage,
                                    "Không thể xóa phiếu này",
                                    Alert.AlertType.ERROR);
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

        return col;
    }

    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        if (danhSachGoc == null) return;

        if (tuKhoa == null || tuKhoa.isBlank()) {
            table.setItems(danhSachGoc);
            return;
        }

        String key = tuKhoa.toLowerCase().trim();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate ngayTim = null;
        try {
            ngayTim = LocalDate.parse(key, fmt);
        } catch (Exception ignored) {}

        LocalDate finalNgayTim = ngayTim;

        table.setItems(danhSachGoc.filtered(pdb -> {

            if (pdb.getMaPDB() != null &&
                    pdb.getMaPDB().toLowerCase().contains(key))
                return true;

            if (pdb.getTrangThai() != null &&
                    pdb.getTrangThai().toLowerCase().contains(key))
                return true;

            KhachHang kh = pdb.getKhachHang();
            if (kh != null && kh.getHoTen() != null &&
                    kh.getHoTen().toLowerCase().contains(key))
                return true;

            NhanVien nv = pdb.getNhanVien();
            if (nv != null && nv.getHoTen() != null &&
                    nv.getHoTen().toLowerCase().contains(key))
                return true;

            if (finalNgayTim != null && pdb.getNgayDat() != null &&
                    pdb.getNgayDat().toLocalDate().equals(finalNgayTim))
                return true;

            return false;
        }));
    }

    @Override
    protected void lamMoiDuLieu() {
        danhSachGoc = FXCollections.observableArrayList(
                controller.layDanhSachPDB()
        );
        table.setItems(danhSachGoc);
    }

    private void hienThiChiTiet(PhieuDatBan pdb) {
        gdChiTietPhieuDatBan.hienThiThongTin(pdb);
    }
}

