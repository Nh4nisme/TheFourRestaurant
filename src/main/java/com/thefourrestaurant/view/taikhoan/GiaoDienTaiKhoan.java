package com.thefourrestaurant.view.taikhoan;

import com.thefourrestaurant.controller.HoaDonController;
import com.thefourrestaurant.controller.TaiKhoanController;
import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class GiaoDienTaiKhoan extends GiaoDienThucThe {

    // View giữ tham chiếu tới controller
    private final TaiKhoanController controller;
    private final GiaoDienChiTietTaiKhoan gdChiTietTK;
    private TableView<TaiKhoan> table;
    private ObservableList<TaiKhoan> danhSachGoc;
    private ObservableList<TaiKhoan> danhSachHienThi;

    public GiaoDienTaiKhoan() {
        super("Tài khoản", new GiaoDienChiTietTaiKhoan());
        controller = new TaiKhoanController();
        gdChiTietTK = (GiaoDienChiTietTaiKhoan) getChiTietNode();
        khoiTaoGiaoDien();
        khoiTaoBoLocTimKiem();
        napDanhSachVaiTro();
        khoiTaoSuKien();
        lamMoiDuLieu();
    }

    @Override
    protected TableView<TaiKhoan> taoBangChinh() {
        table = new TableView<>();

        TableColumn<TaiKhoan, String> colMaTK = new TableColumn<>("Mã TK");
        colMaTK.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getMaTK())
        );

        TableColumn<TaiKhoan, String> colTenDN = new TableColumn<>("Tên đăng nhập");
        colTenDN.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTenDN())
        );

        // Mật khẩu không hiển thị trong bảng (bảo mật)

        TableColumn<TaiKhoan, String> colVaiTro = new TableColumn<>("Vai trò");
        colVaiTro.setCellValueFactory(cell -> {
            VaiTro vt = cell.getValue().getVaiTro();
            return new SimpleStringProperty(vt != null ? vt.getTenVaiTro() : "");
        });

        TableColumn<TaiKhoan, Void> colHanhDong = new TableColumn<>("Hành động");
        colHanhDong.setCellFactory(col -> new TableCell<>() {
            private final Button btnXoa = new Button("🗑");

            {
                btnXoa.setOnAction(event -> {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) btnXoa.getScene().getWindow();

                    if (xacNhan(stage, "Bạn có chắc muốn xóa tài khoản: " + tk.getTenDN() + " ?")) {
                        boolean ok = controller.xoaTaiKhoan(tk.getMaTK());
                        if (ok) {
                            getTableView().getItems().remove(tk);
                            hienThongBao(stage,"Đã xóa tài khoản!");
                        } else {
                            hienThongBao(stage,"Không thể xóa tài khoản này!", Alert.AlertType.ERROR);
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

        table.getColumns().addAll(colMaTK, colTenDN, colVaiTro, colHanhDong);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // View gọi Controller để lấy danh sách dữ liệu từ DAO
        List<TaiKhoan> dsTaiKhoan = controller.layDanhSachTaiKhoan();
        table.getItems().setAll(dsTaiKhoan);

        //Sự kiện chọn dòng
        table.setRowFactory(t ->{
            TableRow<TaiKhoan> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(!row.isEmpty()) {
                    TaiKhoan tk = row.getItem();
                    hienThiChiTiet(tk);
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

        String lowerKey = tuKhoa.toLowerCase();
        ObservableList<TaiKhoan> ketQua = danhSachGoc.filtered(
                tk -> tk.getMaTK().toLowerCase().contains(lowerKey)
                        || tk.getTenDN().toLowerCase().contains(lowerKey)
                        || tk.getVaiTro().getTenVaiTro().toLowerCase().contains(lowerKey)
        );
        table.setItems(ketQua);
    }

    @Override
    protected void lamMoiDuLieu() {
        danhSachGoc = FXCollections.observableArrayList(new TaiKhoanController().layDanhSachTaiKhoan());
        danhSachHienThi = FXCollections.observableArrayList(danhSachGoc);
        table.setItems(danhSachHienThi);
    }

    private void khoiTaoSuKien() {
        // ==== Tạo mới tài khoản ====
//        gdChiTietTK.getBtnTaoMoi().setOnAction(e -> {
//            String maTK = gdChiTietTK.getTxtMaTK().getText().trim();
//            String tenDN = gdChiTietTK.getTxtTenDangNhap().getText().trim();
//            String matKhau = gdChiTietTK.getTxtMatKhau().getText().trim();
//            VaiTro vt = gdChiTietTK.getCboVaiTro().getValue();
//
//            String result = controller.taoTaiKhoan(maTK, tenDN, matKhau, vt);
//            Stage  stage = (Stage) gdChiTietTK.getScene().getWindow();
//
//            if (result.equals("OK")) {
//                gdChiTietTK.Clear(); // chỉ xóa form khi tạo mới thành công
//                refreshBangChinh();
//                hienThongBao(stage,"Tạo tài khoản thành công!", Alert.AlertType.INFORMATION);
//            } else {
//                hienThongBao(stage,result, Alert.AlertType.WARNING);
//            }
//        });

        // ==== Cập nhật tài khoản ====
        gdChiTietTK.getBtnLuu().setOnAction(e -> {
            String maTK = gdChiTietTK.getTxtMaTK().getText().trim();
            String tenDN = gdChiTietTK.getTxtTenDangNhap().getText().trim();
            String matKhau = gdChiTietTK.getTxtMatKhau().getText().trim();
            VaiTro vt = gdChiTietTK.getCboVaiTro().getValue();

            String result = controller.capNhatTaiKhoan(maTK, tenDN, matKhau, vt);
            Stage  stage = (Stage) gdChiTietTK.getScene().getWindow();

            if (result.equals("OK")) {
                refreshBangChinh();
                hienThongBao(stage,"Cập nhật thành công!", Alert.AlertType.INFORMATION);
            } else {
                hienThongBao(stage, result, Alert.AlertType.WARNING);
            }
        });
    }

    private void hienThiChiTiet(TaiKhoan tk) {
        gdChiTietTK.hienThiThongTin(tk);
    }

    private void napDanhSachVaiTro() {
        List<VaiTro> dsVaiTro = controller.layDanhSachVaiTro();
        gdChiTietTK.getCboVaiTro().getItems().setAll(dsVaiTro);
    }

    private void refreshBangChinh() {
        if (table != null) {
            table.getItems().setAll(controller.layDanhSachTaiKhoan());
        }
    }
}