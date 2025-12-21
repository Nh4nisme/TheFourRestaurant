package com.thefourrestaurant.view.taikhoan;

import com.thefourrestaurant.controller.HoaDonController;
import com.thefourrestaurant.controller.TaiKhoanController;
import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Comparator;
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
        khoiTaoBoLocTimKiem("nhập tên tài khoản");
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
            private final HBox box = new HBox(6);
            {
                box.setAlignment(javafx.geometry.Pos.CENTER);
            }
            private final ButtonSample btnSua = new ButtonSample("Sửa", 36, 14, 1);
            private final ButtonSample btnXoa = new ButtonSample("Xóa", 36, 14, 2);
            private final ButtonSample btnAdd = new ButtonSample("Thêm tài khoản", 36, 16, 1);

            {
                btnSua.setOnAction(e -> {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    if (tk != null) {
                        hienThiChiTiet(tk);
                        getTableView().getSelectionModel().select(tk);
                    }
                });

                btnXoa.setOnAction(e -> {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    if (tk == null || tk.getMaTK() == null || tk.getMaTK().trim().isEmpty()) return;
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setTitle("Xác nhận");
                    a.setHeaderText("Xác nhận");
                    a.setContentText("Bạn có chắc muốn xóa tài khoản: " + tk.getTenDN() + " ?");
                    a.initOwner(getTableView().getScene() != null ? (javafx.stage.Window) getTableView().getScene().getWindow() : null);
                    a.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.OK) {
                            try {
                                boolean ok = controller.xoaTaiKhoan(tk.getMaTK());
                                if (ok) {
                                    refreshBangChinh();
                                } else {
                                    Alert err = new Alert(Alert.AlertType.ERROR, "Xóa thất bại.");
                                    err.initOwner(getTableView().getScene() != null ? (javafx.stage.Window) getTableView().getScene().getWindow() : null);
                                    err.showAndWait();
                                }
                            } catch (Exception ex) { ex.printStackTrace(); }
                        }
                    });
                });

                btnAdd.setOnAction(e -> {
                    table.getSelectionModel().clearSelection();
                    gdChiTietTK.Clear();
                    try {
                        gdChiTietTK.getTxtMaTK().setText(com.thefourrestaurant.DAO.TaiKhoanDAO.taoMaTaiKhoanMoi());
                    } catch (Exception ex) { }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                box.getChildren().clear();
                if (empty) { setGraphic(null); return; }
                TaiKhoan tk = getTableView().getItems().get(getIndex());
                if (tk == null || tk.getMaTK() == null || tk.getMaTK().trim().isEmpty()) {
                    btnAdd.setPrefWidth(180);
                    box.getChildren().add(btnAdd);
                } else {
                    btnSua.setPrefWidth(80);
                    btnXoa.setPrefWidth(80);
                    box.getChildren().addAll(btnSua, btnXoa);
                }
                setGraphic(box);
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });

        table.getColumns().addAll(colMaTK, colTenDN, colVaiTro, colHanhDong);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // View gọi Controller để lấy danh sách dữ liệu từ DAO
        List<TaiKhoan> dsTaiKhoan = controller.layDanhSachTaiKhoan();
        var source = FXCollections.observableArrayList(dsTaiKhoan);
        TaiKhoan placeholder = new TaiKhoan();
        source.add(placeholder);

        table.setItems(source);
        table.comparatorProperty().addListener((obs, old, nw) -> {
            Comparator<TaiKhoan> comp = nw;
            javafx.application.Platform.runLater(() -> {
                if (comp != null) {
                    javafx.collections.FXCollections.sort(source, (a, b) -> {
                        if (a == placeholder && b == placeholder) return 0;
                        if (a == placeholder) return 1;
                        if (b == placeholder) return -1;
                        return comp.compare(a, b);
                    });
                }
            });
        });

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
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            table.setItems(danhSachGoc);
            return;
        }

        String lowerKey = tuKhoa.toLowerCase();
        ObservableList<TaiKhoan> ketQua = danhSachGoc.filtered(tk -> {
            if (tk == null) return false;
            try {
                String ma = tk.getMaTK() == null ? "" : tk.getMaTK().toLowerCase();
                String ten = tk.getTenDN() == null ? "" : tk.getTenDN().toLowerCase();
                String matkhau = tk.getMatKhau() == null ? "" : tk.getMatKhau().toLowerCase();
                String isDel = tk.isDeleted() == null ? "" : tk.isDeleted().toString().toLowerCase();
                String vaiTroName = "";
                String vaiTroMa = "";
                if (tk.getVaiTro() != null) {
                    vaiTroName = tk.getVaiTro().getTenVaiTro() == null ? "" : tk.getVaiTro().getTenVaiTro().toLowerCase();
                    vaiTroMa = tk.getVaiTro().getMaVT() == null ? "" : tk.getVaiTro().getMaVT().toLowerCase();
                }
                return ma.contains(lowerKey) || ten.contains(lowerKey) || matkhau.contains(lowerKey) || isDel.contains(lowerKey) || vaiTroName.contains(lowerKey) || vaiTroMa.contains(lowerKey);
            } catch (Exception ex) {
                return false;
            }
        });
        table.setItems(ketQua);
    }

    @Override
    protected void lamMoiDuLieu() {
        List<TaiKhoan> ds = controller.layDanhSachTaiKhoan();
        danhSachGoc = FXCollections.observableArrayList(ds);

        var source = FXCollections.observableArrayList(ds);
        TaiKhoan placeholder = new TaiKhoan();
        source.add(placeholder);
        table.setItems(source);
        table.comparatorProperty().addListener((obs, old, nw) -> {
            Comparator<TaiKhoan> comp = nw;
            javafx.application.Platform.runLater(() -> {
                if (comp != null) {
                    javafx.collections.FXCollections.sort(source, (a, b) -> {
                        if (a == placeholder && b == placeholder) return 0;
                        if (a == placeholder) return 1;
                        if (b == placeholder) return -1;
                        return comp.compare(a, b);
                    });
                }
            });
        });
    }

    private void khoiTaoSuKien() {
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
            lamMoiDuLieu();
        }
    }
}