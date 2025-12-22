package com.thefourrestaurant.view.nhanvien;

import com.thefourrestaurant.controller.NhanVienController;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.util.Comparator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GiaoDienNhanVien extends GiaoDienThucThe {

    private final NhanVienController controller = new NhanVienController();
    private final com.thefourrestaurant.DAO.NhanVienDAO nhanVienDAO = new com.thefourrestaurant.DAO.NhanVienDAO();
    private final GiaoDienChiTietNhanVien gdChiTiet;
    private TableView<NhanVien> table;
    private ObservableList<NhanVien> danhSachGoc;
    private ObservableList<NhanVien> danhSachHienThi;

    public GiaoDienNhanVien() {
        super("Nhân viên", new GiaoDienChiTietNhanVien());
        gdChiTiet = (GiaoDienChiTietNhanVien) getChiTietNode();
        khoiTaoGiaoDien();
        khoiTaoBoLocTimKiem("nhập tên nhân viên");
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
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        TableColumn<NhanVien, Void> colAction = new TableColumn<>("Hành động");
        colAction.setCellFactory(tc -> new TableCell<>() {
            private final HBox box = new HBox(6);
            {
                box.setAlignment(Pos.CENTER);
            }
            private final com.thefourrestaurant.view.components.ButtonSample btnSua = new com.thefourrestaurant.view.components.ButtonSample("Sửa", 36, 14, 1);
//            private final com.thefourrestaurant.view.components.ButtonSample btnXoa = new com.thefourrestaurant.view.components.ButtonSample("Xóa", 36, 14, 2);
            private final com.thefourrestaurant.view.components.ButtonSample btnAdd = new com.thefourrestaurant.view.components.ButtonSample("Thêm nhân viên", 36, 16, 1);

            {
                btnSua.setOnAction(e -> {
                    NhanVien nv = getTableView().getItems().get(getIndex());
                    if (nv != null) {
                        ((GiaoDienChiTietNhanVien) getChiTietNode()).hienThi(nv);
                        getTableView().getSelectionModel().select(nv);
                    }
                });

//                btnXoa.setOnAction(e -> {
//                    NhanVien nv = getTableView().getItems().get(getIndex());
//                    if (nv == null || nv.getMaNV() == null || nv.getMaNV().trim().isEmpty()) return;
//                    Stage stage = getTableView().getScene() != null ? (Stage) getTableView().getScene().getWindow() : null;
//                    boolean confirm = xacNhan(stage, "Bạn có chắc muốn xóa nhân viên này?");
//                    if (!confirm) return;
//                    try {
//                        nv.setDeleted(true);
//                        boolean ok = controller.capNhatNhanVien(nv, null);
//                        if (ok) {
//                            getTableView().getItems().remove(nv);
//                        } else {
//                            hienThongBao(stage, "Xóa thất bại.", Alert.AlertType.ERROR);
//                        }
//                    } catch (Exception ex) { ex.printStackTrace(); }
//                });

                btnAdd.setOnAction(e -> {
                    GiaoDienChiTietNhanVien chiTiet = (GiaoDienChiTietNhanVien) getChiTietNode();
                    chiTiet.hienThi(null);
                    try {
                        chiTiet.getTxtMaNV().setText(nhanVienDAO.taoMaNhanVienMoi());
                        chiTiet.getTxtMaTK().setText(com.thefourrestaurant.DAO.TaiKhoanDAO.taoMaTaiKhoanMoi());
                        chiTiet.getDtpNgaySinh().setValue(LocalDate.of(2001, 1, 1));
                    } catch (Exception ex) { }
                    getTableView().getSelectionModel().clearSelection();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                box.getChildren().clear();
                if (empty) {
                    setGraphic(null);
                    return;
                }
                NhanVien nv = getTableView().getItems().get(getIndex());
                if (nv == null || nv.getMaNV() == null || nv.getMaNV().trim().isEmpty()) {
                    btnAdd.setPrefWidth(180);
                    box.getChildren().add(btnAdd);
                } else {
                    btnSua.setPrefWidth(80);
//                    btnXoa.setPrefWidth(80);
                    box.getChildren().addAll(btnSua);
                }
                setGraphic(box);
                setAlignment(Pos.CENTER);
            }
        });
        colAction.setPrefWidth(300);

        table.getColumns().addAll(colMa, colHoTen, colNgaySinh, colGioiTinh, colSDT, colVaiTro, colMaTK, colAction);
        return table;
    }

    @Override
    protected void lamMoiDuLieu() {
        List<NhanVien> ds = controller.layDanhSachNhanVien();
        danhSachGoc = FXCollections.observableArrayList(ds);

        var source = FXCollections.observableArrayList(ds);
        NhanVien placeholder = new NhanVien();
        source.add(placeholder);

        table.setItems(source);
        table.comparatorProperty().addListener((obs, old, nw) -> {
            Comparator<NhanVien> comp = nw;
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

    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            table.setItems(danhSachGoc);
            return;
        }
        String q = tuKhoa.toLowerCase();
        ObservableList<NhanVien> filtered = danhSachGoc.filtered(nv -> {
            if (nv == null) return false;
            try {
                String ten = nv.getHoTen() == null ? "" : nv.getHoTen().toLowerCase();
                return ten.contains(q);
            } catch (Exception ex) {
                return false;
            }
        });
        table.setItems(filtered);
    }

    private void khoiTaoSuKien() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) {
                gdChiTiet.hienThi(null);
                return;
            }
            if (newV.getMaNV() == null || newV.getMaNV().trim().isEmpty()) {
                gdChiTiet.hienThi(null);
                try {
                    gdChiTiet.getTxtMaNV().setText(nhanVienDAO.taoMaNhanVienMoi());
                    gdChiTiet.getTxtMaTK().setText(com.thefourrestaurant.DAO.TaiKhoanDAO.taoMaTaiKhoanMoi());
                    gdChiTiet.getDtpNgaySinh().setValue(LocalDate.of(2001, 1, 1));
                } catch (Exception ex) { }
                return;
            }
            gdChiTiet.hienThi(newV);
        });
        
        gdChiTiet.getBtnLuu().setOnAction(e -> {
            NhanVien selected = table.getSelectionModel().getSelectedItem();
            Stage stage = gdChiTiet.getScene() != null ? (Stage) gdChiTiet.getScene().getWindow() : null;
            if (selected == null) {
                hienThongBao(stage, "Vui lòng chọn nhân viên để lưu", Alert.AlertType.WARNING);
                return;
            }

            String ma = gdChiTiet.getTxtMaNV().getText().trim();
            String hoTen = gdChiTiet.getTxtHoTen().getText().trim();
            java.sql.Date ngay = gdChiTiet.getDtpNgaySinh().getValue() == null ? null : java.sql.Date.valueOf(gdChiTiet.getDtpNgaySinh().getValue());
            String gioiTinh = gdChiTiet.getGioiTinhValue(); 
            String sdt = gdChiTiet.getTxtSDT().getText().trim();
            BigDecimal luong = selected.getLuong();
            if (luong == null) luong = BigDecimal.ZERO;

            NhanVien nv = new NhanVien(ma, hoTen, ngay, gioiTinh, sdt, luong, selected.getMaTK());
            boolean ok = controller.capNhatNhanVien(nv, gdChiTiet.getSelectedImageFile());
            if (ok) {
                hienThongBao(stage, "Thông tin nhân viên đã được cập nhật", Alert.AlertType.INFORMATION);
                lamMoiDuLieu();
            } else {
                hienThongBao(stage, "Cập nhật thất bại", Alert.AlertType.ERROR);
            }
        });
    }
}