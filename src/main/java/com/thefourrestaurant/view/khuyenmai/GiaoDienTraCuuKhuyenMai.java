package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.DAO.LoaiKhuyenMaiDAO;
import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.view.components.DropDownButtonMap;
import com.thefourrestaurant.view.components.GiaoDienTraCuu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GiaoDienTraCuuKhuyenMai extends GiaoDienTraCuu {

    private final KhuyenMaiController controller;
    private final LoaiKhuyenMaiDAO loaiKhuyenMaiDAO;
    private List<KhuyenMai> danhSachKhuyenMaiGoc;
    private List<KhuyenMai> danhSachKhuyenMaiHienThi;
    private DropDownButtonMap<String> btnLoaiKM;

    public GiaoDienTraCuuKhuyenMai() {
        super();
        this.controller = new KhuyenMaiController();
        this.loaiKhuyenMaiDAO = new LoaiKhuyenMaiDAO();

        khoiTaoGiaoDien();
        themBoLocChuCai();
        themBoLocLoaiKhuyenMai();
        themBoLocSapXep();
        themThanhTimKiem("Tìm kiếm khuyến mãi (Tên, Mã, Code)...");
        themButtonLamMoi();

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        lamMoiDuLieu();
    }

    private void themBoLocLoaiKhuyenMai() {
        LinkedHashMap<String, String> dsLoaiMon = new LinkedHashMap<>();
        dsLoaiMon.put("Tất cả", null);
        loaiKhuyenMaiDAO.layTatCaLoaiKhuyenMai()
                .forEach(loaiKM -> {
                    dsLoaiMon.put(
                            loaiKM.getTenLoaiKM(),   // text hiển thị
                            loaiKM.getMaLoaiKM()     // value logic
                    );
                });
        btnLoaiKM = new DropDownButtonMap<>("Lọc theo loại khuyến mãi", dsLoaiMon, null, 35, 16, 3);
        btnLoaiKM.setOnItemSelected(maLoaiMon -> locVaCapNhatKhuyenMai());
        thanhTrai.getChildren().add(btnLoaiKM);
    }

    private void themBoLocSapXep() {
        LinkedHashMap<String, Boolean> mapNgay = new LinkedHashMap<>();
        mapNgay.put("Mới nhất", false);
        mapNgay.put("Cũ nhất", true);

        DropDownButtonMap<Boolean> btnTheoNgay = new DropDownButtonMap<>(
                "Theo thời gian ▼",
                mapNgay,
                null, 35, 16, 3
        );
        btnTheoNgay.setOnItemSelected(this::sapXepTheoNgay);

        LinkedHashMap<String, String> mapTrangThai = new LinkedHashMap<>();
        mapTrangThai.put("Đang diễn ra", "DANG_DIEN_RA");
        mapTrangThai.put("Sắp diễn ra", "SAP_DIEN_RA");
        mapTrangThai.put("Đã hết hạn", "DA_HET_HAN");

        DropDownButtonMap<String> btnTheoTrangThai = new DropDownButtonMap<>(
                "Theo trạng thái ▼",
                mapTrangThai,
                null, 35, 16, 3
        );
        btnTheoTrangThai.setOnItemSelected(this::locTheoTrangThai);

        thanhTrai.getChildren().addAll(btnTheoNgay, btnTheoTrangThai);
    }

    private void sapXepTheoNgay(boolean ascending) {
        Comparator<KhuyenMai> comp = Comparator.comparingInt(m -> {
            String id = m.getMaKM();
            if (id == null) return 0;
            String digits = id.replaceAll("\\D+", "");
            try { return Integer.parseInt(digits); } catch (Exception e) { return 0; }
        });
        if (!ascending) comp = comp.reversed();
        danhSachKhuyenMaiHienThi.sort(comp);
        capNhatBang();
    }

    private void locTheoTrangThai(String statusKey) {
        LocalDateTime now = LocalDateTime.now();
        danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc.stream().filter(km -> {
            if (km.getNgayBatDau() == null || km.getNgayKetThuc() == null) return false;
            switch (statusKey) {
                case "DANG_DIEN_RA":
                    return now.isAfter(km.getNgayBatDau()) && now.isBefore(km.getNgayKetThuc());
                case "SAP_DIEN_RA":
                    return now.isBefore(km.getNgayBatDau());
                case "DA_HET_HAN":
                    return now.isAfter(km.getNgayKetThuc());
                default:
                    return true;
            }
        }).collect(Collectors.toList());
        capNhatBang();
    }

    private void locVaCapNhatKhuyenMai() {
        String maLoaiDuocChon = btnLoaiKM.getSelectedValue();
        if (maLoaiDuocChon == null) {
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc;
        } else {
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc.stream()
                    .filter(monAn ->
                            monAn.getLoaiKhuyenMai() != null &&
                                    maLoaiDuocChon.equals(monAn.getLoaiKhuyenMai().getMaLoaiKM())
                    )
                    .toList();
        }
        capNhatBang();
    }

    @Override
    protected TableView<?> taoBangChinh() {
        TableView<KhuyenMai> table = new TableView<>();
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<KhuyenMai, String> maKMCol = new TableColumn<>("Mã KM");
        maKMCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaKM()));

        TableColumn<KhuyenMai, String> tenKMCol = new TableColumn<>("Tên khuyến mãi");
        tenKMCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTenKM()));

        TableColumn<KhuyenMai, String> kieuKMCol = new TableColumn<>("Kiểu KM");
        kieuKMCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            String kieu = KhuyenMai.KIEU_MA_GIAM_GIA.equals(km.getKieuKM()) ? "Mã giảm giá" : "Sự kiện";
            return new SimpleStringProperty(kieu);
        });

        TableColumn<KhuyenMai, String> loaiKMCol = new TableColumn<>("Loại KM");
        loaiKMCol.setCellValueFactory(cell -> {
            String tenLoaiKM = (cell.getValue().getLoaiKhuyenMai() != null)
                    ? cell.getValue().getLoaiKhuyenMai().getTenLoaiKM()
                    : "";
            return new SimpleStringProperty(tenLoaiKM);
        });

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        TableColumn<KhuyenMai, String> ngayBDCol = new TableColumn<>("Bắt đầu");
        ngayBDCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getNgayBatDau();
            return new SimpleStringProperty(date != null ? date.format(dateTimeFormatter) : "");
        });

        TableColumn<KhuyenMai, String> ngayKTCol = new TableColumn<>("Kết thúc");
        ngayKTCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getNgayKetThuc();
            return new SimpleStringProperty(date != null ? date.format(dateTimeFormatter) : "");
        });

        TableColumn<KhuyenMai, String> trangThaiCol = new TableColumn<>("Trạng thái");
        trangThaiCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            LocalDateTime now = LocalDateTime.now();
            String status = "N/A";
            if (km.getNgayBatDau() != null && km.getNgayKetThuc() != null) {
                if (now.isAfter(km.getNgayKetThuc())) {
                    status = "Đã hết hạn";
                } else if (now.isBefore(km.getNgayBatDau())) {
                    status = "Sắp diễn ra";
                } else {
                    status = "Đang diễn ra";
                }
            }
            return new SimpleStringProperty(status);
        });

        table.getColumns().addAll(maKMCol, tenKMCol, kieuKMCol, loaiKMCol, ngayBDCol, ngayKTCol, trangThaiCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setRowFactory(tv -> {
            TableRow<KhuyenMai> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    KhuyenMai km = row.getItem();
                    Stage owner = null;
                    if (row.getScene() != null && row.getScene().getWindow() != null) {
                        owner = (Stage) row.getScene().getWindow();
                    }
                    CuaSoChiTietTraCuuKhuyenMai detailWindow = new CuaSoChiTietTraCuuKhuyenMai(owner, km);
                    detailWindow.showAndWait();
                }
            });
            return row;
        });

        return table;
    }

    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isEmpty()) {
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc;
        } else {
            String lowerCaseTuKhoa = tuKhoa.toLowerCase();
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc.stream()
                    .filter(km ->
                            km.getTenKM().toLowerCase().contains(lowerCaseTuKhoa) ||
                                    km.getMaKM().toLowerCase().contains(lowerCaseTuKhoa) ||
                                    (km.getMaCode() != null && km.getMaCode().toLowerCase().contains(lowerCaseTuKhoa))
                    )
                    .collect(Collectors.toList());
        }
        capNhatBang();
    }

    @Override
    protected void lamMoiDuLieu() {
        danhSachKhuyenMaiGoc = controller.layDanhSachKhuyenMai();
        danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc;
        capNhatBang();
    }

    @Override
    protected void thucHienLocTheoChuCai(String chuCai) {
        if (chuCai == null || chuCai.isEmpty()) {
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc;
        } else {
            String lowerCaseChuCai = chuCai.toLowerCase();
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc.stream()
                    .filter(km -> km.getTenKM().toLowerCase().startsWith(lowerCaseChuCai))
                    .collect(Collectors.toList());
        }
        capNhatBang();
    }

    @SuppressWarnings("unchecked")
    private void capNhatBang() {
        ((TableView<KhuyenMai>) tableChinh).setItems(FXCollections.observableArrayList(danhSachKhuyenMaiHienThi));
    }
}
