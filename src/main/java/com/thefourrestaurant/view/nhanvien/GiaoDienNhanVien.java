package com.thefourrestaurant.view.nhanvien;

import com.thefourrestaurant.controller.NhanVienController;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.util.List;

public class GiaoDienNhanVien extends GiaoDienThucThe {

    private final NhanVienController controller = new NhanVienController();
    private final GiaoDienChiTietNhanVien gdChiTiet;
    private TableView<NhanVien> table;
    private ObservableList<NhanVien> danhSachGoc;
    private ObservableList<NhanVien> danhSachHienThi;

    public GiaoDienNhanVien() {
        super("Nhân viên", new GiaoDienChiTietNhanVien());
        gdChiTiet = (GiaoDienChiTietNhanVien) getChiTietNode();
        khoiTaoGiaoDien();
        lamMoiDuLieu();
        khoiTaoSuKien();
        adjustSplitPaneDivider();
    }
    
    private void adjustSplitPaneDivider() {
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof javafx.scene.control.SplitPane) {
                javafx.scene.control.SplitPane splitPane = (javafx.scene.control.SplitPane) node;
                splitPane.setDividerPositions(0.70);
                break;
            }
        }
    }

    @Override
    protected TableView<?> taoBangChinh() {
        table = new TableView<>();

        TableColumn<NhanVien, String> colMa = new TableColumn<>("Mã NV");
        colMa.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getMaNV()));

        TableColumn<NhanVien, String> colHoTen = new TableColumn<>("Họ tên");
        colHoTen.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getHoTen()));

        TableColumn<NhanVien, String> colNgaySinh = new TableColumn<>("Ngày sinh");
        colNgaySinh.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getNgaySinh() == null ? "" : cd.getValue().getNgaySinh().toLocalDate().toString()
        ));

        TableColumn<NhanVien, String> colGioiTinh = new TableColumn<>("Giới tính");
        colGioiTinh.setCellValueFactory(cd -> {
            String gioiTinh = cd.getValue().getGioiTinh();
            if ("Nu".equals(gioiTinh)) {
                return new SimpleStringProperty("Nữ");
            }
            return new SimpleStringProperty(gioiTinh);
        });

        TableColumn<NhanVien, String> colSDT = new TableColumn<>("SĐT");
        colSDT.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSoDienThoai()));

        TableColumn<NhanVien, String> colLuong = new TableColumn<>("Lương");
        colLuong.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getLuong() == null ? "" : cd.getValue().getLuong().toPlainString()
        ));

        TableColumn<NhanVien, String> colVaiTro = new TableColumn<>("Vai trò");
        colVaiTro.setCellValueFactory(cd -> {
            String vaiTro = "";
            if (cd.getValue().getMaTK() != null && cd.getValue().getMaTK().getVaiTro() != null) {
                vaiTro = cd.getValue().getMaTK().getVaiTro().getTenVaiTro();
                vaiTro = GiaoDienChiTietNhanVien.formatVaiTro(vaiTro);
            }
            return new SimpleStringProperty(vaiTro);
        });

        TableColumn<NhanVien, String> colMaTK = new TableColumn<>("Mã TK");
        colMaTK.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getMaTK() != null ? cd.getValue().getMaTK().getMaTK() : ""
        ));

        table.getColumns().addAll(colMa, colHoTen, colNgaySinh, colGioiTinh, colSDT, colLuong, colVaiTro, colMaTK);
        return table;
    }

    @Override
    protected void lamMoiDuLieu() {
        List<NhanVien> ds = controller.layDanhSachNhanVien();
        danhSachGoc = FXCollections.observableArrayList(ds);
        danhSachHienThi = FXCollections.observableArrayList(ds);
        table.setItems(danhSachHienThi);
    }

    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            table.setItems(danhSachGoc);
            return;
        }
        String q = tuKhoa.toLowerCase();
        ObservableList<NhanVien> filtered = danhSachGoc.filtered(nv ->
                (nv.getMaNV() != null && nv.getMaNV().toLowerCase().contains(q)) ||
                (nv.getHoTen() != null && nv.getHoTen().toLowerCase().contains(q)) ||
                (nv.getSoDienThoai() != null && nv.getSoDienThoai().toLowerCase().contains(q))
        );
        table.setItems(filtered);
    }

    private void khoiTaoSuKien() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            gdChiTiet.hienThi(newV);
        });
        
        gdChiTiet.getBtnLuu().setOnAction(e -> {
            NhanVien selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn nhân viên để lưu");
                a.showAndWait();
                return;
            }

            String ma = gdChiTiet.getTxtMaNV().getText().trim();
            String hoTen = gdChiTiet.getTxtHoTen().getText().trim();
            java.sql.Date ngay = gdChiTiet.getDtpNgaySinh().getValue() == null ? null : java.sql.Date.valueOf(gdChiTiet.getDtpNgaySinh().getValue());
            String gioiTinh = gdChiTiet.getGioiTinhValue(); 
            String sdt = gdChiTiet.getTxtSDT().getText().trim();
            BigDecimal luong = null;
            try {
                String l = gdChiTiet.getTxtLuong().getText().trim();
                if (!l.isEmpty()) luong = new BigDecimal(l);
            } catch (Exception ex) { }

            NhanVien nv = new NhanVien(ma, hoTen, ngay, gioiTinh, sdt, luong, selected.getMaTK());
            boolean ok = controller.capNhatNhanVien(nv, gdChiTiet.getSelectedImageFile());
            if (ok) {
                Alert info = new Alert(Alert.AlertType.INFORMATION, "Thông tin nhân viên đã được cập nhật");
                info.showAndWait();
                lamMoiDuLieu();
            } else {
                Alert err = new Alert(Alert.AlertType.ERROR, "Cập nhật thất bại");
                err.showAndWait();
            }
        });
    }
}