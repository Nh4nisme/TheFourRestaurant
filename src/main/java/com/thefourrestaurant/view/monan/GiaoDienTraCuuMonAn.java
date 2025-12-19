package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.controller.MonAnController;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.DropDownButtonMap;
import com.thefourrestaurant.view.components.GiaoDienTraCuu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GiaoDienTraCuuMonAn extends GiaoDienTraCuu {

    private final MonAnController controller;
    private List<MonAn> danhSachMonAnGoc;
    private List<MonAn> danhSachMonAnHienThi;
    private final ComboBox<String> cboLoaiMonFilter = new ComboBox<>();

    public GiaoDienTraCuuMonAn() {
        super();
        this.controller = new MonAnController();

        khoiTaoGiaoDien();
        themBoLocChuCai();
        themBoLocLoaiMon();
        themBoLocSapXep();
        themThanhTimKiem("Tìm kiếm món ăn...");
        themButtonLamMoi();

        lamMoiDuLieu();
    }

    private void themBoLocLoaiMon() {
        cboLoaiMonFilter.setPromptText("Lọc theo loại");
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
        List<String> tenLoaiMon = loaiMonDAO.layTatCaLoaiMon().stream()
                .map(LoaiMon::getTenLoaiMon)
                .collect(Collectors.toList());
        cboLoaiMonFilter.getItems().add("Tất cả");
        cboLoaiMonFilter.getItems().addAll(tenLoaiMon);
        cboLoaiMonFilter.setValue("Tất cả");
        cboLoaiMonFilter.setOnAction(e -> locVaCapNhatMonAn());

        thanhTrai.getChildren().add(cboLoaiMonFilter);
    }

    private void themBoLocSapXep() {
        LinkedHashMap<String, Boolean> mapGia = new LinkedHashMap<>();
        mapGia.put("Tăng dần", true);
        mapGia.put("Giảm dần", false);

        DropDownButtonMap<Boolean> btnTheoGia = new DropDownButtonMap<>(
                "Theo giá ▼",
                mapGia,
                null, 35, 16, 3
        );
        btnTheoGia.setOnItemSelected(this::sapXepTheoGia);

        LinkedHashMap<String, Boolean> mapDaBan = new LinkedHashMap<>();
        mapDaBan.put("Phổ biến nhất", false);
        mapDaBan.put("Ít phổ biến", true);

        DropDownButtonMap<Boolean> btnTheoDaBan = new DropDownButtonMap<>(
                "Theo độ phổ biến ▼",
                mapDaBan,
                null, 35, 16, 3
        );
        btnTheoDaBan.setOnItemSelected(this::sapXepTheoDaBan);

        LinkedHashMap<String, Boolean> mapNgay = new LinkedHashMap<>();
        mapNgay.put("Mới nhất", false);
        mapNgay.put("Cũ nhất", true);

        DropDownButtonMap<Boolean> btnTheoNgay = new DropDownButtonMap<>(
                "Theo thời gian ▼",
                mapNgay,
                null, 35, 16, 3
        );
        btnTheoNgay.setOnItemSelected(this::sapXepTheoNgay);

        thanhTrai.getChildren().addAll(btnTheoGia, btnTheoNgay, btnTheoDaBan);
    }

    private void sapXepTheoGia(boolean ascending) {
        if (ascending) {
            danhSachMonAnHienThi.sort(Comparator.comparing(MonAn::getDonGia));
        } else {
            danhSachMonAnHienThi.sort(Comparator.comparing(MonAn::getDonGia).reversed());
        }
        capNhatBang();
    }

    private void sapXepTheoDaBan(boolean ascending) {
        if (ascending) {
            danhSachMonAnHienThi.sort(Comparator.comparingInt(MonAn::getDaBan));
        } else {
            danhSachMonAnHienThi.sort(Comparator.comparingInt(MonAn::getDaBan).reversed());
        }
        capNhatBang();
    }

    private void sapXepTheoNgay(boolean ascending) {
        Comparator<MonAn> comp = Comparator.comparingInt(m -> {
            String id = m.getMaMonAn();
            if (id == null) return 0;
            String digits = id.replaceAll("\\D+", "");
            try { return Integer.parseInt(digits); } catch (Exception e) { return 0; }
        });
        if (!ascending) comp = comp.reversed();
        danhSachMonAnHienThi.sort(comp);
        capNhatBang();
    }

    private void locVaCapNhatMonAn() {
        String loaiMonFilter = cboLoaiMonFilter.getValue();

        if (loaiMonFilter != null && !loaiMonFilter.equals("Tất cả")) {
            danhSachMonAnHienThi = danhSachMonAnGoc.stream()
                    .filter(monAn -> monAn.getLoaiMon() != null && monAn.getLoaiMon().getTenLoaiMon().equals(loaiMonFilter))
                    .collect(Collectors.toList());
        } else {
            danhSachMonAnHienThi = danhSachMonAnGoc;
        }
        capNhatBang();
    }

    @Override
    protected TableView<?> taoBangChinh() {
        TableView<MonAn> table = new TableView<>();
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<MonAn, String> maMonCol = new TableColumn<>("Mã món");
        maMonCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaMonAn()));

        TableColumn<MonAn, String> tenMonAnCol = new TableColumn<>("Tên món ăn");
        tenMonAnCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTenMon()));

        TableColumn<MonAn, String> donGiaCol = new TableColumn<>("Đơn giá (VND)");
        donGiaCol.setCellValueFactory(cellData -> {
            BigDecimal gia = cellData.getValue().getDonGia();
            String formattedGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(gia);
            return new SimpleStringProperty(formattedGia);
        });

        TableColumn<MonAn, String> soLuongCol = new TableColumn<>("Số lượng");
        soLuongCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getSoLuong())));

        TableColumn<MonAn, String> trangThaiCol = new TableColumn<>("Trạng thái");
        trangThaiCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTrangThai()));

        TableColumn<MonAn, String> loaiMonCol = new TableColumn<>("Loại món");
        loaiMonCol.setCellValueFactory(cell -> {
            String tenLoaiMon = (cell.getValue().getLoaiMon() != null)
                    ? cell.getValue().getLoaiMon().getTenLoaiMon()
                    : "";
            return new SimpleStringProperty(tenLoaiMon);
        });

        table.getColumns().addAll(maMonCol, tenMonAnCol, donGiaCol, soLuongCol, trangThaiCol, loaiMonCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setRowFactory(tv -> {
            TableRow<MonAn> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    MonAn monAn = row.getItem();
                    Stage owner = null;
                    if (row.getScene() != null && row.getScene().getWindow() != null) {
                        owner = (Stage) row.getScene().getWindow();
                    }
                    CuaSoChiTietTraCuuMonAn detailWindow = new CuaSoChiTietTraCuuMonAn(owner, monAn);
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
            danhSachMonAnHienThi = danhSachMonAnGoc;
        } else {
            String lowerCaseTuKhoa = tuKhoa.toLowerCase();
            danhSachMonAnHienThi = danhSachMonAnGoc.stream()
                    .filter(monAn ->
                            monAn.getTenMon().toLowerCase().contains(lowerCaseTuKhoa) ||
                                    monAn.getMaMonAn().toLowerCase().contains(lowerCaseTuKhoa)
                    )
                    .collect(Collectors.toList());
        }
        capNhatBang();
    }

    @Override
    protected void lamMoiDuLieu() {
        danhSachMonAnGoc = controller.layTatCaMonAn();
        danhSachMonAnHienThi = danhSachMonAnGoc;
        capNhatBang();
    }

    @Override
    protected void thucHienLocTheoChuCai(String chuCai) {
        if (chuCai == null || chuCai.isEmpty()) {
            danhSachMonAnHienThi = danhSachMonAnGoc;
        } else {
            String lowerCaseChuCai = chuCai.toLowerCase();
            danhSachMonAnHienThi = danhSachMonAnGoc.stream()
                    .filter(monAn -> monAn.getTenMon().toLowerCase().startsWith(lowerCaseChuCai))
                    .collect(Collectors.toList());
        }
        capNhatBang();
    }

    @SuppressWarnings("unchecked")
    private void capNhatBang() {
        ((TableView<MonAn>) tableChinh).setItems(FXCollections.observableArrayList(danhSachMonAnHienThi));
    }
}
