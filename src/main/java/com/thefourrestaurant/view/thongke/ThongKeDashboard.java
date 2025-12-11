package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.ThongKeDAO;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThongKeDashboard extends VBox {

    private final ThongKeDAO thongKeDAO;
    private final NumberFormat currencyFormat;
    private final HBox cardsContainer;
    private final HBox chartsContainer;
    private final ComboBox<Integer> cboThang;
    private final ComboBox<Integer> cboNam;

    public ThongKeDashboard() {
        this.thongKeDAO = new ThongKeDAO();
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #F5F5F5;");

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        Label lblThang = new Label("Thang:");
        lblThang.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        cboThang = new ComboBox<>();
        cboThang.getItems().addAll(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        cboThang.setValue(currentMonth);

        Label lblNam = new Label("Nam:");
        lblNam.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        cboNam = new ComboBox<>();
        cboNam.getItems().addAll(IntStream.rangeClosed(currentYear - 5, currentYear).boxed().sorted((a, b) -> b - a).toList());
        cboNam.setValue(currentYear);

        ButtonSample btnCapNhat = new ButtonSample("Cap Nhat", 50, 25, 14);
        btnCapNhat.getStyleClass().add("button_sampleGamboge");
        btnCapNhat.setOnAction(e -> loadData());

        filterBox.getChildren().addAll(lblThang, cboThang, lblNam, cboNam, btnCapNhat);

        cardsContainer = new HBox(15);
        cardsContainer.setAlignment(Pos.CENTER);

        chartsContainer = new HBox(20);
        chartsContainer.setAlignment(Pos.CENTER);
        HBox.setHgrow(chartsContainer, Priority.ALWAYS);

        this.getChildren().addAll(filterBox, cardsContainer, chartsContainer);

        loadData();
    }

    private void loadData() {
        int month = cboThang.getValue();
        int year = cboNam.getValue();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        BigDecimal tongDoanhThu = thongKeDAO.getTongDoanhThu(startDate, endDate);
        int soHoaDon = thongKeDAO.getSoHoaDon(startDate, endDate);
        int soKhachHangMoi = thongKeDAO.getSoKhachHangMoi(startDate, endDate);
        int soMonAnBanRa = thongKeDAO.getSoMonAnBanRa(startDate, endDate);
        BigDecimal trungBinhHD = thongKeDAO.getDoanhThuTrungBinhHD(startDate, endDate);

        cardsContainer.getChildren().clear();
        cardsContainer.getChildren().addAll(
            createCard("Tong Doanh Thu", currencyFormat.format(tongDoanhThu), "#27AE60"),
            createCard("So Hoa Don", String.valueOf(soHoaDon), "#2980B9"),
            createCard("Khach Hang Moi", String.valueOf(soKhachHangMoi), "#8E44AD"),
            createCard("Mon An Ban Ra", String.valueOf(soMonAnBanRa), "#E67E22"),
            createCard("TB/Hoa Don", currencyFormat.format(trungBinhHD), "#16A085")
        );

        chartsContainer.getChildren().clear();

        Map<String, Double> doanhThuTheoNgay = thongKeDAO.getDoanhThuTheoNgay(startDate, endDate, null);
        if (!doanhThuTheoNgay.isEmpty()) {
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Ngay");
            yAxis.setLabel("Doanh thu (VND)");
            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Doanh thu theo ngay");
            lineChart.setLegendVisible(false);
            lineChart.setPrefSize(800, 500);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<String, Double> entry : doanhThuTheoNgay.entrySet()) {
                String label = entry.getKey().length() > 5 ? entry.getKey().substring(5) : entry.getKey();
                series.getData().add(new XYChart.Data<>(label, entry.getValue()));
            }
            lineChart.getData().add(series);
            chartsContainer.getChildren().add(lineChart);
        }

        Map<String, Integer> thongKeGio = thongKeDAO.getThongKeGioPhucVu(startDate, endDate);
        if (!thongKeGio.isEmpty()) {
            CategoryAxis xAxis2 = new CategoryAxis();
            NumberAxis yAxis2 = new NumberAxis();
            xAxis2.setLabel("Gio");
            yAxis2.setLabel("So hoa don");
            BarChart<String, Number> barChart = new BarChart<>(xAxis2, yAxis2);
            barChart.setTitle("Gio cao diem");
            barChart.setLegendVisible(false);
            barChart.setPrefSize(700, 500);

            XYChart.Series<String, Number> series2 = new XYChart.Series<>();
            for (Map.Entry<String, Integer> entry : thongKeGio.entrySet()) {
                series2.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChart.getData().add(series2);
            chartsContainer.getChildren().add(barChart);
        }
    }

    private VBox createCard(String title, String value, String color) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setMinWidth(180);
        card.setMaxWidth(220);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4); " +
                      "-fx-border-color: " + color + "; -fx-border-width: 0 0 0 5; -fx-border-radius: 10;");

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #7F8C8D;");

        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }
}