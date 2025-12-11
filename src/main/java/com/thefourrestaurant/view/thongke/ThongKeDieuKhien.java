package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.ThongKeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

public class ThongKeDieuKhien {

    private final ThongKeGiaoDien giaoDien;
    private final ThongKeDAO thongKeDAO;
    private Chart bieuDoHienTai = null;
    private String loaiBieuDoHienTai = "";
    private String loaiThongKeHienTai = "";
    private Set<String> tatCaDanhMuc = new LinkedHashSet<>();

    public ThongKeDieuKhien(ThongKeGiaoDien giaoDien) {
        this.giaoDien = giaoDien;
        this.thongKeDAO = new ThongKeDAO();
        themXuLySuKien();
    }

    private void themXuLySuKien() {
        giaoDien.layNutXemThongKe().setOnAction(e -> xuLyXemThongKe());

        giaoDien.layHopKiemSoSanh().selectedProperty().addListener((obs, giaTriCu, giaTriMoi) -> {
            if (giaTriMoi) {
                giaoDien.layNutXemThongKe().setText("Thêm So Sánh");
            } else {
                giaoDien.layNutXemThongKe().setText("Xem Thống Kê");
            }
        });

        giaoDien.layBoChonNgayBatDau().setOnAction(e -> kiemTraKhoangNgay());
        giaoDien.layBoChonNgayKetThuc().setOnAction(e -> kiemTraKhoangNgay());
    }

    private void kiemTraKhoangNgay() {
        LocalDate ngayBatDau = giaoDien.layBoChonNgayBatDau().getValue();
        LocalDate ngayKetThuc = giaoDien.layBoChonNgayKetThuc().getValue();
        if (ngayBatDau != null && ngayKetThuc != null) {
            if (ngayBatDau.isAfter(ngayKetThuc)) {
                giaoDien.layBoChonNgayKetThuc().setValue(ngayBatDau);
            }
        }
    }

    private void xuLyXemThongKe() {
        String theo = giaoDien.layHopChonTheo().getValue();
        LocalDate ngayBatDau = null;
        LocalDate ngayKetThuc = null;

        switch (theo) {
            case "Ngày":
                ngayBatDau = giaoDien.layBoChonNgayBatDau().getValue();
                ngayKetThuc = giaoDien.layBoChonNgayKetThuc().getValue();
                if (ngayBatDau == null || ngayKetThuc == null || ngayBatDau.isAfter(ngayKetThuc)) {
                    hienThiThongBao("Khoảng thời gian không hợp lệ.");
                    return;
                }
                break;
            case "Tháng":
                int thang = giaoDien.layHopChonThang().getValue();
                int namChoThang = giaoDien.layHopChonNam().getValue();
                YearMonth namThang = YearMonth.of(namChoThang, thang);
                ngayBatDau = namThang.atDay(1);
                ngayKetThuc = namThang.atEndOfMonth();
                break;
            case "Năm":
                int nam = giaoDien.layHopChonNam().getValue();
                ngayBatDau = LocalDate.of(nam, 1, 1);
                ngayKetThuc = LocalDate.of(nam, 12, 31);
                break;
            case "Quý":
                int quy = Integer.parseInt(giaoDien.layHopChonQuy().getValue().substring(4));
                int namChoQuy = giaoDien.layHopChonNam().getValue();
                ngayBatDau = LocalDate.of(namChoQuy, (quy - 1) * 3 + 1, 1);
                ngayKetThuc = ngayBatDau.plusMonths(2).withDayOfMonth(ngayBatDau.plusMonths(2).lengthOfMonth());
                break;
        }

        String loaiThongKe = giaoDien.layHopChonLoaiThongKe().getValue();
        String loaiBieuDo = giaoDien.layHopChonLoaiBieuDo().getValue();
        String tuyChon = giaoDien.layHopChonTuyChon().getValue();

        if (giaoDien.layHopKiemSoSanh().isSelected()) {
            themSeriesMoiVaoBieuDo(ngayBatDau, ngayKetThuc, loaiThongKe, loaiBieuDo, tuyChon);
        } else {
            taoBieuDoMoi(ngayBatDau, ngayKetThuc, loaiThongKe, loaiBieuDo, tuyChon);
        }
    }

    private void taoBieuDoMoi(LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiThongKe, String loaiBieuDo, String tuyChon) {
        giaoDien.layKhuVucBieuDo().getChildren().clear();
        bieuDoHienTai = null;
        tatCaDanhMuc.clear();

        Map<String, ? extends Number> duLieu = layDuLieu(ngayBatDau, ngayKetThuc, loaiThongKe, tuyChon);
        if (duLieu == null || duLieu.isEmpty()) {
            giaoDien.layKhuVucBieuDo().getChildren().add(new Label("Không có dữ liệu cho khoảng thời gian này."));
            giaoDien.layLuoiTomTat().setVisible(false);
            return;
        }

        tatCaDanhMuc.addAll(duLieu.keySet());

        String tieuDe = taoTieuDeBieuDo(loaiThongKe, tuyChon, ngayBatDau, ngayKetThuc);
        String tenChuoiDuLieu = taoTenChuoiDuLieu(loaiThongKe, tuyChon, ngayBatDau, ngayKetThuc);

        Chart bieuDo = taoBieuDo(duLieu, loaiBieuDo, tieuDe, loaiThongKe, tenChuoiDuLieu);
        if (bieuDo != null) {
            bieuDoHienTai = bieuDo;
            loaiBieuDoHienTai = loaiBieuDo;
            loaiThongKeHienTai = loaiThongKe;
            giaoDien.layKhuVucBieuDo().getChildren().add(bieuDo);
            capNhatTomTat(ngayBatDau, ngayKetThuc, loaiThongKe, duLieu);
        } else {
            giaoDien.layKhuVucBieuDo().getChildren().add(new Label("Loại biểu đồ không được hỗ trợ."));
            giaoDien.layLuoiTomTat().setVisible(false);
        }
    }

    private void themSeriesMoiVaoBieuDo(LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiThongKe, String loaiBieuDo, String tuyChon) {
        if (bieuDoHienTai == null) {
            hienThiThongBao("Vui lòng tạo một biểu đồ gốc trước khi so sánh.");
            return;
        }
        if (!loaiBieuDoHienTai.equals(loaiBieuDo) || !loaiThongKeHienTai.equals(loaiThongKe)) {
            hienThiThongBao("Không thể so sánh hai loại thống kê hoặc biểu đồ khác nhau.");
            return;
        }
        if (bieuDoHienTai instanceof PieChart) {
            hienThiThongBao("Không thể thêm so sánh cho biểu đồ tròn.");
            return;
        }

        XYChart<String, Number> bieuDoXY = (XYChart<String, Number>) bieuDoHienTai;
        bieuDoXY.setAnimated(false);

        Map<String, ? extends Number> duLieu = layDuLieu(ngayBatDau, ngayKetThuc, loaiThongKe, tuyChon);
        if (duLieu == null || duLieu.isEmpty()) {
            hienThiThongBao("Không có dữ liệu để so sánh trong khoảng thời gian đã chọn.");
            bieuDoXY.setAnimated(true);
            return;
        }

        tatCaDanhMuc.addAll(duLieu.keySet());
        List<String> danhMucSapXep = new ArrayList<>(tatCaDanhMuc);
        Collections.sort(danhMucSapXep);

        CategoryAxis trucX = (CategoryAxis) bieuDoXY.getXAxis();
        trucX.setAutoRanging(false);
        trucX.getCategories().clear();
        trucX.setCategories(FXCollections.observableArrayList(danhMucSapXep));

        XYChart.Series<String, Number> chuoiMoi = new XYChart.Series<>();
        String tenChuoiDuLieu = taoTenChuoiDuLieu(loaiThongKe, tuyChon, ngayBatDau, ngayKetThuc);
        chuoiMoi.setName(tenChuoiDuLieu);

        for (String danhMuc : danhMucSapXep) {
            Number giaTri = duLieu.get(danhMuc);
            if (giaTri == null) {
                giaTri = 0;
            }
            chuoiMoi.getData().add(new XYChart.Data<>(danhMuc, giaTri));
        }

        for (XYChart.Series<String, Number> chuoiHienCo : bieuDoXY.getData()) {
            Map<String, Number> duLieuHienCo = new HashMap<>();
            for (XYChart.Data<String, Number> d : chuoiHienCo.getData()) {
                duLieuHienCo.put(d.getXValue(), d.getYValue());
            }
            chuoiHienCo.getData().clear();
            for (String danhMuc : danhMucSapXep) {
                Number giaTri = duLieuHienCo.getOrDefault(danhMuc, 0);
                chuoiHienCo.getData().add(new XYChart.Data<>(danhMuc, giaTri));
            }
        }

        bieuDoXY.getData().add(chuoiMoi);

        bieuDoHienTai.setTitle(String.format("So sánh %s", loaiThongKe.toLowerCase()));
        if (!bieuDoXY.isLegendVisible()) {
            bieuDoXY.setLegendVisible(true);
        }
        
        bieuDoXY.setAnimated(true);
    }

    private Map<String, ? extends Number> layDuLieu(LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiThongKe, String tuyChon) {
        return switch (loaiThongKe) {
            case "Doanh thu" -> thongKeDAO.getDoanhThuTheoNgay(ngayBatDau, ngayKetThuc, tuyChon);
            case "Món ăn" -> thongKeDAO.getThongKeMonAn(ngayBatDau, ngayKetThuc, tuyChon);
            case "Bàn" -> thongKeDAO.getThongKeBan(ngayBatDau, ngayKetThuc, tuyChon);
            default -> null;
        };
    }

    private Chart taoBieuDo(Map<String, ? extends Number> duLieu, String loaiBieuDo, String tieuDe, String nhanTrucXCoBan, String tenChuoiDuLieu) {
        String nhanTrucY = switch (nhanTrucXCoBan) {
            case "Doanh thu", "Bàn" -> "Doanh thu (VND)";
            case "Món ăn" -> "Số lượng bán";
            default -> "Giá trị";
        };
        String nhanTrucX = switch (nhanTrucXCoBan) {
            case "Doanh thu" -> "Ngày";
            default -> nhanTrucXCoBan;
        };

        return switch (loaiBieuDo) {
            case "Biểu đồ cột" -> taoBieuDoCot(duLieu, tieuDe, nhanTrucX, nhanTrucY, tenChuoiDuLieu);
            case "Biểu đồ đường" -> taoBieuDoDuong(duLieu, tieuDe, nhanTrucX, nhanTrucY, tenChuoiDuLieu);
            case "Biểu đồ tròn" -> taoBieuDoTron(duLieu, tieuDe);
            default -> null;
        };
    }

    private <T extends Number> BarChart<String, Number> taoBieuDoCot(Map<String, T> duLieu, String tieuDe, String nhanTrucX, String nhanTrucY, String tenChuoiDuLieu) {
        CategoryAxis trucX = new CategoryAxis();
        NumberAxis trucY = new NumberAxis();
        BarChart<String, Number> bieuDoCot = new BarChart<>(trucX, trucY);
        trucX.setLabel(nhanTrucX);
        trucY.setLabel(nhanTrucY);
        bieuDoCot.setTitle(tieuDe);
        bieuDoCot.setLegendVisible(false);
        bieuDoCot.setCategoryGap(10);
        bieuDoCot.setBarGap(3);
        XYChart.Series<String, Number> chuoi = new XYChart.Series<>();
        chuoi.setName(tenChuoiDuLieu);
        
        List<String> danhMucSapXep = new ArrayList<>(duLieu.keySet());
        Collections.sort(danhMucSapXep);
        trucX.setAutoRanging(false);
        trucX.setCategories(FXCollections.observableArrayList(danhMucSapXep));

        for (String danhMuc : danhMucSapXep) {
            chuoi.getData().add(new XYChart.Data<>(danhMuc, duLieu.get(danhMuc)));
        }
        bieuDoCot.getData().add(chuoi);
        return bieuDoCot;
    }

    private <T extends Number> LineChart<String, Number> taoBieuDoDuong(Map<String, T> duLieu, String tieuDe, String nhanTrucX, String nhanTrucY, String tenChuoiDuLieu) {
        CategoryAxis trucX = new CategoryAxis();
        NumberAxis trucY = new NumberAxis();
        LineChart<String, Number> bieuDoDuong = new LineChart<>(trucX, trucY);
        trucX.setLabel(nhanTrucX);
        trucY.setLabel(nhanTrucY);
        bieuDoDuong.setTitle(tieuDe);
        bieuDoDuong.setLegendVisible(false);
        XYChart.Series<String, Number> chuoi = new XYChart.Series<>();
        chuoi.setName(tenChuoiDuLieu);

        List<String> danhMucSapXep = new ArrayList<>(duLieu.keySet());
        Collections.sort(danhMucSapXep);
        trucX.setAutoRanging(false);
        trucX.setCategories(FXCollections.observableArrayList(danhMucSapXep));

        for (String danhMuc : danhMucSapXep) {
            chuoi.getData().add(new XYChart.Data<>(danhMuc, duLieu.get(danhMuc)));
        }
        bieuDoDuong.getData().add(chuoi);
        return bieuDoDuong;
    }

    private <T extends Number> PieChart taoBieuDoTron(Map<String, T> duLieu, String tieuDe) {
        ObservableList<PieChart.Data> duLieuBieuDoTron = FXCollections.observableArrayList();
        for (Map.Entry<String, T> muc : duLieu.entrySet()) {
            duLieuBieuDoTron.add(new PieChart.Data(muc.getKey(), muc.getValue().doubleValue()));
        }
        PieChart bieuDoTron = new PieChart(duLieuBieuDoTron);
        bieuDoTron.setTitle(tieuDe);
        return bieuDoTron;
    }

    private String taoTenChuoiDuLieu(String loaiThongKe, String tuyChon, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        String khoangNgay;
        if (ngayBatDau.equals(ngayKetThuc)) {
            khoangNgay = ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            khoangNgay = String.format("%s - %s",
                    ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM")),
                    ngayKetThuc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (("Món ăn".equals(loaiThongKe) || "Bàn".equals(loaiThongKe) || "Doanh thu".equals(loaiThongKe)) && tuyChon != null && !tuyChon.startsWith("Tất cả")) {
            return String.format("%s (%s)", tuyChon, khoangNgay);
        }
        return khoangNgay;
    }
    
    private String taoTieuDeBieuDo(String loaiThongKe, String tuyChon, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        String thoiGian;
        if (ngayBatDau.equals(ngayKetThuc)) {
            thoiGian = "ngày " + ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            thoiGian = String.format("từ %s đến %s",
                    ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    ngayKetThuc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (("Món ăn".equals(loaiThongKe) || "Bàn".equals(loaiThongKe) || "Doanh thu".equals(loaiThongKe)) && tuyChon != null && !tuyChon.startsWith("Tất cả")) {
            return String.format("Thống kê %s loại '%s' %s", loaiThongKe.toLowerCase(), tuyChon, thoiGian);
        } else {
            return String.format("Thống kê tất cả %s %s", loaiThongKe.toLowerCase(), thoiGian);
        }
    }

    private void capNhatTomTat(LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiThongKe, Map<String, ? extends Number> duLieu) {
        GridPane luoiTomTat = giaoDien.layLuoiTomTat();
        luoiTomTat.getChildren().clear();
        luoiTomTat.setVisible(false);

        if ("Món ăn".equals(loaiThongKe)) {
            BigDecimal hoaDonCaoNhat = thongKeDAO.getInvoiceStat(ngayBatDau, ngayKetThuc, "MAX");
            BigDecimal hoaDonThapNhat = thongKeDAO.getInvoiceStat(ngayBatDau, ngayKetThuc, "MIN");

            Optional<? extends Map.Entry<String, ? extends Number>> monBanChay = duLieu.entrySet().stream().max(Map.Entry.comparingByValue(Comparator.comparingDouble(Number::doubleValue)));
            Optional<? extends Map.Entry<String, ? extends Number>> monBanCham = duLieu.entrySet().stream().min(Map.Entry.comparingByValue(Comparator.comparingDouble(Number::doubleValue)));

            luoiTomTat.add(new Label("Hóa đơn cao nhất:"), 0, 0);
            luoiTomTat.add(new Label(NumberFormat.getCurrencyInstance(Locale.of("vi", "VN")).format(hoaDonCaoNhat)), 1, 0);
            luoiTomTat.add(new Label("Hóa đơn thấp nhất:"), 0, 1);
            luoiTomTat.add(new Label(NumberFormat.getCurrencyInstance(Locale.of("vi", "VN")).format(hoaDonThapNhat)), 1, 1);

            monBanChay.ifPresent(muc -> {
                luoiTomTat.add(new Label("Món bán chạy nhất:"), 2, 0);
                luoiTomTat.add(new Label(String.format("%s (%d)", muc.getKey(), muc.getValue().intValue())), 3, 0);
            });
            monBanCham.ifPresent(muc -> {
                luoiTomTat.add(new Label("Món bán chậm nhất:"), 2, 1);
                luoiTomTat.add(new Label(String.format("%s (%d)", muc.getKey(), muc.getValue().intValue())), 3, 1);
            });

            luoiTomTat.setVisible(true);
        }
    }

    private void hienThiThongBao(String noiDung) {
        Alert thongBao = new Alert(Alert.AlertType.INFORMATION);
        thongBao.setTitle("Thông báo");
        thongBao.setHeaderText(null);
        thongBao.setContentText(noiDung);

        Window cuaSoCha = giaoDien.getScene().getWindow();
        thongBao.initOwner(cuaSoCha);

        thongBao.showAndWait();
    }
}
