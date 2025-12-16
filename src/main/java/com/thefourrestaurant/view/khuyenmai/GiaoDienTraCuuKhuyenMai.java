package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.DAO.LoaiKhuyenMaiDAO;
import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButtonMap;
import com.thefourrestaurant.view.components.GiaoDienTraCuu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GiaoDienTraCuuKhuyenMai extends GiaoDienTraCuu {

    private final KhuyenMaiController controller;
    private List<KhuyenMai> danhSachKhuyenMaiGoc;
    private List<KhuyenMai> danhSachKhuyenMaiHienThi;
    private final ComboBox<String> cboLoaiKMFilter = new ComboBox<>();

    public GiaoDienTraCuuKhuyenMai() {
        super();
        this.controller = new KhuyenMaiController();

        khoiTaoGiaoDien();
        themBoLocChuCai();
        themBoLocLoaiKhuyenMai();
        themBoLocSapXep();
        themThanhTimKiem();
        themButtonLamMoi();

        ButtonSample btnThemMoi = new ButtonSample("+ Thêm khuyến mãi", 35, 16, 3);
        btnThemMoi.setOnAction(e -> {
            Stage owner = (Stage) getScene().getWindow();
            if (controller.themKhuyenMaiMoi(owner)) {
                lamMoiDuLieu();
            }
        });
        thanhPhai.getChildren().add(btnThemMoi);

        lamMoiDuLieu();
    }

    private void themBoLocLoaiKhuyenMai() {
        cboLoaiKMFilter.setPromptText("Lọc theo loại");
        LoaiKhuyenMaiDAO loaiKhuyenMaiDAO = new LoaiKhuyenMaiDAO();
        List<String> tenLoaiKM = loaiKhuyenMaiDAO.layTatCaLoaiKhuyenMai().stream()
                .map(LoaiKhuyenMai::getTenLoaiKM)
                .collect(Collectors.toList());
        cboLoaiKMFilter.getItems().add("Tất cả");
        cboLoaiKMFilter.getItems().addAll(tenLoaiKM);
        cboLoaiKMFilter.setValue("Tất cả");
        cboLoaiKMFilter.setOnAction(e -> locVaCapNhatKhuyenMai());

        thanhTrai.getChildren().add(cboLoaiKMFilter);
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
        btnTheoNgay.setOnItemSelected(ascending -> sapXepTheoNgay(ascending));

        thanhTrai.getChildren().add(btnTheoNgay);
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

    private void locVaCapNhatKhuyenMai() {
        String loaiKMFilter = cboLoaiKMFilter.getValue();

        if (loaiKMFilter != null && !loaiKMFilter.equals("Tất cả")) {
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc.stream()
                    .filter(km -> km.getLoaiKhuyenMai() != null && km.getLoaiKhuyenMai().getTenLoaiKM().equals(loaiKMFilter))
                    .collect(Collectors.toList());
        } else {
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc;
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

        TableColumn<KhuyenMai, String> tyLeCol = new TableColumn<>("Tỷ lệ");
        tyLeCol.setCellValueFactory(cellData -> {
            BigDecimal tyLe = cellData.getValue().getTyLe();
            return new SimpleStringProperty(tyLe != null && tyLe.compareTo(BigDecimal.ZERO) > 0
                    ? tyLe.stripTrailingZeros().toPlainString() + "%" : "");
        });

        TableColumn<KhuyenMai, String> soTienCol = new TableColumn<>("Số tiền");
        soTienCol.setCellValueFactory(cellData -> {
            BigDecimal soTien = cellData.getValue().getSoTien();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return new SimpleStringProperty(soTien != null && soTien.compareTo(BigDecimal.ZERO) > 0
                    ? currencyFormatter.format(soTien) : "");
        });

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        TableColumn<KhuyenMai, String> ngayBDCol = new TableColumn<>("Ngày bắt đầu");
        ngayBDCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getNgayBatDau();
            return new SimpleStringProperty(date != null ? date.format(dateTimeFormatter) : "");
        });

        TableColumn<KhuyenMai, String> ngayKTCol = new TableColumn<>("Ngày kết thúc");
        ngayKTCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getNgayKetThuc();
            return new SimpleStringProperty(date != null ? date.format(dateTimeFormatter) : "");
        });

        TableColumn<KhuyenMai, String> trangThaiCol = new TableColumn<>("Trạng thái");
        trangThaiCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            LocalDateTime now = LocalDateTime.now();
            String status = "Chưa áp dụng";
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

        table.getColumns().addAll(maKMCol, tenKMCol, kieuKMCol, loaiKMCol, tyLeCol, soTienCol, ngayBDCol, ngayKTCol, trangThaiCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setRowFactory(tv -> {
            TableRow<KhuyenMai> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    KhuyenMai km = row.getItem();
                    Stage owner = (Stage) getScene().getWindow();
                    if (controller.capNhatKhuyenMai(owner, km)) {
                        lamMoiDuLieu();
                    }
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
