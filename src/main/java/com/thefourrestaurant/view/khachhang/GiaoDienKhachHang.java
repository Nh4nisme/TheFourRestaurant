package com.thefourrestaurant.view.khachhang;

import com.thefourrestaurant.controller.KhachHangController;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.LoaiKhachHang;
import com.thefourrestaurant.view.components.GiaoDienThucThe;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienKhachHang extends GiaoDienThucThe {

    private final KhachHangController controller;
    private final GiaoDienChiTietKhachHang gdChiTietKH;
    private TableView<KhachHang> table;

    public GiaoDienKhachHang() {
        super("Khách hàng", new GiaoDienChiTietKhachHang());
        controller = new KhachHangController();
        gdChiTietKH = (GiaoDienChiTietKhachHang) getChiTietNode();
        khoiTaoGiaoDien();
        napDanhSachLoaiKhachHang();
        khoiTaoSuKien();
    }

    @Override
    protected TableView<KhachHang> taoBangChinh() {
        table = new TableView<>();

        TableColumn<KhachHang, String> colMaKH = new TableColumn<>("Mã KH");
        colMaKH.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaKH()));

        TableColumn<KhachHang, String> colHoTen = new TableColumn<>("Họ tên");
        colHoTen.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getHoTen()));

        TableColumn<KhachHang, String> colNgaySinh = new TableColumn<>("Ngày sinh");
        colNgaySinh.setCellValueFactory(cell -> {
            if (cell.getValue().getNgaySinh() != null) {
                return new SimpleStringProperty(
                        new java.text.SimpleDateFormat("dd/MM/yyyy").format(cell.getValue().getNgaySinh())
                );
            }
            return new SimpleStringProperty("");
        });

        TableColumn<KhachHang, String> colGioiTinh = new TableColumn<>("Giới tính");
        colGioiTinh.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGioiTinh()));

        TableColumn<KhachHang, String> colSoDT = new TableColumn<>("Số ĐT");
        colSoDT.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSoDT()));

        TableColumn<KhachHang, String> colLoaiKH = new TableColumn<>("Loại KH");
        colLoaiKH.setCellValueFactory(cell -> {
            if (cell.getValue().getLoaiKH() != null) {
                return new SimpleStringProperty(cell.getValue().getLoaiKH().getTenLoaiKH());
            }
            return new SimpleStringProperty("");
        });

        table.getColumns().addAll(colMaKH, colHoTen, colNgaySinh, colGioiTinh, colSoDT, colLoaiKH);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        List<KhachHang> dsKhachHang = controller.layDanhSachKhachHang();
        table.getItems().setAll(dsKhachHang);

        table.setRowFactory(t -> {
            TableRow<KhachHang> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    KhachHang kh = row.getItem();
                    hienThiChiTiet(kh); // show chi tiết bên pane phải
                }
            });
            return row;
        });

        return table;
    }

    private void hienThiChiTiet(KhachHang kh) {
        gdChiTietKH.hienThiThongTin(kh);
    }

    private void khoiTaoSuKien() {
        gdChiTietKH.getBtnTaoMoi().setOnAction(e -> {
            String maKH = gdChiTietKH.getTxtMaKH().getText().trim();
            String hoTen = gdChiTietKH.getTxtHoTen().getText().trim();
            String gioiTinh = gdChiTietKH.getTxtGioiTinh().getText().trim();
            String soDT = gdChiTietKH.getTxtSoDT().getText().trim();
            var ngaySinh = gdChiTietKH.getDpNgaySinh().getValue();
            LoaiKhachHang loai = gdChiTietKH.getCboLoaiKH().getValue();

            String result = controller.taoKhachHang(hoTen, ngaySinh, gioiTinh, soDT, loai);
            Stage stage = (Stage) gdChiTietKH.getScene().getWindow();

            if (result.equals("OK")) {
                gdChiTietKH.Clear();
                refreshBangChinh();
                hienThongBao(stage,"Tạo khách hàng thành công!", Alert.AlertType.INFORMATION);
            } else {
                hienThongBao(stage,result, Alert.AlertType.WARNING);
            }
        });

        gdChiTietKH.getBtnLuu().setOnAction(e -> {
            String maKH = gdChiTietKH.getTxtMaKH().getText().trim();
            String hoTen = gdChiTietKH.getTxtHoTen().getText().trim();
            String gioiTinh = gdChiTietKH.getTxtGioiTinh().getText().trim();
            String soDT = gdChiTietKH.getTxtSoDT().getText().trim();
            var ngaySinh = gdChiTietKH.getDpNgaySinh().getValue();
            LoaiKhachHang loai = gdChiTietKH.getCboLoaiKH().getValue();

            String result = controller.capNhatKhachHang(maKH, hoTen, ngaySinh, gioiTinh, soDT, loai);
            Stage stage = (Stage) gdChiTietKH.getScene().getWindow();

            if (result.equals("OK")) {
                refreshBangChinh();
                hienThongBao(stage,"Cập nhật khách hàng thành công!", Alert.AlertType.INFORMATION);
            } else {
                hienThongBao(stage,result, Alert.AlertType.WARNING);
            }
        });
    }

    private void napDanhSachLoaiKhachHang() {
        List<LoaiKhachHang> dsLoai = controller.layDanhSachLoaiKhachHang();
        gdChiTietKH.getCboLoaiKH().getItems().setAll(dsLoai);
    }

    private void refreshBangChinh() {
        if (table != null) {
            table.getItems().setAll(controller.layDanhSachKhachHang());
        }
    }
}