package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.controller.KhachHangController;
import com.thefourrestaurant.controller.PhieuDatBanController;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.components.GiaoDienThucThe;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienPhieuDatBan extends GiaoDienThucThe {
    private final PhieuDatBanController controller;
    private final GiaoDienChiTietPhieuDatBan gdChiTietPhieuDatBan;
    private TableView<PhieuDatBan> table;

    public GiaoDienPhieuDatBan() {
        super("Phiếu đặt bàn", new GiaoDienChiTietPhieuDatBan());
        controller = new PhieuDatBanController();
        gdChiTietPhieuDatBan = (GiaoDienChiTietPhieuDatBan) getChiTietNode();
        khoiTaoGiaoDien();
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

        table.getColumns().addAll(colMaPDB, colNgayTao, colNgayDat, colTenKH, colTenNV, colTrangThai);
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

    public void hienThiChiTiet(PhieuDatBan pdb){gdChiTietPhieuDatBan.hienThiThongTin(pdb);}
}
