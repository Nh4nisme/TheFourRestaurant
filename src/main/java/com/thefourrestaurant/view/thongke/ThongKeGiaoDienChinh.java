package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.ThongKeDAO;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class ThongKeGiaoDienChinh extends BorderPane {

    private final TabPane bangTab;
    private final ThongKeDAO thongKeDAO;
    private final NumberFormat dinhDangTienTe;

    public ThongKeGiaoDienChinh() {
        this.thongKeDAO = new ThongKeDAO();
        this.dinhDangTienTe = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));
        this.setStyle("-fx-background-color: white;");

        Label nhanTieuDe = new Label("Báo Cáo & Thống Kê");
        nhanTieuDe.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #D4A017;");

        ButtonSample nutXuatCSV = new ButtonSample("Xuat CSV", 50, 25, 14);
        nutXuatCSV.getStyleClass().add("button_sampleGamboge");
        nutXuatCSV.setOnAction(e -> xuatRaCSV());

        HBox tieuDe = new HBox(20);
        tieuDe.setPadding(new Insets(15));
        tieuDe.setAlignment(Pos.CENTER_LEFT);
        tieuDe.setStyle("-fx-background-color: #1E424D;");
        Region khoangTrong = new Region();
        HBox.setHgrow(khoangTrong, Priority.ALWAYS);
        tieuDe.getChildren().addAll(nhanTieuDe, khoangTrong, nutXuatCSV);
        this.setTop(tieuDe);

        bangTab = new TabPane();
        bangTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabTongQuan = new Tab("Tổng quan");
        tabTongQuan.setContent(new ThongKeDashboard());

        Tab tabDoanhThu = new Tab("Doanh Thu");
        tabDoanhThu.setContent(new ThongKeGiaoDien());

        Tab tabKhachHang = new Tab("Khách hàng");
        tabKhachHang.setContent(new ThongKeKhachHang());

        Tab tabNhanVien = new Tab("Nhân viên");
        tabNhanVien.setContent(new ThongKeNhanVien());

        bangTab.getTabs().addAll(tabTongQuan, tabDoanhThu, tabKhachHang, tabNhanVien);
        bangTab.setStyle("-fx-tab-min-width: 120px; -fx-tab-max-width: 180px;");

        this.setCenter(bangTab);
    }

    private void xuatRaCSV() {
        FileChooser hopChonFile = new FileChooser();
        hopChonFile.setTitle("Luu bao cao");
        hopChonFile.setInitialFileName("BaoCaoThongKe_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv");
        hopChonFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage cuaSo = (Stage) this.getScene().getWindow();
        File file = hopChonFile.showSaveDialog(cuaSo);

        if (file != null) {
            try (PrintWriter nguoiGhi = new PrintWriter(new FileWriter(file))) {
                LocalDate homNay = LocalDate.now();
                YearMonth namThang = YearMonth.of(homNay.getYear(), homNay.getMonthValue());
                LocalDate ngayBatDau = namThang.atDay(1);
                LocalDate ngayKetThuc = namThang.atEndOfMonth();

                nguoiGhi.println("BAO CAO THONG KE - " + homNay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                nguoiGhi.println();

                nguoiGhi.println("TONG QUAN");
                BigDecimal tongDoanhThu = thongKeDAO.getTongDoanhThu(ngayBatDau, ngayKetThuc);
                int soHoaDon = thongKeDAO.getSoHoaDon(ngayBatDau, ngayKetThuc);

                nguoiGhi.println("Tong doanh thu," + tongDoanhThu);
                nguoiGhi.println("So hoa don," + soHoaDon);
                nguoiGhi.println();

                nguoiGhi.println("DOANH THU THEO NGAY");
                nguoiGhi.println("Ngay,Doanh thu");
                Map<String, Double> doanhThuNgay = thongKeDAO.getDoanhThuTheoNgay(ngayBatDau, ngayKetThuc, null);
                for (Map.Entry<String, Double> muc : doanhThuNgay.entrySet()) {
                    nguoiGhi.println(muc.getKey() + "," + muc.getValue());
                }

                Alert thongBao = new Alert(Alert.AlertType.INFORMATION);
                thongBao.setTitle("Thanh cong");
                thongBao.setHeaderText(null);
                thongBao.setContentText("Da xuat bao cao thanh cong!");
                thongBao.initOwner(cuaSo);
                thongBao.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert thongBaoLoi = new Alert(Alert.AlertType.ERROR);
                thongBaoLoi.setTitle("Loi");
                thongBaoLoi.setHeaderText(null);
                thongBaoLoi.setContentText("Khong the xuat bao cao: " + ex.getMessage());
                thongBaoLoi.initOwner(cuaSo);
                thongBaoLoi.showAndWait();
            }
        }
    }
}
