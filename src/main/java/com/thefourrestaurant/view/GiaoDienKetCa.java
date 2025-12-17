package com.thefourrestaurant.view;

import com.thefourrestaurant.DAO.ThongKeDAO;
import com.thefourrestaurant.util.Session;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Locale;
import com.thefourrestaurant.connect.ConnectSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GiaoDienKetCa extends BorderPane {

    private static final String COLOR_TEAL = "#1E424D";
    private static final String COLOR_CARD = "#FFFFFF33";
    private static final String COLOR_CARD_INNER = "#B0BAC366";
    private static final String COLOR_GOLD = "#DDB248";

    private Font montserratSemibold;
    private Font montserratExtrabold;

    public GiaoDienKetCa() {
        montserratSemibold = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"), 18);
        montserratExtrabold = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-ExtraBold.ttf"), 20);

        HBox topCards = new HBox(12);
        topCards.setPadding(new Insets(20));
        topCards.setAlignment(Pos.CENTER);

        LocalDate startDate = getShiftStartDate();
        LocalDate endDate = LocalDate.now();

        ThongKeDAO tk = new ThongKeDAO();
        BigDecimal tongDoanhThu = tk.getTongDoanhThu(startDate, endDate);
        String doanhThuStr = formatCurrency(tongDoanhThu != null ? tongDoanhThu : BigDecimal.ZERO);

        String gioLam = computeShiftDurationString();

        int soHoaDonTop = tk.getSoHoaDon(startDate, endDate);
        BigDecimal trungBinhHD = tk.getDoanhThuTrungBinhHD(startDate, endDate);
        String trungBinhStr = formatCurrency(trungBinhHD != null ? trungBinhHD : BigDecimal.ZERO);

        topCards.getChildren().addAll(
            createCard("Doanh thu", doanhThuStr, "#27AE60"),
            createCard("Giờ làm", gioLam, "#2980B9"),
            createCard("Số hóa đơn", String.valueOf(soHoaDonTop), "#8E44AD"),
            createCard("Trung bình/Hóa đơn", trungBinhStr, "#16A085")
        );

        VBox contentCard = new VBox(20);
        contentCard.setPadding(new Insets(18));
        contentCard.setStyle("-fx-background-color: " + COLOR_CARD + "; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8, 0, 0, 3);");

        GridPane form = new GridPane();
        form.setHgap(40);
        form.setVgap(12);
        form.setPadding(new Insets(10));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        form.getColumnConstraints().addAll(col1, col2);

        VBox leftCol = new VBox(12);
        Label lblTienDauCa = new Label("Tiền đầu ca:");
        lblTienDauCa.setFont(montserratSemibold);
        lblTienDauCa.setTextFill(Color.WHITE);
        TextField txtTienDauCa = createValueField();
        BigDecimal tienDau = Session.getStartingCash() != null ? Session.getStartingCash() : BigDecimal.ZERO;
        txtTienDauCa.setText(formatCurrency(tienDau));

        Label lblSoHoaDon = new Label("Tổng số hóa đơn:");
        lblSoHoaDon.setFont(montserratSemibold);
        lblSoHoaDon.setTextFill(Color.WHITE);
        TextField txtSoHoaDon = createValueField();
        int soHoaDon = tk.getSoHoaDon(startDate, endDate);
        txtSoHoaDon.setText(String.valueOf(soHoaDon));

        leftCol.getChildren().addAll(lblTienDauCa, txtTienDauCa, lblSoHoaDon, txtSoHoaDon);

        VBox rightCol = new VBox(12);
        Label lblTienMat = new Label("Tiền mặt thu được:");
        lblTienMat.setFont(montserratSemibold);
        lblTienMat.setTextFill(Color.WHITE);
        TextField txtTienMat = createValueField();
        BigDecimal tienMat = getTotalByPaymentType(startDate, endDate, "Tiền mặt");
        txtTienMat.setText(formatCurrency(tienMat));

        Label lblChuyenKhoan = new Label("Tiền chuyển khoản thu được:");
        lblChuyenKhoan.setFont(montserratSemibold);
        lblChuyenKhoan.setTextFill(Color.WHITE);
        TextField txtChuyenKhoan = createValueField();
        BigDecimal tienCK = getTotalByPaymentType(startDate, endDate, "Chuyển khoản");
        txtChuyenKhoan.setText(formatCurrency(tienCK));

        Label lblTienCuoi = new Label("Tiền cuối ca:");
        lblTienCuoi.setFont(montserratSemibold);
        lblTienCuoi.setTextFill(Color.WHITE);
        TextField txtTienCuoi = createValueField();
        BigDecimal tienCuoi = tienDau.add(tienMat != null ? tienMat : BigDecimal.ZERO);
        txtTienCuoi.setText(formatCurrency(tienCuoi));
        txtTienCuoi.setStyle("-fx-background-color: #E8F8F0; -fx-text-fill: #0B6B3A; -fx-font-weight: bold; -fx-background-radius: 6;");

        rightCol.getChildren().addAll(lblTienMat, txtTienMat, lblChuyenKhoan, txtChuyenKhoan, lblTienCuoi, txtTienCuoi);

        GridPane.setConstraints(leftCol, 0, 0);
        GridPane.setConstraints(rightCol, 1, 0);
        form.getChildren().addAll(leftCol, rightCol);
        GridPane.setHgrow(leftCol, Priority.ALWAYS);
        GridPane.setHgrow(rightCol, Priority.ALWAYS);
        rightCol.setAlignment(Pos.TOP_RIGHT);

        Button btnXacNhan = new Button("Xác nhận kết ca");
        btnXacNhan.setFont(montserratExtrabold);
        btnXacNhan.setPrefHeight(44);
        btnXacNhan.setPrefWidth(220);
        btnXacNhan.setStyle("-fx-background-color: " + COLOR_GOLD + "; -fx-text-fill: #1E424D; -fx-background-radius: 8;");
        btnXacNhan.setOnAction(e -> {
            Session.clear();
            javafx.scene.Scene sc = this.getScene();
            Stage s = sc != null ? (Stage) sc.getWindow() : null;
            if (s != null) new GiaoDienDangNhap().show(s);
        });

        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(12));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.getChildren().add(btnXacNhan);

        contentCard.getChildren().addAll(form);

        VBox centerWrapper = new VBox();
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.getChildren().add(contentCard);
        contentCard.maxWidthProperty().bind(this.widthProperty().multiply(0.8));
        centerWrapper.paddingProperty().bind(Bindings.createObjectBinding(
            () -> new Insets(0, this.getWidth() * 0.1, this.getHeight() * 0.05, this.getWidth() * 0.1),
            this.widthProperty(), this.heightProperty()
        ));

        this.setStyle("-fx-background-color: #F5F5F5;");
        this.setTop(topCards);
        this.setCenter(centerWrapper);
        this.setBottom(bottomBar);
    }

    private String formatCurrency(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        if (value == null) value = BigDecimal.ZERO;
        return nf.format(value);
    }

    private VBox createCard(String title, String value, String color) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(12));
        card.setMinWidth(200);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 6, 0, 0, 2); -fx-border-color: " + color + "; -fx-border-width: 0 0 0 6; -fx-border-radius: 8;");

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #7F8C8D;");

        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    private TextField createValueField() {
        TextField f = new TextField();
        f.setEditable(false);
        f.setMaxWidth(Double.MAX_VALUE);
        f.setPrefWidth(Region.USE_COMPUTED_SIZE);
        f.setStyle("-fx-background-color: " + COLOR_CARD_INNER + "; -fx-background-radius: 6; -fx-text-fill: white; -fx-padding: 8 10 8 10;");
        return f;
    }

    private LocalDate getShiftStartDate() {
        LocalDateTime login = Session.getLoginTime();
        if (login != null) return login.toLocalDate();
        return LocalDate.now();
    }

    private String computeShiftDurationString() {
        LocalDateTime start = Session.getLoginTime();
        if (start == null) return "0h 0m";
        Duration d = Duration.between(start, LocalDateTime.now());
        long hours = d.toHours();
        long minutes = d.minusHours(hours).toMinutes();
        return String.format("%dh %02dm", hours, minutes);
    }

    private BigDecimal getTotalByPaymentType(LocalDate startDate, LocalDate endDate, String tenPTTT) {
        String sql = "SELECT COALESCE(SUM(CT.soLuong * CT.donGia), 0) as Tong FROM HoaDon HD "
                + "JOIN ChiTietHD CT ON HD.maHD = CT.maHD "
                + "JOIN PhuongThucThanhToan PTTT ON HD.maPTTT = PTTT.maPTTT "
                + "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND PTTT.tenPTTT = ? AND HD.isDeleted = 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            ps.setString(3, tenPTTT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal val = rs.getBigDecimal("Tong");
                    return val != null ? val : BigDecimal.ZERO;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

}
