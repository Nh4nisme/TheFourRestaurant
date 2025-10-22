package com.thefourrestaurant.view.taikhoan;

import com.thefourrestaurant.controller.TaiKhoanController;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;

import java.util.List;

public class GiaoDienTaiKhoan extends GiaoDienThucThe {

    // View giữ tham chiếu tới controller
    private final TaiKhoanController controller;
    private final GiaoDienChiTietTaiKhoan gdChiTietTK;
    private TableView<TaiKhoan> table;

    public GiaoDienTaiKhoan() {
        super("Tài khoản", new GiaoDienChiTietTaiKhoan());
        controller = new TaiKhoanController();
        gdChiTietTK = (GiaoDienChiTietTaiKhoan) getChiTietNode();
        khoiTaoGiaoDien();
        napDanhSachVaiTro();
        khoiTaoSuKien();
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

        TableColumn<TaiKhoan, String> colMatKhau = new TableColumn<>("Mật khẩu");
        colMatKhau.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getMatKhau())
        );

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

                    if (xacNhan("Xác nhận xóa", "Bạn có chắc muốn xóa tài khoản: " + tk.getTenDN() + " ?")) {
                        boolean ok = controller.xoaTaiKhoan(tk.getMaTK());
                        if (ok) {
                            getTableView().getItems().remove(tk);
                            hienThongBao("Đã xóa tài khoản!");
                        } else {
                            hienThongBao("Không thể xóa tài khoản này!", Alert.AlertType.ERROR);
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

        table.getColumns().addAll(colMaTK, colTenDN, colMatKhau, colVaiTro, colHanhDong);
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

    private void khoiTaoSuKien() {
        // ==== Tạo mới tài khoản ====
        gdChiTietTK.getBtnTaoMoi().setOnAction(e -> {
            String maTK = gdChiTietTK.getTxtMaTK().getText().trim();
            String tenDN = gdChiTietTK.getTxtTenDangNhap().getText().trim();
            String matKhau = gdChiTietTK.getTxtMatKhau().getText().trim();
            VaiTro vt = gdChiTietTK.getCboVaiTro().getValue();

            String result = controller.taoTaiKhoan(maTK, tenDN, matKhau, vt);
            if (result.equals("OK")) {
                gdChiTietTK.Clear(); // chỉ xóa form khi tạo mới thành công
                refreshBangChinh();
                hienThongBao("Tạo tài khoản thành công!", Alert.AlertType.INFORMATION);
            } else {
                hienThongBao(result, Alert.AlertType.WARNING);
            }
        });

        // ==== Cập nhật tài khoản ====
        gdChiTietTK.getBtnLuu().setOnAction(e -> {
            String maTK = gdChiTietTK.getTxtMaTK().getText().trim();
            String tenDN = gdChiTietTK.getTxtTenDangNhap().getText().trim();
            String matKhau = gdChiTietTK.getTxtMatKhau().getText().trim();
            VaiTro vt = gdChiTietTK.getCboVaiTro().getValue();

            String result = controller.capNhatTaiKhoan(maTK, tenDN, matKhau, vt);
            if (result.equals("OK")) {
                refreshBangChinh();
                hienThongBao("Cập nhật thành công!", Alert.AlertType.INFORMATION);
            } else {
                hienThongBao(result, Alert.AlertType.WARNING);
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























