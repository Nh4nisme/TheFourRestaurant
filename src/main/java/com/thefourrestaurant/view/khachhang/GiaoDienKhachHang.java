package com.thefourrestaurant.view.khachhang;

import com.thefourrestaurant.DAO.KhachHangDAO;
import com.thefourrestaurant.controller.KhachHangController;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.LoaiKhachHang;
import com.thefourrestaurant.util.ValidatorKhachHang;
import com.thefourrestaurant.view.components.GiaoDienThucThe;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class GiaoDienKhachHang extends GiaoDienThucThe {

    private final KhachHangController controller;
    private final GiaoDienChiTietKhachHang gdChiTietKH;
    private TableView<KhachHang> table;
    private ObservableList<KhachHang> danhSachGoc;
    private boolean comparatorListenerAdded = false;

    public GiaoDienKhachHang() {
        super("Khách hàng", new GiaoDienChiTietKhachHang());
        controller = new KhachHangController();
        gdChiTietKH = (GiaoDienChiTietKhachHang) getChiTietNode();
        khoiTaoGiaoDien();
        khoiTaoBoLocTimKiem("Nhập số điện thoại...");
        napDanhSachLoaiKhachHang();
        khoiTaoSuKien();
        lamMoiDuLieu();
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

        TableColumn<KhachHang, Void> colHanhDong = new TableColumn<>("Hành động");
        colHanhDong.setCellFactory(col -> new TableCell<>() {
            private final HBox box = new HBox(6);
            {
                box.setAlignment(Pos.CENTER);
            }
            private final ButtonSample btnSua = new ButtonSample("Sửa", 36, 14, 1);
            private final ButtonSample btnAdd = new ButtonSample("Thêm khách hàng", 36, 16, 1);

            {
                btnSua.setOnAction(e -> {
                    KhachHang kh = getTableView().getItems().get(getIndex());
                    if (kh == null) return;
                    table.getSelectionModel().select(kh);
                    hienThiChiTiet(kh);
                });

                btnAdd.setOnAction(e -> {
                    gdChiTietKH.Clear();
                    String maMoi = new KhachHangDAO().taoMaKHMoi();
                    try {
                        gdChiTietKH.getTxtMaKH().setText(maMoi);
                    } catch (Exception ex) { }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                KhachHang kh = getTableView().getItems().get(getIndex());
                if (kh == null) {
                    setGraphic(null);
                    return;
                }
                if (kh.getMaKH() == null || kh.getMaKH().isEmpty()) {
                    setGraphic(btnAdd);
                } else {
                    box.getChildren().setAll(btnSua);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(colMaKH, colHoTen, colNgaySinh, colGioiTinh, colSoDT, colLoaiKH, colHanhDong);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        List<KhachHang> dsKhachHang = controller.layDanhSachKhachHang();
        var source = FXCollections.observableArrayList(dsKhachHang);
        KhachHang placeholder = new KhachHang();
        source.add(placeholder);
        table.setItems(source);

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

    @Override
    protected void lamMoiDuLieu() {
        List<KhachHang> ds = controller.layDanhSachKhachHang();
        danhSachGoc = FXCollections.observableArrayList(ds);

        var source = FXCollections.observableArrayList(ds);
        KhachHang placeholder = new KhachHang();
        source.add(placeholder);
        table.setItems(source);

        if (!comparatorListenerAdded) {
            comparatorListenerAdded = true;
            table.comparatorProperty().addListener((obs, old, nw) -> {
                java.util.Comparator<KhachHang> comp = nw;
                Platform.runLater(() -> {
                    if (comp == null) return;
                    try { source.sort(comp); } catch (Exception ex) {}
                    if (source.remove(placeholder)) source.add(placeholder);
                });
            });
        }
    }

    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        if (tuKhoa.isEmpty()) {
            table.setItems(danhSachGoc);
            return;
        }

        String lowerKey = tuKhoa.toLowerCase();
        ObservableList<KhachHang> ketQua = danhSachGoc.filtered(
                kh -> kh.getSoDT().contains(lowerKey)
        );
        table.setItems(ketQua);
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

            // Sử dụng lớp ValidatorKhachHang
            List<String> errors = ValidatorKhachHang.validate(maKH, hoTen, gioiTinh, soDT, ngaySinh, loai);

            Stage stage = (Stage) gdChiTietKH.getScene().getWindow();
            if (!errors.isEmpty()) {
                hienThongBao(stage, String.join("\n", errors), Alert.AlertType.WARNING);
                return;
            }

            String result = controller.taoKhachHang(hoTen, ngaySinh, gioiTinh, soDT, loai);
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
            lamMoiDuLieu();
        }
    }


}